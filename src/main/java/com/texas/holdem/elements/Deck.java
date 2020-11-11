package com.texas.holdem.elements;

import java.util.ArrayList;

public class Deck {

    ArrayList<Card> deck;

    public Deck() {
        ArrayList<Card> newDeck = new ArrayList<>(52);
        for (int i = 2; i < 15; i++) {
            newDeck.add(new Card(i, "spade"));
            newDeck.add(new Card(i, "club"));
            newDeck.add(new Card(i, "diamond"));
            newDeck.add(new Card(i, "heart"));
        }
        this.deck = newDeck;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

}
