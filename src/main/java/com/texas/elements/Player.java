package com.texas.elements;

public class Player {
    String id;
    String nickname;
    int budget;

    public Player(String id, String nickname, int budget) {
        this.id = id;
        this.nickname = nickname;
        this.budget = budget;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
