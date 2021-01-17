package com.texas.holdem.elements;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

@Getter
public class Deck {

    LinkedList<Card> deck;

    public Deck() {
        LinkedList<Card> newDeck = new LinkedList<Card>();
        for (int i = 2; i < 15; i++) {
            newDeck.add(new Card(i, "spade"));
            newDeck.add(new Card(i, "club"));
            newDeck.add(new Card(i, "diamond"));
            newDeck.add(new Card(i, "heart"));
        }
        this.deck = newDeck;
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public Card getFirst(){
        var card = deck.getFirst();
        deck.removeFirst();
        deck.addLast(card);
        return card;
    }

}
