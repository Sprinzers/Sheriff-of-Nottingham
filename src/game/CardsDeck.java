package com.tema1.game;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.ArrayList;
import java.util.List;

public final class CardsDeck {
    private static final int FIRST_CARD = 0;

    private static CardsDeck instance = null;
    private final ArrayList<Goods> deck;

    private CardsDeck() {
        deck = new ArrayList<Goods>();
    }

    public ArrayList<Goods> getDeck() {
        return deck;
    }

    public void setDeck(final Goods good) {
        deck.add(good);
    }

    public static void createInstance(final List<Integer> cards) {
        instance = new CardsDeck();
        for (int i = 0; i < cards.size(); ++i) {
            int id = cards.get(i);
            Goods good = GoodsFactory.getInstance().getGoodsById(id);
            instance.setDeck(good);
        }
    }

    public static CardsDeck getInstance() {
        return instance;
    }

    public Goods getNextCard() {
        Goods card = deck.get(getFirstCard());
        deck.remove(getFirstCard());
        return card;
    }

    public int getSize() {
        return deck.size();
    }

    public static int getFirstCard() {
        return FIRST_CARD;
    }

    public void addCardBackToDeck(final Goods card) {
        deck.add(card);
    }
}
