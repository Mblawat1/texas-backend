package com.texas.holdem.logic;

import com.texas.holdem.elements.cards.Card;
import com.texas.holdem.elements.cards.HoleSet;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HandAnalyzer {

    HandUtilities utility = new HandUtilities();

    public HandAnalyzer() { }

    //podawanie nazwy znalezionego w ręce układu na podstawie jego numeru
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

    //w danej ręce złożonej z pięciu kart szukany i zwracany jest najlepszy znaleziony układ
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
        return new HandOutcome.Builder(0).build();
    }

    //tylko w wypadku, gdy nie znaleziono żadnego sensownego układu, zwracany jest set 5 najwyższych kart spośród posortowanych rosnąco 7
    public HandOutcome makeSetOfHighCards(ArrayList<Card> set) {
        ArrayList<Integer> sortedSet = utility.sortSet(set);
        HandOutcome outcome = new HandOutcome.Builder(0)
                .withHighestIncluded(sortedSet.get(6))
                .withSecondHighestIncluded(sortedSet.get(5))
                .withFirstHighestExcluded(sortedSet.get(4))
                .withSecondHighestExcluded(sortedSet.get(3))
                .withThirdHighestExcluded(sortedSet.get(2))
                .build();
        return outcome;
    }

    //tworzenie pełnego setu 7 kart z 5 kart community i dwóch kart hole gracza
    public ArrayList<Card> makeSet(HoleSet holeSet, List<Card> communitySet) {
        ArrayList<Card> totalSet = new ArrayList<>();
        totalSet.add(holeSet.getHoleCard1());
        totalSet.add(holeSet.getHoleCard2());
        totalSet.addAll(communitySet);
        return totalSet;
    }

    //budowanie wszystkich możliwych permutacji 5 kart (bez powtórzeń) z 7, które stanowi hole set gracza i community set
    //dopuszczony przypadek użycia jedynie 5 kart ze stołu jako ręki gracza
    public List<ArrayList<Card>> makeFiveHandSets(ArrayList<Card> set) {
        List<ArrayList<Card>> sets = new ArrayList<>();
        String[] allPerms = {"01234","01235","01236","01245","01246","01256","01345","01346","01356","01456","02345","02346","02356","02456","03456","12345","12346","12356","12456","13456","23456"};
        for (int i = 0; i < 21; i++) {
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

    //wybieranie najlepszej ręki gracza spośród wszystkich permutacji - lecimy z każdą ręką po kolei
    //w razie znalezienia lepszej ręki nadpisujemy dane final_, które na końcu ustawiane są jako własności finalnej ręki
    public HandOutcome getPlayersWinningHand(int id, HoleSet holeSet, ArrayList<Card> communitySet) {
        List<ArrayList<Card>> playerSets = makeFiveHandSets(makeSet(holeSet, communitySet));
        //numery z wszystkich 7 kart będą służyć do znajdowania najwyższych kart obok
        // FOUR_OF_A_KIND, THREE_OF_A_KIND, TWO_PAIR i PAIR oraz HIGH_CARD
        List<Integer> ranks = utility.sortSet(makeSet(holeSet, communitySet));
        int finalHandValue = 0;
        int finalHighestIncluded = 0;
        int finalSecondIncluded = 0;
        int finalFirstHighestExcluded = 0;
        int finalSecondHighestExcluded = 0;
        int finalThirdHighestExcluded = 0;

        ArrayList<Card> finalWinningHand = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            //jeśli znaleziony zostaje ROYAL_FLUSH, nie szukamy dalej
            HandOutcome outcome = getHandOutcome(playerSets.get(i));
            if (outcome.getHandValue() == 9) {
                outcome.setBestSet(playerSets.get(i));
                outcome.setPlayerId(id);
                return outcome;
            }
            //porównanie na podstawie samego układu
            if (outcome.getHandValue() > finalHandValue) {
                finalHandValue = outcome.getHandValue();
                finalHighestIncluded = outcome.getHighestIncluded();
                finalSecondIncluded = outcome.getSecondHighestIncluded();
                finalFirstHighestExcluded = outcome.getFirstHighestExcluded();
                finalSecondHighestExcluded = outcome.getSecondHighestExcluded();
                finalThirdHighestExcluded = outcome.getThirdHighestExcluded();
                finalWinningHand = playerSets.get(i);
            }
            //jeśli wartość układów jest równa, porównujemy najwyższe karty, na których się one opierają
            else if (outcome.getHandValue() == finalHandValue && outcome.getHighestIncluded() > finalHighestIncluded) {
                finalHighestIncluded = outcome.getHighestIncluded();
                finalSecondIncluded = outcome.getSecondHighestIncluded();
                finalFirstHighestExcluded = outcome.getFirstHighestExcluded();
                finalSecondHighestExcluded = outcome.getSecondHighestExcluded();
                finalWinningHand = playerSets.get(i);
            }
            //jeśli i one są równe, porównujemy kickery
            else if (outcome.getHandValue() == finalHandValue && outcome.getHighestIncluded() == finalHighestIncluded && outcome.getFirstHighestExcluded() > finalFirstHighestExcluded) {
                finalFirstHighestExcluded = outcome.getFirstHighestExcluded();
                finalSecondIncluded = outcome.getSecondHighestIncluded();
                finalSecondHighestExcluded = outcome.getSecondHighestExcluded();
                finalThirdHighestExcluded = outcome.getThirdHighestExcluded();
                finalWinningHand = playerSets.get(i);
            }
        }
        //dorzucanie najlepszych kickerów, ewentualnie drugich kart, trzecich kart do układów niezajmujących 5 kart
        if (finalHandValue == 7) {
            for (int i = 6; i > 0; i--) {
                if (ranks.get(i) != finalHighestIncluded) {
                    finalFirstHighestExcluded = ranks.get(i);
                    break;
                }
            }
        }
        else if (finalHandValue == 3) {
            for (int i = 6; i > 0; i--) {
                if (ranks.get(i) != finalHighestIncluded) {
                    finalFirstHighestExcluded = ranks.get(i);
                    System.out.println(ranks.get(i));
                    ranks.remove(ranks.get(i));
                    break;
                }
            }
            for (int i = 5; i > 0; i--) {
                if (ranks.get(i) != finalHighestIncluded) {
                    finalSecondHighestExcluded = ranks.get(i);
                    break;
                }
            }
        }
        else if (finalHandValue == 2) {
            for (int i = 6; i >= 0; i--) {
                if (ranks.get(i) != finalHighestIncluded && ranks.get(i) != finalSecondIncluded) {
                    finalFirstHighestExcluded = ranks.get(i);
                    break;
                }
            }
        }
        else if (finalHandValue == 1) {
            for (int i = 6; i > 0; i--) {
                if (ranks.get(i) != finalHighestIncluded) {
                    finalFirstHighestExcluded = ranks.get(i);
                    System.out.println(ranks.get(i));
                    ranks.remove(ranks.get(i));
                    break;
                }
            }
            for (int i = 5; i > 0; i--) {
                if (ranks.get(i) != finalHighestIncluded) {
                    finalSecondHighestExcluded = ranks.get(i);
                    break;
                }
            }
            for (int i = 4; i > 0; i--) {
                if (ranks.get(i) != finalHighestIncluded) {
                    finalThirdHighestExcluded = ranks.get(i);
                    break;
                }
            }
        }
        //zwrócenie układu złożonego jedynie z najwyższych kart
        else if (finalHandValue == 0) {
            HandOutcome outcome = makeSetOfHighCards(makeSet(holeSet, communitySet));
            outcome.setPlayerId(id);
            return outcome;
        }

        //zwrócenie finalnego układu
        return new HandOutcome.Builder(finalHandValue)
                .ofPlayer(id)
                .withHighestIncluded(finalHighestIncluded)
                .withSecondHighestIncluded(finalSecondIncluded)
                .withBestSet(finalWinningHand)
                .withFirstHighestExcluded(finalFirstHighestExcluded)
                .withSecondHighestExcluded(finalSecondHighestExcluded)
                .withThirdHighestExcluded(finalThirdHighestExcluded)
                .build();
    }

    //wybranie zwycięzcy na podstawie posortowanej listy najlepszych rąk graczy
    public ArrayList<Integer> getWinner(List<HandOutcome> playersBestHands) {
        Collections.sort(playersBestHands);
        playersBestHands.forEach(hand -> System.out.println(hand.getPlayerId() + " " +  hand.getHighestIncluded()));
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
        HandOutcome outcome1 = playersBestHands.get(size-1);
        HandOutcome outcome2 = playersBestHands.get(size-2);
        if (outcome1.getHandValue() == outcome2.getHandValue() && outcome1.getHighestIncluded() == outcome2.getHighestIncluded()
                && outcome1.getSecondHighestIncluded() == outcome2.getSecondHighestIncluded() && outcome1.getFirstHighestExcluded() == outcome2.getFirstHighestExcluded()
                && outcome1.getSecondHighestExcluded() == outcome2.getSecondHighestExcluded() && outcome1.getThirdHighestExcluded() == outcome2.getThirdHighestExcluded()) {
            ArrayList<Integer> winnerID = new ArrayList<>();
            winnerID.add(playersBestHands.get(size - 1).getPlayerId());
            winnerID.add(playersBestHands.get(size-2).getPlayerId());
            return winnerID;
        }
        ArrayList<Integer> winnerID = new ArrayList<>();
        winnerID.add(playersBestHands.get(size - 1).getPlayerId());
        return winnerID;
    }
}
