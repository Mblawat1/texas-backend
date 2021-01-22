package com.texas.holdem.elements.players;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayerDTO{
    private String nickname;

    private int avatar = 0;
}
