package com.texas.holdem.elements;

import lombok.*;

import java.util.ArrayList;

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
