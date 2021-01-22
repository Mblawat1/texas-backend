package com.texas.holdem.logic;

import com.texas.holdem.elements.cards.Card;
import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.elements.cards.CommunitySet;
import javafx.util.Pair;

import java.util.*;

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


    public HandOutcome getHandOutcome(ArrayList<Card> set) {
        if (utility.checkRoyalFlush(set).getHandValue() == 9) return utility.checkRoyalFlush(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 8) return utility.checkStraightFlush(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 7) return utility.checkFourOfAKind(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 6) return utility.checkFullHouse(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 5) return utility.checkFlush(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 4) return utility.checkStraight(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 3) return utility.checkThreeOfAKind(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 2) return utility.checkTwoPairs(set);
        else if (utility.checkRoyalFlush(set).getHandValue() == 1) return utility.checkPair(set);
        else return new HandOutcome.Builder(0).withSingleHighest(checkHighCard(set)).build();
    }

    public int checkHighCard(ArrayList<Card> set) {
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

    public HandOutcome getPlayersWinningHand(List<ArrayList<Card>> playerSets) {
        int finalHandValue = 0;
        int finalSingleHighest = 0;
        int finalHighestIncluded = 0;
        ArrayList<Card> finalWinningHand = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            HandOutcome outcome = getHandOutcome(playerSets.get(i));
            if (outcome.getHandValue() > finalHandValue) {
                finalHandValue = outcome.getHandValue();
                finalHighestIncluded = outcome.getHighestIncluded();
                finalSingleHighest = outcome.getSingleHighest();
                finalWinningHand = playerSets.get(i);
            }
            else if (outcome.getHandValue() == finalHandValue && outcome.getHighestIncluded() <)
        }
        return new HandOutcome.Builder(finalHandValue).build();
    }

}
