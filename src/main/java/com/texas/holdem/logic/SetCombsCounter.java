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
public class SetCombsCounter {
    private ArrayList<Card> set;

    public int getNCount(ArrayList<Card> set, int n) {
        int nCount = 0;
        ArrayList<Integer> valuesSet = new ArrayList<>();
        set.forEach(card -> valuesSet.add(card.getNumber()));
        Collections.sort(valuesSet);
        Map<Integer, Integer> occurrences = new HashMap<>();
        for (int i = 2; i < 15; i++) {
            occurrences.put(i, Collections.frequency(valuesSet, i));
        }
        for(Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            if(entry.getValue() == n) {
                nCount++;
            }
        }
        System.out.println(valuesSet);
        return nCount;
    }
}
