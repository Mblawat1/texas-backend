package com.texas.holdem.logic;

import com.texas.holdem.elements.cards.Card;
import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.elements.cards.CommunitySet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandAnalyzer {

    HandUtilities utility = new HandUtilities();

    public HandAnalyzer() { }

    public String translateHand(int handNumber) {
        String handName = new String();
        HashMap<Integer, String> hierarchy = new HashMap<Integer, String>();
        hierarchy.put(0, "HIGH_CARD");
        hierarchy.put(1, "PAIR");
        hierarchy.put(2, "TWO_PAIR");
        hierarchy.put(3, "THREE_OF_A_KIND");
        hierarchy.put(4, "STRAIGHT");
        hierarchy.put(5, "FLUSH");
        hierarchy.put(6, "FULL_HOUSE");
        hierarchy.put(7, "FOUR_OF_A_KIND");
        hierarchy.put(8, "STRAIGHT_FLUSH");
        hierarchy.put(9, "ROYAL_FLUSH");
        handName = hierarchy.get(handNumber);
        return handName;
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

    public int checkHighCard(ArrayList<Card> set) {
        Card highCard = new Card();
        ArrayList<Integer> sortedSet = utility.sortSet(set);
        return sortedSet.get(sortedSet.size()-1);
    }

    public ArrayList<Card> makeSet(HoleSet holeSet, CommunitySet communitySet) {
        ArrayList<Card> totalSet = new ArrayList<>();
        totalSet.add(holeSet.getHoleCard1());
        totalSet.add(holeSet.getHoleCard2());
        totalSet.addAll(communitySet.getCommunitySet());
        return totalSet;
    }

    public List<ArrayList<Card>> makeFiveHandSets(ArrayList<Card> set) {
        ArrayList<Card> fiveHandSet = new ArrayList<>();
        List<ArrayList<Card>> sets = new ArrayList<>();
        String[] allPerms = "01234,01235,01236,01245,01246,01256,01345,01346,01356,01456,02345,02346,02356,02456,03456,12345,12346,12356,12456,13456".split(",");
        for (int i = 0; i < 20; i++) {
            String currentPerm = allPerms[i];
            fiveHandSet.add(set.get(currentPerm.charAt(0)));
            fiveHandSet.add(set.get(currentPerm.charAt(1)));
            fiveHandSet.add(set.get(currentPerm.charAt(2)));
            fiveHandSet.add(set.get(currentPerm.charAt(3)));
            fiveHandSet.add(set.get(currentPerm.charAt(4)));
            sets.add(fiveHandSet);
        }
        return sets;
    }

}
