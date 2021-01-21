package com.texas.holdem;

import com.texas.holdem.elements.Card;
import com.texas.holdem.logic.HandUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class HoldemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoldemApplication.class, args);
        ArrayList<Card> test = new ArrayList<>();
        test.add(new Card(11, "sada"));
        test.add(new Card(12, "sada"));
        test.add(new Card(14, "sada"));
        test.add(new Card(10, "sada"));
        test.add(new Card(13, "sads"));
        

        HandUtilities utility = new HandUtilities();
        System.out.println(utility.getNCount(test, 2));
        System.out.println(utility.getNCount(test, 3) + " triples");
        System.out.println(utility.getNCount(test, 4));
        System.out.println(utility.checkRoyalFlush(test).toString());

    }

}
