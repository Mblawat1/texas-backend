package com.texas.holdem.service;

import com.texas.holdem.elements.PlayerDTO;
import com.texas.holdem.elements.RoomId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    RoomService roomService;

    public HttpStatus addPlayer(String roomId, PlayerDTO playerDTO){
        var optRoom = roomService.getRoom(roomId);
        if (optRoom.isEmpty())
            return HttpStatus.NOT_FOUND;
        var room = optRoom.get();
        if (room.getPlayers().size() == 8)
            return HttpStatus.BAD_REQUEST;
        room.addPlayer(playerDTO);
        return HttpStatus.OK;
    }

    public HttpStatus deletePlayer(String roomId, int playerId) {
        var optRoom = roomService.getRoom(roomId);
        if (optRoom.isEmpty())
            return HttpStatus.NOT_FOUND;
        var room = optRoom.get();
        room.deletePlayer(playerId);
        return HttpStatus.OK;
    }

    public HttpStatus setBet(String roomId, int playerId, int bet) {
        var optRoom = roomService.getRoom(roomId);
        if (optRoom.isEmpty())
            return HttpStatus.NOT_FOUND;
        var room = optRoom.get();
        var player = room.getPlayers().get(playerId);

        if(player.isPass() || !player.isActive() || player.getBudget()<bet)
            return HttpStatus.BAD_REQUEST;

        player.addBet(bet);
        player.subBudget(bet);
        room.addCoinsInRound(bet);

        room.nextTurn(playerId);
        return HttpStatus.OK;
    }

    public HttpStatus pass(String roomId, int playerId) {
        var optRoom = roomService.getRoom(roomId);
        if (optRoom.isEmpty())
            return HttpStatus.NOT_FOUND;
        var room = optRoom.get();
        var player = room.getPlayers().get(playerId);

        if(player.isPass() || !player.isActive())
            return HttpStatus.BAD_REQUEST;

        player.setPass(true);
        room.nextTurn(playerId);
        return HttpStatus.OK;
    }
}
