package com.texas.holdem.logic;

import com.texas.holdem.elements.cards.Card;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HandUtilities {
    private ArrayList<Card> set;

    public ArrayList<Integer> sortSet(ArrayList<Card> set) {
        ArrayList<Integer> valuesSet = new ArrayList<>();
        set.forEach(card -> valuesSet.add(card.getRank()));
        Collections.sort(valuesSet);
        return valuesSet;
    }

    public List<Integer> getNCount(ArrayList<Card> set, int n) {
        int nCount = 0;
        List<Integer> findings = new ArrayList<>();
        //struktura findings: pierwszy: wartość bezwzględnie najwyższej karty w ręce, kolejne: najwyższa zawarta w kombinacji, (opcjonalnie) druga najwyższa
        //ostatni element: ilość znalezionych tego typu kombinacji w ręce
        //1 dla 1 pary, trójki, czwórki; 2 dla 2 par
        ArrayList<Integer> valuesSet = sortSet(set);
        findings.add(valuesSet.get(valuesSet.size()-1));
        Map<Integer, Integer> occurrences = new HashMap<>();
        for (int i = 2; i < 15; i++) {
            occurrences.put(i, Collections.frequency(valuesSet, i));
        }
        for(Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            if(entry.getValue() == n) {
                findings.add(entry.getKey());
                nCount++;
            }
        }
        findings.add(nCount);
        return findings;
    }

    public boolean checkSuitsEquality(ArrayList<Card> set) {
        String suit = set.get(0).getSuit();
        boolean ifRightSuit = true;
        for (int i = 1; i < 5; i++) {
            if (!(set.get(i).getSuit().equals(suit))) {
                ifRightSuit = false;
                break;
            }
        }
        return ifRightSuit;
    }


    public HandOutcome checkRoyalFlush(ArrayList<Card> fiveHandSet) {
        ArrayList<Integer> ranks = sortSet(fiveHandSet);
        boolean ifRightSuit = checkSuitsEquality(fiveHandSet);
        boolean ifRightRanks = false;
        if(ranks.get(4) == 14 && ranks.get(3) == 13
                && ranks.get(2) == 12 && ranks.get(1) == 11
                && ranks.get(0) == 10)
        { ifRightRanks = true; }
        if(ifRightSuit && ifRightRanks) {
            System.out.println(ranks);
            HandOutcome outcome = new HandOutcome.Builder(9)
                    .withSingleHighest(14)
                    .withHighestIncluded(14)
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(ranks.get(4))
                .build());
    }

    public HandOutcome checkStraightFlush(ArrayList<Card> fiveHandSet) {
        ArrayList<Integer> ranks = sortSet(fiveHandSet);
        int startRank = ranks.get(0);
        boolean ifRightRanks = false;
        boolean ifRightSuit = checkSuitsEquality(fiveHandSet);
        if((ranks.get(1) == (startRank + 1)) && (ranks.get(2) == (startRank + 2))
                && (ranks.get(3) == (startRank + 3)) && (ranks.get(4) == (startRank + 4)))
        { ifRightRanks = true; }
        if(ifRightSuit && ifRightRanks) {
            HandOutcome outcome = new HandOutcome.Builder(8)
                    .withSingleHighest(ranks.get(4))
                    .withHighestIncluded(ranks.get(4))
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(ranks.get(4))
                .build());
    }

    public HandOutcome checkFourOfAKind(ArrayList<Card> fiveHandSet) {
        List<Integer> nCount = getNCount(fiveHandSet, 4);
        if(nCount.get(nCount.size()-1) == 1) {
            HandOutcome outcome = new HandOutcome.Builder(7)
                    .withSingleHighest(nCount.get(0))
                    .withHighestIncluded(nCount.get(1))
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(nCount.get(0))
                .build());
    }

    public HandOutcome checkFullHouse(ArrayList<Card> fiveHandSet) {
        HandOutcome pairOutcome = checkPair(fiveHandSet);
        HandOutcome threeOutcome = checkThreeOfAKind(fiveHandSet);
        int singleHighest = 0;
        if (pairOutcome.getSingleHighest() <= threeOutcome.getSingleHighest()) { singleHighest = threeOutcome.getSingleHighest(); }
        else singleHighest = pairOutcome.getSingleHighest();
        if(pairOutcome.getHandValue() == 1 && threeOutcome.getHandValue() == 3 && pairOutcome.getHighestIncluded() != threeOutcome.getHighestIncluded()) {
            HandOutcome outcome = new HandOutcome.Builder(6)
                    .withSingleHighest(singleHighest)
                    .withHighestIncluded(threeOutcome.getHighestIncluded())
                    .withSecondHighestIncluded(pairOutcome.getHighestIncluded())
                    .build();
            System.out.println(outcome.getSecondHighestIncluded());
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(singleHighest)
                .build());
    }

    public HandOutcome checkFlush(ArrayList<Card> fiveHandSet) {
        ArrayList<Integer> ranks = sortSet(fiveHandSet);
        boolean ifRightSuit = checkSuitsEquality(fiveHandSet);
        if(ifRightSuit) {
            HandOutcome outcome = new HandOutcome.Builder(5)
                    .withSingleHighest(ranks.get(4))
                    .withHighestIncluded(ranks.get(4))
                    .withSecondHighestIncluded(ranks.get(3))
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(ranks.get(4))
                .build());
    }

    public HandOutcome checkStraight(ArrayList<Card> fiveHandSet) {
        ArrayList<Integer> ranks = sortSet(fiveHandSet);
        int startRank = ranks.get(0);
        boolean ifRightRanks = false;
        if((ranks.get(1) == (startRank + 1)) && (ranks.get(2) == (startRank + 2))
                && (ranks.get(3) == (startRank + 3)) && (ranks.get(4) == (startRank + 4)))
        { ifRightRanks = true; }
        if(ifRightRanks) {
            HandOutcome outcome = new HandOutcome.Builder(4)
                    .withSingleHighest(ranks.get(4))
                    .withHighestIncluded(ranks.get(4))
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(ranks.get(4))
                .build());
    }

    public HandOutcome checkThreeOfAKind(ArrayList<Card> fiveHandSet) {
        List<Integer> nCount = getNCount(fiveHandSet, 3);
        if(nCount.get(nCount.size()-1) == 1) {
            HandOutcome outcome = new HandOutcome.Builder(3)
                    .withSingleHighest(nCount.get(0))
                    .withHighestIncluded(nCount.get(1))
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(nCount.get(0))
                .build());
    }

    public HandOutcome checkTwoPairs(ArrayList<Card> fiveHandSet) {
        List<Integer> nCount = getNCount(fiveHandSet, 2);
        if(nCount.get(nCount.size()-1) == 2) {
            HandOutcome outcome = new HandOutcome.Builder(2)
                    .withSingleHighest(nCount.get(0))
                    .withHighestIncluded(nCount.get(2))
                    .withSecondHighestIncluded(nCount.get(1))
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(nCount.get(0))
                .build());
    }

    public HandOutcome checkPair(ArrayList<Card> fiveHandSet) {
        List<Integer> nCount = getNCount(fiveHandSet, 2);
        if(nCount.get(nCount.size()-1) == 1) {
            HandOutcome outcome = new HandOutcome.Builder(1)
                    .withSingleHighest(nCount.get(0))
                    .withHighestIncluded(nCount.get(1))
                    .build();
            return outcome;
        }
        else return (new HandOutcome.Builder(0)
                .withSingleHighest(nCount.get(0))
                .build());
    }
}
