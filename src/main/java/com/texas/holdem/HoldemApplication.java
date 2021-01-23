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
        CommunitySet cs = new CommunitySet();
        HoleSet hs = new HoleSet();
        hs.setHoleCard1(new Card(14, "heart"));
        hs.setHoleCard2(new Card(12, "diamond"));
        ArrayList<Card> communitySet = new ArrayList<>();
        communitySet.add(new Card(10, "diamond"));
        communitySet.add(new Card(12, "heart"));
        communitySet.add(new Card(9, "diamond"));
        communitySet.add(new Card(8, "diamond"));
        communitySet.add(new Card(11, "diamond"));
        cs.setCommunitySet(communitySet);
        ArrayList<Card> set = analyzer.makeSet(hs, cs);
        List<ArrayList<Card>> possibleSets = analyzer.makeFiveHandSets(set);
        HandOutcome playerOutcome = analyzer.getPlayersWinningHand(possibleSets);
        playerOutcome.setPlayerId(1);
        HandOutcome secondOutcome = new HandOutcome.Builder(8)
                .withHighestIncluded(13)
                .ofPlayer(2)
                .build();

        List<HandOutcome> outcomes = new ArrayList<>();
        outcomes.add(playerOutcome);
        outcomes.add(secondOutcome);
        System.out.println(analyzer.getWinner(outcomes));
    }

}
