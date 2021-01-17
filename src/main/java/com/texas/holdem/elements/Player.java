package com.texas.holdem.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player {
    int id;
    String nickname;
    int budget;
    HoleSet holeSet;
    private int bet;
    private boolean pass;
    private boolean active;
    @JsonIgnore
    private boolean starting;
    private boolean check;

    public Player(int id, String nickname, int budget, HoleSet holeSet) {
        this.id = id;
        this.nickname = nickname;
        this.budget = budget;
        this.holeSet = holeSet;
        pass = false;
        active = false;
        starting = false;
        check = false;
    }

    public void addBet(int bet) {
        this.bet += bet;
    }

    public void subBudget(int bet) {
        budget -= bet;
    }

    public void addBudget(int prize) {
        budget += prize;
    }

}
