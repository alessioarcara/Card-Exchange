package com.aadm.cardexchange.client.utils;

import com.aadm.cardexchange.shared.models.Card;

import java.util.List;

public class CardListCache {
    private static List<Card> cardList;

    public static void cacheCardList(List<Card> newCardList) {
        cardList = newCardList;
    }

    public static List<Card> getCachedCardList() {
        return cardList;
    }
}
