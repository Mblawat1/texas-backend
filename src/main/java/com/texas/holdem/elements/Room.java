package com.texas.holdem.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
public class Room {
    String id;
    ArrayList<Player> players;
    Table table;
    private int startingBudget = 5000;
    @JsonIgnore
    Deck deck;

    public Room(String id, Table table) {
        this.id = id;
        this.players = new ArrayList<Player>();
        this.table = table;
        deck = new Deck();
    }

    public Room() {
    }

    public int addPlayer(PlayerDTO playerDTO) {
        int newPlayerId;
        if (players.isEmpty())
            newPlayerId = 1;
        else
            newPlayerId = players.get(players.size() - 1).getId() + 1;
        var player = new Player(newPlayerId, playerDTO.getNickname(), startingBudget, new HoleSet());
        players.add(player);

        if (player.getId() == players.get(0).getId())
            player.setStarting(true);
        return newPlayerId;
    }

    public void deletePlayer(int id) {
        players.removeIf(n -> n.getId() == id);
    }

    public Optional<Player> getPlayer(int id) {
        return players.stream().filter(n -> n.id == id).findFirst();
    }

    public void nextTurn(int playerId) {
        var optActivePlayer = players.stream().filter(n -> n.getId() == playerId).findFirst();

        if (optActivePlayer.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");

        var activePlayer = optActivePlayer.get();
        activePlayer.setActive(false);
        //szukam pierwszego aktywnego
        var lowerId = players.stream().filter(n -> !n.isPass()).findFirst();
        //szukam pierwszego aktywnego z wyższym id
        var higherId = players.stream().filter(n -> !n.isPass() && n.id > playerId).findFirst();
        //jeśli jest z wyższym id to zmieniam jego
        if (higherId.isPresent()) {
            var pla = higherId.get();
            pla.setActive(true);
        } else if (lowerId.isPresent()) {
            var pla = lowerId.get();
            pla.setActive(true);
        }
    }

    public void addCoinsInRound(int bet) {
        table.addCoinsInRound(bet);
    }

    public void nextStarting() {
        var startingPlayer = players.stream().filter(n -> n.isStarting()).findFirst().get();
        startingPlayer.setStarting(false);

        var higherPlayer = players.stream().filter(n -> n.getId() > startingPlayer.getId()).findFirst();

        if (higherPlayer.isPresent())
            higherPlayer.get().setStarting(true);
        else
            players.get(0).setStarting(true);
    }
}
