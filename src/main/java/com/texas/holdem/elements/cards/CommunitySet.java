package com.texas.holdem.elements.cards;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommunitySet {
    ArrayList<Card> communitySet;

    public void clear(){
        communitySet = new ArrayList<>();
    }

}
