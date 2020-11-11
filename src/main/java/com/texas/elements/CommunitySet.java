package com.texas.elements;

import com.texas.elements.Card;

public class CommunitySet {
    Card communityCard1;
    Card communityCard2;
    Card communityCard3;
    Card communityCard4;
    Card communityCard5;

    public CommunitySet() {}

    public CommunitySet(Card communityCard1, Card communityCard2, Card communityCard3, Card communityCard4, Card communityCard5) {
        this.communityCard1 = communityCard1;
        this.communityCard2 = communityCard2;
        this.communityCard3 = communityCard3;
        this.communityCard4 = communityCard4;
        this.communityCard5 = communityCard5;
    }

    public Card getCommunityCard1() {
        return communityCard1;
    }

    public void setCommunityCard1(Card communityCard1) {
        this.communityCard1 = communityCard1;
    }

    public Card getCommunityCard2() {
        return communityCard2;
    }

    public void setCommunityCard2(Card communityCard2) {
        this.communityCard2 = communityCard2;
    }

    public Card getCommunityCard3() {
        return communityCard3;
    }

    public void setCommunityCard3(Card communityCard3) {
        this.communityCard3 = communityCard3;
    }

    public Card getCommunityCard4() {
        return communityCard4;
    }

    public void setCommunityCard4(Card communityCard4) {
        this.communityCard4 = communityCard4;
    }

    public Card getCommunityCard5() {
        return communityCard5;
    }

    public void setCommunityCard5(Card communityCard5) {
        this.communityCard5 = communityCard5;
    }
}
