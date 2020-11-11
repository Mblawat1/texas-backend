package com.texas.holdem.elements;

public class Room {
    String id;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player player5;
    Player player6;
    Player player7;
    Player player8;
    Table table;

    public Room(String id) {
        this.id = id;
    }

    public Room(String id, Player player1, Player player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Room(String id, Player player1, Player player2, Player player3) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
    }

    public Room(String id, Player player1, Player player2, Player player3, Player player4) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
    }

    public Room(String id, Player player1, Player player2, Player player3, Player player4, Player player5) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.player5 = player5;
    }

    public Room(String id, Player player1, Player player2, Player player3, Player player4, Player player5, Player player6) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.player5 = player5;
        this.player6 = player6;
    }

    public Room(String id, Player player1, Player player2, Player player3, Player player4, Player player5, Player player6, Player player7) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.player5 = player5;
        this.player6 = player6;
        this.player7 = player7;
    }

    public Room(String id, Player player1, Player player2, Player player3, Player player4, Player player5, Player player6, Player player7, Player player8) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.player5 = player5;
        this.player6 = player6;
        this.player7 = player7;
        this.player8 = player8;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayer3() {
        return player3;
    }

    public void setPlayer3(Player player3) {
        this.player3 = player3;
    }

    public Player getPlayer4() {
        return player4;
    }

    public void setPlayer4(Player player4) {
        this.player4 = player4;
    }

    public Player getPlayer5() {
        return player5;
    }

    public void setPlayer5(Player player5) {
        this.player5 = player5;
    }

    public Player getPlayer6() {
        return player6;
    }

    public void setPlayer6(Player player6) {
        this.player6 = player6;
    }

    public Player getPlayer7() {
        return player7;
    }

    public void setPlayer7(Player player7) {
        this.player7 = player7;
    }

    public Player getPlayer8() {
        return player8;
    }

    public void setPlayer8(Player player8) {
        this.player8 = player8;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
