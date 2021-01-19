package com.texas.holdem.service;

import com.texas.holdem.elements.Player;
import com.texas.holdem.elements.PlayerDTO;
import com.texas.holdem.elements.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Autowired
    RoomService roomService;

    public int addPlayer(String roomId, PlayerDTO playerDTO) {
        var room = roomService.getRoomOrThrow(roomId);
        if (room.getPlayers().size() == 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is full");

        return room.addPlayer(playerDTO);
    }

    public void deletePlayer(String roomId, int playerId) {
        var room = roomService.getRoomOrThrow(roomId);

        var player = room.getPlayerOrThrow(playerId);

        if (player.isActive())
            room.nextTurn(playerId);
        room.deletePlayer(playerId);
    }

    public void setBet(String roomId, int playerId, int bet) {
        var room = roomService.getRoomOrThrow(roomId);

        var player = room.getPlayerOrThrow(playerId);

        var players = room.getPlayers();

        var notPassed = room.getPlayers()
                .stream()
                .filter(n -> !n.isPass())
                .collect(Collectors.toList());
        var maxBet = notPassed.stream()
                .max(Comparator.comparingInt(Player::getBet))
                .map(n -> n.getBet()).get();

        if (player.isPass())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player passed");

        if (!player.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It isn't your turn");

        if (player.getBudget() < bet)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your budget is too low");

        if(player.getBet()+bet < maxBet)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your bet is too low");

        betHelper(player,room,bet);

        if(player.getBet() > maxBet) {
            notPassed.forEach(n -> n.setCheck(false));
            player.setLastAction("raise");
        }else
            player.setLastAction("call");

        player.setCheck(true);
        room.nextTurn(playerId);

        var checked = players.stream().filter(n -> n.isCheck()).count();
        var commSet = room.getTable().getCommunitySet();
        var deck = room.getDeck();

        if(checked == notPassed.size() && commSet.size()<5){
            if(commSet.size() == 0){
                commSet.add(deck.getFirst());
                commSet.add(deck.getFirst());
            }
            commSet.add(deck.getFirst());
            players.forEach(n -> n.setCheck(false));
        }

    }

    public void pass(String roomId, int playerId) {
        var room = roomService.getRoomOrThrow(roomId);

        var player = room.getPlayerOrThrow(playerId);

        if (player.isPass())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player already passed");

        if (!player.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It isn't your turn");

        player.setPass(true);

        player.setLastAction("fold");

        room.nextTurn(playerId);

    }

    public void setReady(String roomId, int playerId) {
        var room = roomService.getRoomOrThrow(roomId);

        var player = room.getPlayerOrThrow(playerId);

        player.setReady(!player.isReady());

        var players = room.getPlayers();

        var ready = players.stream().filter(n -> n.isReady()).count();

        if(ready == players.size()) {
            roomService.startRound(roomId);
            players.forEach(n -> n.setReady(false));
        }
    }

    private void betHelper(Player player,Room room, int bet){
        player.addBet(bet);
        player.subBudget(bet);
        room.addCoinsInRound(bet);
    }

}
