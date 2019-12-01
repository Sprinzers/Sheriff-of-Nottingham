package com.tema1.player;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;
import com.tema1.game.CardsDeck;
import com.tema1.goods.IllegalGoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Player {
    protected static final int START_COINS = 80;
    protected static final int MIN_COINS_SHERIFF = 16;
    protected static final int MAX_CARDS_HAND = 10;
    protected static final int MAX_GOODS_BAG = 8;
    private static final int CLASS_NAME_POSITION = 17;

    protected ArrayList<Goods> bag;
    protected ArrayList<Goods> goodsInHand;
    protected HashMap<Goods, Integer> marketStand;
    protected int bribe;
    protected int totalCoins;
    protected Goods declaredGood;
    protected int profit;
    protected int score;
    protected int id;

    Player() {
        totalCoins = START_COINS;
        bribe = 0;
        profit = 0;
        score = 0;
        declaredGood = null;
        bag = new ArrayList<Goods>();
        goodsInHand = new ArrayList<Goods>();
        marketStand = new HashMap<Goods, Integer>();
    }

    public final int getId() {
        return id;
    }

    public final void setId(final int newId) {
        id = newId;
    }

    public final ArrayList<Goods> getBag() {
        return bag;
    }

    public final ArrayList<Goods> getGoodsInHand() {
        return goodsInHand;
    }

    public final int getTotalCoins() {
        return totalCoins;
    }

    public final void addToTotalCoins(final int newCoins) {
        totalCoins += newCoins;
    }

    public final void subFromTotalCoins(final int newCoins) {
        totalCoins -= newCoins;
    }

    public final boolean hasBribe() {
        return bribe > 0;
    }

    public final int getBribe() {
        totalCoins -= bribe;
        return bribe;
    }

    public final void giveBribe(final int newBribe) {
        bribe = newBribe;
    }

    public final void pushToBag(final Goods good) {
        bag.add(good);
    }

    public final void setDeclaredGood(final Goods goodDeclared) {
        declaredGood = goodDeclared;
    }

    private int getProfit() {
        return profit;
    }

    private void setProfit(final int newProfit) {
        profit += newProfit;
    }

    public final int getScore() {
        return score;
    }

    public final void setScore(final int newScore) {
        score += newScore;
    }

    public final HashMap<Goods, Integer> getMarketStand() {
        return marketStand;
    }

    public final void resetBag() {
        bag.clear();
    }

    /**
     *  Metoda este folosita pentru a intoarce numele jucatorului.
     */
    public String getPlayerName() {
        String name = this.getClass().getName().substring(CLASS_NAME_POSITION).toUpperCase();
        return name;
    }

    /**
     *  Metoda trage cate o carte din pachet si jucatorul o primeste in mana.
     */
    public final void drawCards() {
        for (int i = 0; i < MAX_CARDS_HAND; ++i) {
            Goods goodToAdd = CardsDeck.getInstance().getNextCard();
            goodsInHand.add(goodToAdd);
        }
    }

    /**
     *  Metoda arde cartile din mana ramase neutilizate.
     */
    public final void burnInHandCards() {
        if (goodsInHand.size() > 0) {
            goodsInHand.clear();
        }
    }

    /**
     *  Metoda adauga un bun pe taraba jucatorului curent, daca ea este goala,
     *  se adauga bunul cu numarul de aparitii 1.
     *  Daca bunul exista deja, numarul lui de aparitii este incrementat.
     * @param goodToAdd bunul ce urmeaza sa fie adaugat pe taraba
     */
    public final void addGoodToMarketStand(final Goods goodToAdd) {
        int defaultInsertNum = 1;
        if (marketStand.containsKey(goodToAdd)) {
            int numGoodsOfType = marketStand.get(goodToAdd);
            marketStand.put(goodToAdd, numGoodsOfType + 1);
        } else {
            marketStand.put(goodToAdd, defaultInsertNum);
        }
    }

    /**
     *  Metoda folosita pentru a pune toate bunurile pe taraba.
     */
    public final void populateMarketStand() {
        for (int i = 0; i < bag.size(); ++i) {
            addGoodToMarketStand(bag.get(i));
        }
    }

    /**
     *  Metoda este folosita de serif pentru a verifica un jucator si pentru a actualiza visteriile
     *  celor doi in functie de rezultatul verificarii.
     * @param playerToCheck jucatorul care este verificat de serif
     */
    public void checkPlayer(final Player playerToCheck) {
        int penalty = playerToCheck.computePenalty();
        this.addToTotalCoins(penalty);
        playerToCheck.subFromTotalCoins(penalty);
    }

    /**
     *  Metoda calculeaza penalty-ul dupa verificarea unui jucator.
     *  Daca seriful a controlat un comerciant cinstit, penalty-ul returnat este negativ,
     *  seriful pierde bani.
     *  Bunurile confiscate sunt adaugate inapoi in teancul de carti.
     * @return penalty pozitiv(comerciantul este mincinos) sau negativ(comerciantul este sincer)
     * */
    public int computePenalty() {
        int penalty = 0;
        boolean honest = true;
        ArrayList<Goods> confiscatedGoods = new ArrayList<Goods>();

        for (int i = 0; i < bag.size(); ++i) {
            if (bag.get(i).getType() == GoodsType.Illegal || bag.get(i) != declaredGood) {
                honest = false;
                penalty += bag.get(i).getPenalty();
                CardsDeck.getInstance().addCardBackToDeck(bag.get(i));
                confiscatedGoods.add(bag.get(i));
            }
        }
        for (int i = 0; i < confiscatedGoods.size(); ++i) {
            bag.remove(confiscatedGoods.get(i));
        }
        if (honest) {
            for (int i = 0; i < bag.size(); ++i) {
                penalty -= bag.get(i).getPenalty();
            }
        }
        confiscatedGoods.clear();
        return penalty;
    }

    /**
     *  Metoda este folosita pentru a acorda bonusul ilegal.
     */
    public void giveIllegalBonus() {
        HashMap<Goods, Integer> bonusGoods = new HashMap<Goods, Integer>();

        for (Map.Entry<Goods, Integer> entry : getMarketStand().entrySet()) {
            if (entry.getKey().getType() == GoodsType.Illegal) {
                IllegalGoods currGood = (IllegalGoods) entry.getKey();
                for (Map.Entry<Goods, Integer> entryBonus : currGood.getIllegalBonus().entrySet()) {
                    bonusGoods.put(entryBonus.getKey(),
                            bonusGoods.getOrDefault(entryBonus.getKey(), 0)
                                    + entryBonus.getValue() * entry.getValue());
                }
            }
        }

        for (Map.Entry<Goods, Integer> entry : bonusGoods.entrySet()) {
            for (int numGoods = 0; numGoods < entry.getValue(); ++numGoods) {
                addGoodToMarketStand(entry.getKey());
            }
        }

    }
    /**
     *  Metoda este folosita pentru a adauga bonusul pentru obiectele ilegala si pentru
     *  a calcula profitul total.
     */
    public void computeScore() {
        HashMap<Goods, Integer> bonusGoods = new HashMap<Goods, Integer>();

        for (Map.Entry<Goods, Integer> entry : getMarketStand().entrySet()) {
            setProfit(entry.getKey().getProfit() * entry.getValue());
        }
        setScore(getProfit() + getTotalCoins());
    }

    public abstract void playMerchant(int roundNumber);

    public abstract void playSheriff(ArrayList<Player> players);

    public abstract void playLegal();

    public abstract void playIllegal();
}
