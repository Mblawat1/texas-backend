package com.texas.elements;

public class Card {
    int number;
    String color;

    public Card() {}

    public Card(int number, String color) {
        this.number = number;
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int compareCards(Card first, Card second) {
        int elderCard = 0;
        if (first.getNumber() > second.getNumber()) {
            elderCard = 1;
        } else if (second.getNumber() > first.getNumber()) {
            elderCard = 2;
        }
        return elderCard;
    }

}
