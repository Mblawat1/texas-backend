package com.texas.holdem.elements;

import java.util.ArrayList;

public class CommunitySet {
    ArrayList<Card> communitySet;

    public CommunitySet() {}

    public CommunitySet(ArrayList<Card> communitySet) {
        this.communitySet = communitySet;
    }

    public ArrayList<Card> getCommunitySet() {
        return communitySet;
    }

    public void setCommunitySet(ArrayList<Card> communitySet) {
        this.communitySet = communitySet;
    }
}
