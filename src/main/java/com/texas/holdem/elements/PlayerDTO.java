package com.texas.holdem.elements;

public class PlayerDTO{
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public PlayerDTO() {
    }

    public PlayerDTO(String nickname) {
        this.nickname = nickname;
    }
}
