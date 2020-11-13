package com.texas.holdem.elements;

public class Player {
    int id;
    String nickname;
    int budget;
    HoleSet holeSet;

    public Player(int id, String nickname, int budget, HoleSet holeSet) {
        this.id = id;
        this.nickname = nickname;
        this.budget = budget;
        this.holeSet = holeSet;
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

}
