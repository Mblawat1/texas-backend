package com.texas.holdem.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Table {
    @JsonIgnore
    String roomId;
    private int coinsInRound;

    public Table(String roomId) {
        this.roomId = roomId;
        coinsInRound = 0;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getCoinsInRound() {
        return coinsInRound;
    }

    public void setCoinsInRound(int coinsInRound) {
        this.coinsInRound = coinsInRound;
    }

    public void addCoinsInRound(int bet) {
        coinsInRound += bet;
    }
}
