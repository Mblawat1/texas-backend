package com.texas.holdem.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Table {
    @JsonIgnore
    String roomId;
    private int coinsInRound;

    public Table(String roomId) {
        this.roomId = roomId;
        coinsInRound = 0;
    }


    public void addCoinsInRound(int bet) {
        coinsInRound += bet;
    }
}
