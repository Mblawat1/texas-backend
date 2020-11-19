package com.texas.holdem.elements;

public class Player {
    int id;
    String nickname;
    int budget;
    HoleSet holeSet;
    private int bet;
    private boolean pass;
    private boolean active;

    public Player(int id, String nickname, int budget, HoleSet holeSet) {
        this.id = id;
        this.nickname = nickname;
        this.budget = budget;
        this.holeSet = holeSet;
        pass = false;
        active= id == 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public HoleSet getHoleSet() {
        return holeSet;
    }

    public void setHoleSet(HoleSet holeSet) {
        this.holeSet = holeSet;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addBet(int bet) {
        this.bet+=bet;
    }

    public void subBudget(int bet) {
        budget-=bet;
    }
}
