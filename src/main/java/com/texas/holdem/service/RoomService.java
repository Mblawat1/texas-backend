package com.texas.holdem.service;

import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.elements.players.Player;
import com.texas.holdem.elements.players.PlayerDTO;
import com.texas.holdem.elements.room.Room;
import com.texas.holdem.elements.room.RoomId;
import com.texas.holdem.elements.room.Table;
import com.texas.holdem.logic.HandAnalyzer;
import com.texas.holdem.logic.HandOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private HashMap<RoomId, Room> rooms;

    @Autowired
    HandAnalyzer handAnalyzer;

    public RoomService() {
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
     *
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
     *
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

        var bigBlind = room.getStartingBudget() / 50;

        players.forEach(n -> {
            if (n.getBudget() <= 0)
                n.setPass(true);
            if (n.isStarting()) {
                room.nextTurn(n.getId());
                n.setBet(bigBlind);
                n.subBudget(bigBlind);

                var lowestId = players.get(0).getId();
                Player smallBlind;
                if (n.getId() == lowestId) {
                    smallBlind = players.get(players.size() - 1);
                } else {
                    smallBlind = players.stream()
                            .filter(p -> p.getId() < n.getId())
                            .max(Comparator.comparing(Player::getId)).get();
                }
                smallBlind.setBet(bigBlind / 2);
                smallBlind.subBudget(bigBlind / 2);
            }
        });

        room.addCoinsInRound(bigBlind + bigBlind / 2);

        var deck = room.getDeck();
        deck.shuffle();

        room.getTable().getCommunitySet().clear();

        players.forEach(n -> n.setHoleSet(new HoleSet(deck.getFirst(), deck.getFirst())));

        room.getTable().setStatus("game");
        room.getTable().setMaxBet(bigBlind);
    }

    public void dealCards(String roomId) {
        var room = getRoomOrThrow(roomId);
        var players = room.getPlayers();
        var notPassed = room.getNotPassedPlayers();
        var table = room.getTable();

        var checked = notPassed.stream().filter(n -> n.isCheck()).count();
        var commSet = table.getCommunitySet();
        var deck = room.getDeck();

        if (checked == notPassed.size() && commSet.size() < 5) {
            if (commSet.size() == 0) {
                commSet.add(deck.getFirst());
                commSet.add(deck.getFirst());
                commSet.add(deck.getFirst());
            } else
                commSet.add(deck.getFirst());
            players.forEach(n -> n.setBet(0));
            table.setMaxBet(0);
            players.forEach(n -> n.setCheck(false));
            notPassed.forEach(n -> n.setLastAction(null));
        }
    }

    public Optional<List<String>> getWinners(String roomId) {
        var room = getRoomOrThrow(roomId);
        var notPassed = room.getNotPassedPlayers();
        var table = room.getTable();

        var checked = notPassed.stream().filter(n -> n.isCheck()).count();

        if (checked == notPassed.size()) {
            ArrayList<HandOutcome> outcomes = new ArrayList<>();
            notPassed.forEach(p -> outcomes.add(handAnalyzer.getPlayersWinningHand(p.getId(), p.getHoleSet(), table.getCommunitySet())));
            ArrayList<Integer> winnersIds = handAnalyzer.getWinner(outcomes);

            var winners = notPassed
                    .stream()
                    .filter(p -> winnersIds.contains(p.getId()))
                    .collect(Collectors.toList());

            var prize = table.getCoinsInRound() / winners.size();
            winners.forEach(p -> p.addBudget(prize));
            table.setCoinsInRound(0);
            var winnersList = winners.stream().map(n -> n.getNickname()).collect(Collectors.toList());
            startRound(roomId);
            return Optional.of(winnersList);
        }
        return Optional.empty();
    }
}

