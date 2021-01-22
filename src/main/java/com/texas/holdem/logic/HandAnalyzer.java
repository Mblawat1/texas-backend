package com.texas.holdem.logic;

import com.texas.holdem.elements.cards.Card;
import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.elements.cards.CommunitySet;

import java.util.ArrayList;
import java.util.HashMap;

public class HandAnalyzer {

    public HandAnalyzer() { }

    public String translateHand(int handNumber) {
        String handName = new String();
        HashMap<Integer, String> hierarchy = new HashMap<Integer, String>();
        hierarchy.put(1, "ROYAL_FLUSH");
        hierarchy.put(2, "STRAIGHT_FLUSH");
        hierarchy.put(3, "FOUR_OF_A_KIND");
        hierarchy.put(4, "FULL_HOUSE");
        hierarchy.put(5, "FLUSH");
        hierarchy.put(6, "STRAIGHT");
        hierarchy.put(7, "THREE_OF_A_KIND");
        hierarchy.put(8, "TWO_PAIR");
        hierarchy.put(9, "PAIR");
        hierarchy.put(10, "HIGH_CARD");
        handName = hierarchy.get(handNumber);
        return handName;
    }

    public int checkHand(HoleSet holeSet, CommunitySet communitySet) {
        int hand = 0;
        Card highCard = checkHighCard(holeSet, communitySet);
        ArrayList<Card> set = makeFiveHandSet(holeSet, communitySet);
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

    //to be done by a map/pair
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

    public ArrayList<Card> makeFiveHandSet(HoleSet holeSet, CommunitySet communitySet) {
        ArrayList<Card> set = new ArrayList<>(7);
        set.add(holeSet.getHoleCard1());
        set.add(holeSet.getHoleCard2());
        /*for (Card c: communitySet.getCommunitySet()) {
            set.add(c);
        }
        Collections.sort(set, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                return Integer.valueOf(c2.getRank()).compareTo(c1.getRank());
            }
        });*/
        return set;
    }

}
