package com.texas.holdem;

import com.texas.holdem.elements.cards.Card;
import com.texas.holdem.elements.cards.CommunitySet;
import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.logic.HandAnalyzer;
import com.texas.holdem.logic.HandOutcome;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HoldemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoldemApplication.class, args);

        HandAnalyzer analyzer = new HandAnalyzer();
        ArrayList<Card> community = new ArrayList<>();
        community.add(new Card(12, "diamond"));
        community.add(new Card(12, "club"));
        community.add(new Card(8, "heart"));
        community.add(new Card(12, "spade"));
        community.add(new Card(14, "diamond"));

        CommunitySet cs = new CommunitySet();
        cs.setCommunitySet(community);

        HandOutcome outcome1 = analyzer.getPlayersWinningHand(1, new HoleSet(new Card(14, "spade"), new Card(6, "diamond")), community);
        HandOutcome outcome2 = analyzer.getPlayersWinningHand(2, new HoleSet(new Card(5, "spade"), new Card(8, "diamond")), community);
        HandOutcome outcome3 = analyzer.getPlayersWinningHand(3, new HoleSet(new Card(13, "diamond"), new Card(3, "diamond")), community);
        List<HandOutcome> outcomeList = new ArrayList<>();
        outcomeList.add(outcome1);
        outcomeList.add(outcome2);
        outcomeList.add(outcome3);
        System.out.println("Winner " + analyzer.getWinner(outcomeList));
        System.out.println("First's highest excluded " + outcome1.getFirstHighestExcluded());
    }

}
