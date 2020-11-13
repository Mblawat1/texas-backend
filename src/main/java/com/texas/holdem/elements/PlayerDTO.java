package com.texas.holdem.elements;

public class PlayerDTO{
    private String nickname;
    private int budget;

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

    public PlayerDTO() {
    }

    public PlayerDTO(String nickname, int budget) {
        this.nickname = nickname;
        this.budget = budget;
    }
}
