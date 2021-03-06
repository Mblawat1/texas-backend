package com.texas.holdem.service;

import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.elements.players.Player;
import com.texas.holdem.elements.players.PlayerDTO;
import com.texas.holdem.elements.players.Winner;
import com.texas.holdem.elements.room.Room;
import com.texas.holdem.elements.room.RoomId;
import com.texas.holdem.elements.room.Table;
import com.texas.holdem.logic.HandAnalyzer;
import com.texas.holdem.logic.HandOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    /**
     * <h3>Tworzenie pokoju</h3>
     *
     * @return Id utworzonego pokoju
     */
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

    /**
     * <h3>Dodaje gracza do pokoju</h3>
     *
     * @param room pokój
     * @return id nowo utworzonego gracza
     * @throws ResponseStatusException, BAD_REQUEST jeśli pokój jest pełny
     */
    public int addPlayer(Room room, PlayerDTO playerDTO) {
        if (room.getPlayers().size() == 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is full");

        return room.addPlayer(playerDTO);
    }

    /**
     * <h3>Usuwanie gracza z pokoju</h3>
     *
     * @param room   pokój
     * @param playerId id gracza
     */
    public void deletePlayer(Room room, int playerId) {
        var players = room.getPlayers();

        var player = room.getPlayerOrThrow(playerId);

        if (player.isActive())
            room.nextTurn(player);
        room.deletePlayer(playerId);

        if (players.stream().filter(n -> !n.isPass()).count() == 1) {
            var table = room.getTable();
            var lastPlayer = players.get(0);
            lastPlayer.addBudget(lastPlayer.getBet());
            lastPlayer.setBet(0);
            lastPlayer.setCheck(false);
            lastPlayer.setAllIn(false);
            lastPlayer.setPass(true);
            lastPlayer.setActive(false);
            lastPlayer.setStarting(true);
            lastPlayer.setReady(false);
            lastPlayer.setHoleSet(new HoleSet());
            lastPlayer.setWholeRoundBet(0);
            table.setMaxBet(0);
            table.setCoinsInRound(0);
            table.getCommunitySet().clear();
            table.setStatus("lobby");
        }
    }

    /**
     * <h3>Usuwanie pokoju</h3>
     *
     * @param id id pokoju
     */
    public void deleteRoom(String id) {
        var roomId = new RoomId(id);
        rooms.remove(roomId);
    }

    /**
     * <h3>Sprawdzanie czy wszyscy spasowali</h3>
     *
     * @param room pokój
     * @return Optional ze zwycięzcą jeśli wszyscy spasowali, pusty jeśli nie
     */
    public Optional<Winner> checkAllPassed(Room room) {
        var players = room.getPlayers();
        var table = room.getTable();

        var notPassed = room.getNotPassedPlayers();
        if (notPassed.size() == 1) {
            var winner = notPassed.get(0);
            var prize = room.getTable().getCoinsInRound();

            if (winner.isAllIn()) {
                table.setCoinsInRound(0);
                players.stream().filter(n -> n != winner).forEach(n -> {
                    int diff = 0;
                    if (winner.getWholeRoundBet()< n.getWholeRoundBet())
                        diff = Math.abs(n.getWholeRoundBet() - winner.getWholeRoundBet());
                    winner.addBudget(n.getWholeRoundBet() - diff);
                    table.setCoinsInRound(table.getCoinsInRound() + diff);
                });
                winner.addBudget(winner.getWholeRoundBet());
            }else {
                table.setCoinsInRound(0);
                winner.addBudget(prize);
            }

            room.nextStarting();

            return Optional.of(new Winner(winner.getId(), winner.getNickname(), "Last not folded player"));
        }
        return Optional.empty();
    }

    /**
     * <h3>Startowanie rundy</h3>
     *
     * @param room pokój
     * @throws ResponseStatusException BAD_REQUEST jeśli jest mniej niż 2 graczy
     * @throws ResponseStatusException BAD_REQUEST jeśli jest mniej niż dwóch bankrutów
     */
    public void startRound(Room room) {
        var players = room.getPlayers();
        room.getTable().getCommunitySet().clear();
        var notBankrupts = players.stream().filter(p -> p.getBudget() > 0).collect(Collectors.toList());

        if (players.size() < 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough players");

        var bigBlind = room.getStartingBudget() / 50;

        players.forEach(n -> {
            if (n.getBudget() <= 0)
                n.setPass(true);
            else
                n.setPass(false);
            n.setCheck(false);
            n.setAllIn(false);
            n.setLastAction(null);
            n.setBet(0);
            n.setWholeRoundBet(0);
            n.setHoleSet(new HoleSet());
            n.setActive(false);
        });

        if(notBankrupts.size() > 1) {
            players.forEach(n -> {
                if (n.isStarting()) {
                    room.nextTurn(n);
                    n.setBet(bigBlind);
                    n.subBudget(bigBlind);
                    n.setWholeRoundBet(bigBlind);

                    var lowestId = notBankrupts.get(0).getId();
                    Player smallBlind;
                    if (n.getId() == lowestId) {
                        smallBlind = notBankrupts.get(notBankrupts.size() - 1);
                    } else {
                        smallBlind = notBankrupts.stream()
                                .filter(p -> p.getId() < n.getId())
                                .max(Comparator.comparing(Player::getId)).get();
                    }
                    smallBlind.setBet(bigBlind / 2);
                    smallBlind.subBudget(bigBlind / 2);
                    smallBlind.setWholeRoundBet(bigBlind/2);
                }
            });

            room.addCoinsInRound(bigBlind + bigBlind / 2);

            var deck = room.getDeck();
            deck.shuffle();

            notBankrupts.forEach(n -> n.setHoleSet(new HoleSet(deck.getFirst(), deck.getFirst())));

            room.getTable().setStatus("game");
            room.getTable().setMaxBet(bigBlind);
        }else
            players.forEach(n -> n.setReady(false));


    }

    /**
     * <h3>Rozdawanie kart</h3>
     *
     * @param room pokój
     */
    public void dealCards(Room room) {
        var players = room.getPlayers();
        var notPassed = room.getNotPassedPlayers();
        var table = room.getTable();

        var checked = notPassed.stream().filter(n -> n.isCheck()).count();
        var commSet = table.getCommunitySet();
        var deck = room.getDeck();

        if (checked == notPassed.size() && commSet.size() < 5) {
            if (notPassed.stream().anyMatch(n -> n.isAllIn())) {
                while (commSet.size() < 5)
                    commSet.add(deck.getFirst());
            } else if (commSet.size() == 0) {
                commSet.add(deck.getFirst());
                commSet.add(deck.getFirst());
                commSet.add(deck.getFirst());
                players.forEach(n -> n.setCheck(false));
            } else {
                commSet.add(deck.getFirst());
                players.forEach(n -> n.setCheck(false));
            }
            players.forEach(n -> n.setBet(0));
            table.setMaxBet(0);

            notPassed.forEach(n -> n.setLastAction(null));
        }
    }

    /**
     * <h3>Wyszukiwanie zwycięzców po każdym becie</h3>
     *
     * @param room pokój
     * @return Optional ze zwycięzcami jeśli wszyscy sprawdzili, pusty jeśli nie
     */
    public Optional<List<Winner>> getWinners(Room room) {
        var notPassed = room.getNotPassedPlayers();
        var table = room.getTable();
        var players = room.getPlayers();

        var checked = notPassed.stream().filter(n -> n.isCheck()).count();

        if (checked == notPassed.size() || notPassed.stream().allMatch(n -> n.isAllIn())) {
            ArrayList<HandOutcome> outcomes = new ArrayList<>();
            notPassed.forEach(p -> outcomes.add(handAnalyzer.getPlayersWinningHand(p.getId(), p.getHoleSet(), table.getCommunitySet())));
            ArrayList<Integer> winnersIds = handAnalyzer.getWinner(outcomes);

            var winners = notPassed
                    .stream()
                    .filter(p -> winnersIds.contains(p.getId()))
                    .collect(Collectors.toList());

            var prize = table.getCoinsInRound();
            var winnersList = new ArrayList<Winner>();

            if(winners.size() > 1 && winners.stream().anyMatch(n -> n.isAllIn())) {
                var winnersMoney = winners.stream().mapToInt(n -> n.getWholeRoundBet()).sum();
                winners.forEach(winner -> {
                    double percent = winner.getWholeRoundBet()/(winnersMoney * 1.0);
                    winner.addBudget((int) Math.round(prize * percent));
                    table.setCoinsInRound(0);
                });
            }else
            if (winners.size() == 1 && winners.get(0).isAllIn()) {
                var winner = winners.get(0);
                table.setCoinsInRound(0);
                players.stream().filter(n -> n != winner).forEach(n -> {
                    int diff = 0;
                    if (winner.getWholeRoundBet()< n.getWholeRoundBet())
                        diff = n.getWholeRoundBet() - winner.getWholeRoundBet();
                    winner.addBudget(n.getWholeRoundBet() - diff);
                    table.setCoinsInRound(table.getCoinsInRound() + diff);
                });
                winner.addBudget(winner.getWholeRoundBet());
            } else {
                winners.forEach(p -> p.addBudget(prize/winners.size()));
                table.setCoinsInRound(0);
            }
            for (Player player : winners) {
                var hand = handAnalyzer
                        .translateHand(outcomes.stream().filter(n -> n.getPlayerId() == player.getId())
                                .map(n -> n.getHandValue()).findFirst().orElse(1));
                winnersList.add(new Winner(player.getId(), player.getNickname(), hand));
            }
            room.nextStarting();

            return Optional.of(winnersList);
        }
        return Optional.empty();
    }
}

