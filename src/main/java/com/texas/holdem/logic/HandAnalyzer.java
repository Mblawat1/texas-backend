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
        Map<Integer, String> hierarchy = new HashMap<Integer, String>();
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
        else if (utility.checkStraightFlush(set).getHandValue() == 8) return utility.checkStraightFlush(set);
        else if (utility.checkFourOfAKind(set).getHandValue() == 7) return utility.checkFourOfAKind(set);
        else if (utility.checkFullHouse(set).getHandValue() == 6) return utility.checkFullHouse(set);
        else if (utility.checkFlush(set).getHandValue() == 5) return utility.checkFlush(set);
        else if (utility.checkStraight(set).getHandValue() == 4) return utility.checkStraight(set);
        else if (utility.checkThreeOfAKind(set).getHandValue() == 3) return utility.checkThreeOfAKind(set);
        else if (utility.checkTwoPairs(set).getHandValue() == 2) return utility.checkTwoPairs(set);
        else if (utility.checkPair(set).getHandValue() == 1) return utility.checkPair(set);
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
        List<ArrayList<Card>> sets = new ArrayList<>();
        String[] allPerms = {"01234","01235","01236","01245","01246","01256","01345","01346","01356","01456","02345","02346","02356","02456","03456","12345","12346","12356","12456","13456"};
        for (int i = 0; i < 20; i++) {
            String currentPerm = allPerms[i];
            ArrayList<Card> fiveHandSet = new ArrayList<>();
            fiveHandSet.add(set.get(Character.getNumericValue(currentPerm.charAt(0))));
            fiveHandSet.add(set.get(Character.getNumericValue(currentPerm.charAt(1))));
            fiveHandSet.add(set.get(Character.getNumericValue(currentPerm.charAt(2))));
            fiveHandSet.add(set.get(Character.getNumericValue(currentPerm.charAt(3))));
            fiveHandSet.add(set.get(Character.getNumericValue(currentPerm.charAt(4))));
            sets.add(fiveHandSet);
        }
        return sets;
    }

    public HandOutcome getPlayersWinningHand(int id, HoleSet holeSet, CommunitySet communitySet) {
        List<ArrayList<Card>> playerSets = makeFiveHandSets(makeSet(holeSet, communitySet));
        int finalHandValue = 0;
        int finalSingleHighest = 0;
        int finalHighestIncluded = 0;
        int finalSecondIncluded = 0;
        ArrayList<Card> finalWinningHand = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            HandOutcome outcome = getHandOutcome(playerSets.get(i));
            if (outcome.getHandValue() == 9) {
                outcome.setBestSet(playerSets.get(i));
                return outcome;
            }
            if (outcome.getHandValue() > finalHandValue) {
                finalHandValue = outcome.getHandValue();
                finalHighestIncluded = outcome.getHighestIncluded();
                finalSecondIncluded = outcome.getSecondHighestIncluded();
                finalSingleHighest = outcome.getSingleHighest();
                finalWinningHand = playerSets.get(i);
            }
            else if (outcome.getHandValue() == finalHandValue && outcome.getHighestIncluded() > finalHighestIncluded) {
                finalHighestIncluded = outcome.getHighestIncluded();
                finalSecondIncluded = outcome.getSecondHighestIncluded();
                finalSingleHighest = outcome.getSingleHighest();
                finalWinningHand = playerSets.get(i);
            }
            else if (outcome.getHandValue() == finalHandValue && outcome.getHighestIncluded() == finalHighestIncluded && outcome.getSingleHighest() > finalSingleHighest) {
                finalSingleHighest = outcome.getSingleHighest();
                finalSecondIncluded = outcome.getSecondHighestIncluded();
                finalWinningHand = playerSets.get(i);
            }
        }
        return new HandOutcome.Builder(finalHandValue)
                .ofPlayer(id)
                .withHighestIncluded(finalHighestIncluded)
                .withSecondHighestIncluded(finalSecondIncluded)
                .withBestSet(finalWinningHand)
                .withSingleHighest(finalSingleHighest)
                .build();
    }

    public ArrayList<Integer> getWinner(List<HandOutcome> playersBestHands) {
        Collections.sort(playersBestHands);
        int size = playersBestHands.size();
        //ROYAL_FLUSH nie pojawi się u więcej niż 1 gracza jednocześnie
        if (playersBestHands.get(size-1).getHandValue() == 9) {
            ArrayList<Integer> winnerID = new ArrayList<>();
            winnerID.add(playersBestHands.get(size-1).getPlayerId());
            return winnerID;
        }
        if (size == 1) {
            ArrayList<Integer> winnerID = new ArrayList<>();
            winnerID.add(playersBestHands.get(size-1).getPlayerId());
            return winnerID;
        }
        //jeśli w układach z jedną składową, która składa się z 5 kart, np. STRAIGHT, FLUSH,
        // gracze mają te same najwyższe karty w układzie i te same najwyższe karty w ogóle (w układach to dopuszczających)
        //obaj wygrywają
        if ((size > 1 && playersBestHands.get(size-1).getHandValue() == playersBestHands.get(size-2).getHandValue()
                && playersBestHands.get(size-1).getHandValue() != 2 && playersBestHands.get(size-1).getHandValue() != 6)
        && playersBestHands.get(size-1).getHighestIncluded() == playersBestHands.get(size-2).getHighestIncluded()
        && playersBestHands.get(size-1).getSingleHighest() == playersBestHands.get(size-2).getSingleHighest()) {
            ArrayList<Integer> winnerID = new ArrayList<>();
            winnerID.add(playersBestHands.get(size-1).getPlayerId());
            winnerID.add(playersBestHands.get(size-2).getPlayerId());
            return winnerID;
        }
        //w układach z dwiema składowymi: FULL_HOUSE, TWO_PAIRS, jeśli najwyższa zawarta karta jest taka sama, sprawdzamy następną najwyższą
        //drugą parę w przypadku TWO_PAIRS i parę po trójce w przypadku FULL_HOUSE
        //jeśli wartości są te same, obaj gracze wygrywają
        else if ((size > 1 && playersBestHands.get(size-1).getHandValue() == 2 || playersBestHands.get(size-1).getHandValue() == 6)
        && playersBestHands.get(size-1).getHandValue() == playersBestHands.get(size-2).getHandValue()
        && playersBestHands.get(size-1).getHighestIncluded() == playersBestHands.get(size-2).getHighestIncluded()) {
            if (playersBestHands.get(size-1).getSecondHighestIncluded() > playersBestHands.get(size-2).getSecondHighestIncluded()) {
                ArrayList<Integer> winnerID = new ArrayList<>();
                winnerID.add(playersBestHands.get(size-1).getPlayerId());
                return winnerID;
            }
            else if (playersBestHands.get(size-1).getSecondHighestIncluded() < playersBestHands.get(size-2).getSecondHighestIncluded()) {
                ArrayList<Integer> winnerID = new ArrayList<>();
                winnerID.add(playersBestHands.get(size-2).getPlayerId());
                return winnerID;
            }
            //w TWO_PAIRS: jeśli gracze mają te same pary, sprawdzimy jeszcze piątą kartę
            else if (playersBestHands.get(size-1).getSecondHighestIncluded() == playersBestHands.get(size-2).getSecondHighestIncluded()){
                if(playersBestHands.get(size-1).getSingleHighest() == playersBestHands.get(size-2).getSingleHighest()) {
                    ArrayList<Integer> winnerID = new ArrayList<>();
                    winnerID.add(playersBestHands.get(size - 1).getPlayerId());
                    winnerID.add(playersBestHands.get(size - 2).getPlayerId());
                    return winnerID;
                }
                else if (playersBestHands.get(size-1).getSingleHighest() > playersBestHands.get(size-2).getSingleHighest()) {
                    ArrayList<Integer> winnerID = new ArrayList<>();
                    winnerID.add(playersBestHands.get(size - 1).getPlayerId());
                    return winnerID;
                }
                else {
                    ArrayList<Integer> winnerID = new ArrayList<>();
                    winnerID.add(playersBestHands.get(size - 2).getPlayerId());
                    return winnerID;
                }
            }
        }
        ArrayList<Integer> winnerID = new ArrayList<>();
        winnerID.add(playersBestHands.get(size - 1).getPlayerId());
        return winnerID;
    }
}
