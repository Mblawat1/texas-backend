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
    private int avatar;
    private boolean pass;
    private boolean active;
    @JsonIgnore
    private boolean starting;
    @JsonIgnore
    private boolean check;
    private boolean ready;
    private String lastAction;

    public Player(int id, String nickname, int budget,int avatar, HoleSet holeSet) {
        this.id = id;
        this.nickname = nickname;
        this.budget = budget;
        this.holeSet = holeSet;
        this.avatar = avatar;
        pass = true;
        active = false;
        starting = false;
        check = false;
        ready = false;
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
