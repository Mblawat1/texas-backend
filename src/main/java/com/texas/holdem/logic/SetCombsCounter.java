package com.texas.holdem.logic;

import com.texas.holdem.elements.Card;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SetCombsCounter {
    private ArrayList<Card> set;

    public int getPairCount(ArrayList<Card> set) {
        int pairCount = 0;
        ArrayList<Integer> valuesSet = new ArrayList<>();
        set.forEach(card -> valuesSet.add(card.getNumber()));
        Collections.sort(valuesSet);
        int i = 0;
        while (i < valuesSet.size()-1){
            int number = valuesSet.get(i);
            int counter = 1;
            i++;
            while (i < valuesSet.size()-1 && valuesSet.get(i) == number) {
                counter++;
                i++;
            }
            if (counter == 2) {
                pairCount++;
            }
        }
        return pairCount;
    }

    public int getTriplesCount(ArrayList<Card> set) {
        int triCount = 0;
        ArrayList<Integer> valuesSet = new ArrayList<>();
        set.forEach(card -> valuesSet.add(card.getNumber()));
        Collections.sort(valuesSet);
        int i = 0;
        while (i < valuesSet.size()-1){
            int number = valuesSet.get(i);
            int counter = 1;
            i++;
            while (i < valuesSet.size()-1 && valuesSet.get(i) == number) {
                counter++;
                i++;
            }
            if (counter == 3 && valuesSet.get(i) != valuesSet.get(i-3)) {
                triCount++;
                System.out.println("Triple found at " + valuesSet.get(i-2));
            }
        }
        return triCount;
    }

    public int getQuadsCount(ArrayList<Card> set) {
        int quadCount = 0;
        ArrayList<Integer> valuesSet = new ArrayList<>();
        set.forEach(card -> valuesSet.add(card.getNumber()));
        Collections.sort(valuesSet);
        System.out.println(valuesSet);
        int i = 0;
        while (i < valuesSet.size()-1){
            int number = valuesSet.get(i);
            int counter = 1;
            i++;
            while (i < valuesSet.size()-1 && valuesSet.get(i) == number) {
                counter++;
                i++;
            }
            if (counter == 4) {
                quadCount++;
                System.out.println("Quadruple found at " + valuesSet.get(i-3));
            }
        }
        return quadCount;
    }

}
