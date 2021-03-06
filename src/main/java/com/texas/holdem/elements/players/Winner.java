package com.texas.holdem.elements.players;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Winner {
    private int id;
    private String nickname;
    private String winningHand;
}
