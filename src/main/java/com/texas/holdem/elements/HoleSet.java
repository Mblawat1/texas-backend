package com.texas.holdem.elements;

public class HoleSet {
    Card holeCard1;
    Card holeCard2;

    public HoleSet() {}

    public HoleSet(Card holeCard1, Card holeCard2) {
        this.holeCard1 = holeCard1;
        this.holeCard2 = holeCard2;
    }

    public Card getHoleCard1() {
        return holeCard1;
    }

    public void setHoleCard1(Card holeCard1) {
        this.holeCard1 = holeCard1;
    }

    public Card getHoleCard2() {
        return holeCard2;
    }

    public void setHoleCard2(Card holeCard2) {
        this.holeCard2 = holeCard2;
    }
}
