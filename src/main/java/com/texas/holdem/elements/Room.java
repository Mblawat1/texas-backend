package com.texas.holdem.elements;

import java.util.ArrayList;

public class Room {
    int id;
    ArrayList<Player> players;
    Table table;

    public Room(int id, Table table) {
        this.id = id;
        this.players = new ArrayList<Player>();
        this.table = table;
    }

    public Room() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
