package com.texas.holdem.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Table {
    @JsonIgnore
    String roomId;
    private int coinsInRound;
    private ArrayList<Card> communitySet = new ArrayList<Card>();
    private String status;
    private int maxBet;

    public Table(String roomId) {
        this.roomId = roomId;
        status = "lobby";
        coinsInRound = 0;
        maxBet = 0;
    }

    public void addCoinsInRound(int bet) {
        coinsInRound += bet;
    }

    public void addCard(Card card){
        communitySet.add(card);
    }
}
