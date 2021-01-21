package com.texas.holdem.logic;

import com.texas.holdem.elements.Card;
import lombok.*;

import java.util.*;
import java.util.function.Predicate;

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
        //findings structure: first: highest card by number, (optional) second, then first highest card included in a set,
        //number of n-sized combinations found in a set
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
        System.out.println(valuesSet);
        return findings;
    }


    public HandOutcome checkRoyalFlush(ArrayList<Card> fiveHandSet) {
        ArrayList<Integer> ranks = sortSet(fiveHandSet);
        String suit = fiveHandSet.get(0).getSuit();
        boolean ifRightRanks = false;
        boolean ifRightSuit = true;
        if(ranks.get(4) == 14 && ranks.get(3) == 13
                && ranks.get(2) == 12 && ranks.get(1) == 11
                && ranks.get(0) == 10)
        { ifRightRanks = true; }
        for (int i = 1; i < 5; i++) {
            if (!(fiveHandSet.get(i).getSuit().equals(suit))) {
                ifRightSuit = false;
                break;
            }
        }
        if(ifRightSuit && ifRightRanks) {
            HandOutcome outcome = new HandOutcome.Builder(1)
                    .withSingleHighest(14)
                    .withHighestIncluded(14)
                    .build();
            return outcome;
        }
        else return new HandOutcome.Builder(0).build();
    }

    public HandOutcome checkThreeOfAKind(ArrayList<Card> fiveHandSet) {
        List<Integer> nCount = getNCount(fiveHandSet, 3);
        if(nCount.get(nCount.size()-1) == 1) {
            HandOutcome outcome = new HandOutcome.Builder(7)
                    .withSingleHighest(nCount.get(0))
                    .withHighestIncluded(nCount.get(1))
                    .build();
            return outcome;
        }
        else return new HandOutcome.Builder(0).build();
    }

    public HandOutcome checkTwoPairs(ArrayList<Card> fiveHandSet) {
        List<Integer> nCount = getNCount(fiveHandSet, 2);
        if(nCount.get(nCount.size()-1) == 2) {
            HandOutcome outcome = new HandOutcome.Builder(8)
                    .withSingleHighest(nCount.get(0))
                    .withHighestIncluded(nCount.get(2))
                    .withNextHighestIncluded(nCount.get(1))
                    .build();
            return outcome;
        }
        else return new HandOutcome.Builder(0).build();
    }

    public HandOutcome checkPair(ArrayList<Card> fiveHandSet) {
        List<Integer> nCount = getNCount(fiveHandSet, 2);
        if(nCount.get(nCount.size()-1) == 1) {
            HandOutcome outcome = new HandOutcome.Builder(9)
                    .withSingleHighest(nCount.get(0))
                    .withHighestIncluded(nCount.get(1))
                    .build();
            return outcome;
        }
        else return new HandOutcome.Builder(0).build();
    }
}
