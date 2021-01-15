package com.texas.holdem.service;

import com.texas.holdem.elements.Player;
import com.texas.holdem.elements.PlayerDTO;
import com.texas.holdem.elements.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    RoomService roomService;

    public int addPlayer(String roomId, PlayerDTO playerDTO) {
        var optRoom = roomService.getRoom(roomId);
        roomExists(optRoom);

        var room = optRoom.get();
        if (room.getPlayers().size() == 8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is full");

        return room.addPlayer(playerDTO);
    }

    public void deletePlayer(String roomId, int playerId) {
        var optRoom = roomService.getRoom(roomId);
        roomExists(optRoom);

        var room = optRoom.get();

        var optPlayer = room.getPlayer(playerId);
        playerExists(optPlayer);

        var player = optPlayer.get();

        if (player.isActive())
            room.nextTurn(playerId);
        room.deletePlayer(playerId);
    }

    public void setBet(String roomId, int playerId, int bet) {
        var optRoom = roomService.getRoom(roomId);
        roomExists(optRoom);

        var room = optRoom.get();

        var optPlayer = room.getPlayer(playerId);
        playerExists(optPlayer);

        var player = optPlayer.get();

        if (player.isPass())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player passed");

        if (!player.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It isn't your turn");

        if (player.getBudget() < bet)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your budget is too low");

        player.addBet(bet);
        player.subBudget(bet);
        room.addCoinsInRound(bet);

        room.nextTurn(playerId);
    }

    public void pass(String roomId, int playerId) {
        var optRoom = roomService.getRoom(roomId);
        roomExists(optRoom);
        var room = optRoom.get();

        var optPlayer = room.getPlayer(playerId);
        playerExists(optPlayer);

        var player = optPlayer.get();

        if (player.isPass())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player passed");

        if (!player.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It isn't your turn");

        player.setPass(true);

        room.nextTurn(playerId);

    }

    private void roomExists(Optional<Room> optRoom) {
        if (optRoom.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
    }

    private void playerExists(Optional<Player> optPlayer) {
        if (optPlayer.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
    }
}
