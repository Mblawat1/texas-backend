package com.texas.holdem.logic;

import com.texas.holdem.elements.HoleSet;
import com.texas.holdem.elements.CommunitySet;

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
        return hand;
    }

    public boolean checkRoyalFlush(HoleSet holeSet, CommunitySet communitySet) {
        return true;
    }

}
