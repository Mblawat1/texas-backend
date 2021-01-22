package com.texas.holdem.service;

import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.elements.players.Player;
import com.texas.holdem.elements.players.PlayerDTO;
import com.texas.holdem.elements.room.Room;
import com.texas.holdem.elements.room.RoomId;
import com.texas.holdem.elements.room.Table;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

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
            id = generateId(4);
        } while (rooms.containsKey(id));

        var room = new Room(id.getId(), new Table(id.getId()));
        rooms.put(id, room);
        return id.getId();
    }

    /**
     * <h3>Generuje unikalne id</h3>
     * @param length długość id
     * @return id jako String
     */
    private RoomId generateId(int length) {
        var rand = new Random();
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            var ch = String.valueOf((char) (rand.nextInt(25) + 65));
            sb.append(ch);
        }
        return new RoomId(sb.toString());
    }

    public int addPlayer(String roomId, PlayerDTO playerDTO) {
        var room = getRoomOrThrow(roomId);
        if (room.getPlayers().size() == 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is full");

        return room.addPlayer(playerDTO);
    }

    public void deletePlayer(String roomId, int playerId) {
        var room = getRoomOrThrow(roomId);

        var player = room.getPlayerOrThrow(playerId);

        if (player.isActive())
            room.nextTurn(playerId);
        room.deletePlayer(playerId);
    }


    /**
     * <h3>Szuka pokoju o podanym id</h3>
     * @param id id pokoju
     * @return Pokój
     * @throws ResponseStatusException z HttpStatus.NOT_FOUND jeśli pokoju nie ma
     */
    public Room getRoomOrThrow(String id) {
        var roomId = new RoomId(id);
        if (rooms.containsKey(roomId))
            return rooms.get(roomId);
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
    }

    public void deleteRoom(String id) {
        var roomId = new RoomId(id);
        rooms.remove(roomId);
    }

    public Optional<String> checkAllPassed(String roomId) {
        var room = getRoomOrThrow(roomId);

        var notPassed = room.getNotPassedPlayers();
        if (notPassed.size() == 1) {
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

            startRound(roomId);

            return Optional.of(winner.getNickname());
        }
        return Optional.empty();
    }

    public void startRound(String roomId) {
        var room = getRoomOrThrow(roomId);

        var players = room.getPlayers();

        if (players.size() < 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough players");

        players.forEach(n -> n.setPass(false));

        var bigBlind = room.getStartingBudget()/50;

        players.forEach(n -> {
            if (n.isStarting()) {
                room.nextTurn(n.getId());
                n.setBet(bigBlind);
                n.subBudget(bigBlind);

                var lowestId = players.get(0).getId();
                Player smallBlind;
                if(n.getId() == lowestId) {
                    smallBlind = players.get(players.size() - 1);
                } else{
                    smallBlind = players.stream()
                            .filter(p -> p.getId() < n.getId())
                            .max(Comparator.comparing(Player::getId)).get();
                }
                smallBlind.setBet(bigBlind/2);
                smallBlind.subBudget(bigBlind/2);
            }
        });
        
        room.addCoinsInRound(bigBlind + bigBlind/2);

        var deck = room.getDeck();
        deck.shuffle();

        players.forEach(n -> n.setHoleSet(new HoleSet(deck.getFirst(), deck.getFirst())));

        room.getTable().setStatus("game");

        room.getTable().setMaxBet(bigBlind);
    }
}

