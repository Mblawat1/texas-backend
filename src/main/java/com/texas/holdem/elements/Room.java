package com.texas.holdem.elements;

import java.util.ArrayList;
import java.util.Optional;

public class Room {
    String id;
    ArrayList<Player> players;
    Table table;
    private int startingBudget = 5000;

    public Room(String id, Table table) {
        this.id = id;
        this.players = new ArrayList<Player>();
        this.table = table;
    }

    public Room() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void addPlayer(PlayerDTO playerDTO) {
        players.add(new Player(players.size(), playerDTO.getNickname(), startingBudget, new HoleSet()));
    }

    public void deletePlayer(int id) {
        players.remove(id);
        for (int i = 0; i < players.size(); i++)
            players.get(i).id = i;
    }

    public Optional<Player> getPlayer(int id) {
        return players.stream().filter(n -> n.id == id).findFirst();
    }

    public void nextTurn(int playerId) {
        var activePlayer = players.get(playerId);
        activePlayer.setActive(false);
        //szukam pierwszego aktywnego
        var lowerId = players.stream().filter(n -> !n.isPass()).findFirst();
        //szukam pierwszego aktywnego z wyższym id
        var higherId = players.stream().filter(n -> !n.isPass() && n.id > playerId).findFirst();
        //jeśli jest z wyższym id to zmieniam jego
        if (higherId.isPresent()) {
            var pla = higherId.get();
            pla.setActive(true);
            players.set(pla.id, pla);
        } else if (lowerId.isPresent()) {
            var pla = lowerId.get();
            pla.setActive(true);
            players.set(pla.id, pla);
        }
        //TODO Tutaj można dodać zakończenie rozdania jak już będzie logika

    }

    public void addCoinsInRound(int bet) {
        table.addCoinsInRound(bet);
    }
}
