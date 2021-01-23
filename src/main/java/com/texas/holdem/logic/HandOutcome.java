package com.texas.holdem.logic;

import com.texas.holdem.elements.cards.Card;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HandOutcome implements Comparable<HandOutcome>{

    private int playerId;
    private int handValue;
    private int highestIncluded; //może być też wartość najwyższej karty w trójce w FULL_HOUSE
    private int secondHighestIncluded; //może być też wartość najwyższej karty w parze w FULL_HOUSE
    private int singleHighest;
    private ArrayList<Card> bestSet;

    @Override
    public int compareTo(HandOutcome outcome) {
        return Comparator.comparingInt(HandOutcome::getHandValue)
                .thenComparingInt(HandOutcome::getHighestIncluded)
                .thenComparingInt(HandOutcome::getSingleHighest)
                .compare(this, outcome);
    }

    public static class Builder {

        private int playerId;
        private int handValue;
        private int highestIncluded;
        private int secondHighestIncluded;
        private int singleHighest;
        private ArrayList<Card> bestSet;

        public Builder(int handValue) {
            this.handValue = handValue;
        }

        public Builder ofPlayer(int playerId) {
            this.playerId = playerId;
            return this;
        }

        public Builder withHighestIncluded(int highestIncluded) {
            this.highestIncluded = highestIncluded;
            return this;
        }

        public Builder withSecondHighestIncluded(int secondHighestIncluded) {
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
            handOutcome.playerId = this.playerId;
            handOutcome.highestIncluded = this.highestIncluded;
            handOutcome.secondHighestIncluded = this.secondHighestIncluded;
            handOutcome.singleHighest = this.singleHighest;
            handOutcome.bestSet = this.bestSet;
            return handOutcome;
        }
    }
}
