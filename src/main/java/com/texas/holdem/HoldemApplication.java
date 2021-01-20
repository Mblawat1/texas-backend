package com.texas.holdem;

import com.texas.holdem.elements.Card;
import com.texas.holdem.logic.SetCombsCounter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Set;

@SpringBootApplication
public class HoldemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoldemApplication.class, args);
        ArrayList<Card> test = new ArrayList<>();
        test.add(new Card(9, "sada"));
        test.add(new Card(10, "sadna"));
        test.add(new Card(2, "ahbdj"));
        test.add(new Card(2, "sada"));
        test.add(new Card(3, "sada"));
        test.add(new Card(2, "sada"));
        test.add(new Card(2, "sada"));
        test.add(new Card(4, "sada"));
        test.add(new Card(5, "sada"));
        test.add(new Card(9, "sada"));
        test.add(new Card(9, "sada"));
        test.add(new Card(10, "sada"));
        test.add(new Card(10, "sadajb"));
        test.add(new Card(10, "sadajh"));
        test.add(new Card(5, "sbhsb"));

        SetCombsCounter counter = new SetCombsCounter();
        System.out.println(counter.getPairCount(test));
        System.out.println(counter.getTriplesCount(test));
        System.out.println(counter.getQuadsCount(test));

    }

}
