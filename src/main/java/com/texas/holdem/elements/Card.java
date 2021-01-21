package com.texas.holdem.elements;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Card {
    int rank;
    String suit;

    public int compareCards(Card first, Card second) {
        int elderCard = 0;
        if (first.getRank() > second.getRank()) {
            elderCard = 1;
        } else if (second.getRank() > first.getRank()) {
            elderCard = 2;
        }
        return elderCard;
    }

}
