package com.texas.holdem.logic;

import com.texas.holdem.elements.cards.Card;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HandOutcome {

    private int handValue;
    private int highestIncluded; //also the rank of three of a kind in the full house
    private int secondHighestIncluded;
    private int singleHighest;
    private ArrayList<Card> bestSet;

    public static class Builder {

        private int handValue;
        private int highestIncluded;
        private int secondHighestIncluded;
        private int singleHighest;
        private ArrayList<Card> bestSet;

        public Builder(int handValue) {
            this.handValue = handValue;
        }

        public Builder withHighestIncluded(int highestIncluded) {
            this.highestIncluded = highestIncluded;
            return this;
        }

        public Builder withNextHighestIncluded(int secondHighestIncluded) {
            this.secondHighestIncluded = secondHighestIncluded;
            return this;
        }

        public Builder withSingleHighest(int singleHighest) {
            this.singleHighest = singleHighest;
            return this;
        }

        public Builder withBestSet(ArrayList<Card> bestSet) {
            this.bestSet = bestSet;
            return this;
        }

        public HandOutcome build() {
            HandOutcome handOutcome = new HandOutcome();
            handOutcome.handValue = this.handValue;
            handOutcome.highestIncluded = this.highestIncluded;
            handOutcome.secondHighestIncluded = this.secondHighestIncluded;
            handOutcome.singleHighest = this.singleHighest;
            return handOutcome;
        }
    }
}
