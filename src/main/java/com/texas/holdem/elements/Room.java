package com.texas.holdem.elements;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;

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

    public void addPlayer(PlayerDTO playerDTO){
        players.add(new Player(players.size(),playerDTO.getNickname(),startingBudget,new HoleSet()));
    }

    public void deletePlayer(int id){
        players.remove(id);
        for(int i=0;i<players.size();i++)
            players.get(i).id = i;
    }
}
