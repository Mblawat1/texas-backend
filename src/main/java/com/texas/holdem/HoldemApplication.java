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
        HoleSet hs1 = new HoleSet();
        hs1.setHoleCard1(new Card(5, "heart"));
        hs1.setHoleCard2(new Card(5, "spade"));
        ArrayList<Card> communitySet = new ArrayList<>();
        communitySet.add(new Card(10, "diamond"));
        communitySet.add(new Card(12, "heart"));
        communitySet.add(new Card(12, "diamond"));
        communitySet.add(new Card(5, "diamond"));
        communitySet.add(new Card(11, "diamond"));
        cs.setCommunitySet(communitySet);
        HandOutcome playerOutcome = analyzer.getPlayersWinningHand(1, hs1, cs);
        HandOutcome secondOutcome = analyzer.getPlayersWinningHand(2, new HoleSet(new Card(12, "spade"), new Card(12, "club")), cs);
        HandOutcome thirdOutcome = analyzer.getPlayersWinningHand(3, new HoleSet(new Card(13, "diamond"), new Card(9, "diamond")), cs);
        List<HandOutcome> outcomes = new ArrayList<>();
        outcomes.add(playerOutcome);
        outcomes.add(secondOutcome);
        outcomes.add(thirdOutcome);
        System.out.println(analyzer.getWinner(outcomes));

    }

}
