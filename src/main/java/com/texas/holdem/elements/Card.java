package com.texas.holdem.elements;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Card {
    int number;
    String color;

    public int compareCards(Card first, Card second) {
        int elderCard = 0;
        if (first.getNumber() > second.getNumber()) {
            elderCard = 1;
        } else if (second.getNumber() > first.getNumber()) {
            elderCard = 2;
        }
        return elderCard;
    }

}
