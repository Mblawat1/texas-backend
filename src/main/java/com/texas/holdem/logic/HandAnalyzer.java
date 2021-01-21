package com.texas.holdem.logic;

import com.texas.holdem.elements.Card;
import com.texas.holdem.elements.HoleSet;
import com.texas.holdem.elements.CommunitySet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HandAnalyzer {

    public HandAnalyzer() { }

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

    public int getHandValue(ArrayList<Card> set) {
        if(isRoyalFlush(set)) return 1;
        else if(isStraightFlush(set)) return 2;
        else if(isFourOfAKind(set)) return 3;
        else if(isFullHouse(set)) return 4;
        else if(isFlush(set)) return 5;
        else if(isStraight(set)) return 6;
        else if(isThree(set)) return 7;
        else if(isTwoPairs(set)) return 8;
        else if(isPair(set)) return 9;
        else return 10;
    }

    public boolean isRoyalFlush(ArrayList<Card> set) { return false; }

    public boolean isStraightFlush(ArrayList<Card> set) {
        return false;
    }

    public boolean isFourOfAKind(ArrayList<Card> set) {
        return false;
    }

    public boolean isFullHouse(ArrayList<Card> set) {
        return false;
    }

    public boolean isFlush(ArrayList<Card> set) {
        return false;
    }

    public boolean isStraight(ArrayList<Card> set) {
        return false;
    }

    public boolean isThree(ArrayList<Card> set) {
        return false;
    }

    public boolean isTwoPairs(ArrayList<Card> set) {
        return false;
    }

    public boolean isPair(ArrayList<Card> set) {
        return false;
    }

    public Card checkHighCard(HoleSet holeSet, CommunitySet communitySet) {
        Card highCard = new Card();
        for (Card c: communitySet.getCommunitySet()) {
            if (highCard.getRank() <= c.getRank()) {
                highCard = c;
            }
        }
        if (highCard.getRank() <= holeSet.getHoleCard1().getRank()) {
            highCard = holeSet.getHoleCard1();
        }
        if (highCard.getRank() <= holeSet.getHoleCard2().getRank()) {
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
                return Integer.valueOf(c2.getRank()).compareTo(c1.getRank());
            }
        });
        return set;
    }

}
