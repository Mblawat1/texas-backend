package com.texas.holdem.service;

import com.texas.holdem.elements.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private HashMap<RoomId, Room> rooms;

    public RoomService() {
        rooms = new HashMap<RoomId, Room>();
    }

    //returnuje room id
    public String createRoom() {
        RoomId id;
        do {
            id = generateId();
        } while (rooms.containsKey(id));

        var room = new Room(id.getId(), new Table(id.getId()));
        rooms.put(id, room);
        return id.getId();
    }

    public Optional<Room> getRoom(String id) {
        var roomId = new RoomId(id);
        if (rooms.containsKey(roomId))
            return Optional.of(rooms.get(roomId));
        return Optional.empty();
    }

    public Optional<Room> deleteRoom(String id) {
        var roomId = new RoomId(id);
        return Optional.ofNullable(rooms.remove(roomId));
    }


    private RoomId generateId() {
        var rand = new Random();
        var sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            var ch = String.valueOf((char) (rand.nextInt(25) + 65));
            sb.append(ch);
        }
        return new RoomId(sb.toString());
    }

    public String checkAllPassed(String roomId) {
        var optRoom = getRoom(roomId);
        if (optRoom.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        var room = optRoom.get();

        var notPassed = room.getPlayers().stream().filter(n -> !n.isPass()).collect(Collectors.toList());
        if (notPassed.size() > 1)
            throw new ResponseStatusException(HttpStatus.OK);

        var winner = notPassed.get(0);
        var prize = room.getTable().getCoinsInRound();

        winner.addBudget(prize);
        room.getPlayers().forEach(n -> {
            n.setBet(0);
            n.setPass(false);
            n.setActive(false);
        });
        room.getTable().setCoinsInRound(0);

        room.nextStarting();

        room.getTable().getCommunitySet().clear();

        return winner.getNickname();
    }

    public void startRound(String roomId) {
        var optRoom = getRoom(roomId);
        if (optRoom.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        var room = optRoom.get();

        var players = room.getPlayers();

        if (players.size() < 2)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not enough players");

        players.forEach(n -> n.setPass(false));

        players.forEach(n -> {
            if (n.isStarting()) {
                room.nextTurn(n.getId());
                n.setBet(100);
                n.subBudget(100);

                var lowestId = players.get(0).getId();
                Player smallBlind;
                if(n.getId() == lowestId) {
                    smallBlind = players.get(players.size() - 1);
                } else{
                    smallBlind = players.stream()
                            .filter(p -> p.getId() < n.getId())
                            .max(Comparator.comparing(Player::getId)).get();
                }
                smallBlind.setBet(50);
                smallBlind.subBudget(50);
            }
        });
        
        room.addCoinsInRound(150);

        var deck = room.getDeck();
        deck.shuffle();

        players.forEach(n -> n.setHoleSet(new HoleSet(deck.getFirst(), deck.getFirst())));
    }
}

