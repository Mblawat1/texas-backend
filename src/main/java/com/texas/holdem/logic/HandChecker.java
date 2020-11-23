package com.texas.holdem.logic;

import com.texas.holdem.elements.Card;
import com.texas.holdem.elements.HoleSet;
import com.texas.holdem.elements.CommunitySet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HandChecker {

    public HandChecker() { }

    public String translateHand(int handNumber) {
        String handName = new String();
        HashMap<Integer, String> hierarchy = new HashMap<Integer, String>();
        hierarchy.put(1, "Royal flush");
        hierarchy.put(2, "Straight flush");
        hierarchy.put(3, "Four of a kind");
        hierarchy.put(4, "Full house");
        hierarchy.put(5, "Flush");
        hierarchy.put(6, "Straight");
        hierarchy.put(7, "Three of a kind");
        hierarchy.put(8, "Two pair");
        hierarchy.put(9, "Pair");
        hierarchy.put(10, "High card");
        handName = hierarchy.get(handNumber);
        return handName;
    }

    public int checkHand(HoleSet holeSet, CommunitySet communitySet) {
        int hand = 0;
        Card highCard = checkHighCard(holeSet, communitySet);
        ArrayList<Card> set = makeSet(holeSet, communitySet);
        return hand;
    }

    public boolean checkRoyalFlush(ArrayList<Card> set) {

        return false;
    }

    public boolean checkStraightFlush(ArrayList<Card> set) {
        return false;
    }

    public boolean checkFour(ArrayList<Card> set) {
        return false;
    }

    public boolean checkFullHouse(ArrayList<Card> set) {
        return false;
    }

    public boolean checkFlush(ArrayList<Card> set) {
        return false;
    }

    public boolean checkStraight(ArrayList<Card> set) {
        return false;
    }

    public boolean checkThree(ArrayList<Card> set) {
        return false;
    }

    public boolean checkTwoPairs(ArrayList<Card> set) {
        return false;
    }

    public boolean checkPair(ArrayList<Card> set) {
        return false;
    }

    public Card checkHighCard(HoleSet holeSet, CommunitySet communitySet) {
        Card highCard = new Card();
        for (Card c: communitySet.getCommunitySet()) {
            if (highCard.getNumber() <= c.getNumber()) {
                highCard = c;
            }
        }
        if (highCard.getNumber() <= holeSet.getHoleCard1().getNumber()) {
            highCard = holeSet.getHoleCard1();
        }
        if (highCard.getNumber() <= holeSet.getHoleCard2().getNumber()) {
            highCard = holeSet.getHoleCard2();
        }
        return highCard;
    }

    public ArrayList<Card> makeSet(HoleSet holeSet, CommunitySet communitySet) {
        ArrayList<Card> set = new ArrayList<>(7);
        set.add(holeSet.getHoleCard1());
        set.add(holeSet.getHoleCard2());
        for (Card c: communitySet.getCommunitySet()) {
            set.add(c);
        }
        Collections.sort(set, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                return Integer.valueOf(c2.getNumber()).compareTo(c1.getNumber());
            }
        });
        return set;
    }

}
