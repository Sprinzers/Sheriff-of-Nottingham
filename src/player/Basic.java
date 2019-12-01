package com.tema1.player;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Basic extends Player {

    /**
     *  Metoda reprezinta strategia basic a comerciantului.
     *  Verifica intai cartile din mana si decide astfel daca poate sa joace legal sau nu.
     */
    @Override
    public void playMerchant(final int roundNumber) {
        boolean haslegal = false;
        for (int i = 0; i < getGoodsInHand().size(); ++i) {
            if (getGoodsInHand().get(i).getType() == GoodsType.Legal) {
                haslegal = true;
            }
        }
        if (haslegal) {
            playLegal();
        } else {
            playIllegal();
        }
    }

    /**
     *  Metoda reprezinta strategie basic a sheriff-ului.
     *  Dupa verificare, comercianti isi pun bunurile pe taraba, acolo unde este cazul.
     * @param players folosit pentru ca seriful sa-i verifice pe ceilalti jucatori.
     */
    @Override
    public void playSheriff(final ArrayList<Player> players) {
        for (int i = 0; i < players.size(); ++i) {
            if (getTotalCoins() >= MIN_COINS_SHERIFF) {
                if (!players.get(i).equals(this)) {
                    checkPlayer(players.get(i));
                }
            }
            players.get(i).populateMarketStand();
        }
    }

    /**
     *  Metoda folosita de jucatorul basic pentru a-si crea sacul in mod onest.
     *  Este determinat cel mai frecvent bun legal cu ajutorul unui hashmap.
     */
    @Override
    public void playLegal() {
        final int defaultInsert = 1;
        HashMap<Goods, Integer> frequency = new HashMap<Goods, Integer>();
        for (int i = 0; i < getGoodsInHand().size(); ++i) {
            if (getGoodsInHand().get(i).getType() == GoodsType.Legal) {
                if (frequency.containsKey(getGoodsInHand().get(i))) {
                    frequency.put(getGoodsInHand().get(i),
                            frequency.get(getGoodsInHand().get(i)) + 1);
                } else {
                    frequency.put(getGoodsInHand().get(i), defaultInsert);
                }
            }
        }
        int maxValue = 0;
        int maxProfit = 0;
        int maxId = -1;
        for (Map.Entry<Goods, Integer> iterator : frequency.entrySet()) {
            if (iterator.getValue() == maxValue) {
                if (iterator.getKey().getProfit() == maxProfit) {
                    if (iterator.getKey().getId() > maxId) {
                        maxId = iterator.getKey().getId();
                    }
                } else if (iterator.getKey().getProfit() > maxProfit) {
                    maxId = iterator.getKey().getId();
                    maxProfit = iterator.getKey().getProfit();
                }
            } else if (iterator.getValue() > maxValue) {
                maxId = iterator.getKey().getId();
                maxProfit = iterator.getKey().getProfit();
                maxValue = iterator.getValue();
            }
        }
        if (maxValue > MAX_GOODS_BAG) {
            maxValue = MAX_GOODS_BAG;
        }
        Goods goodToAdd = GoodsFactory.getInstance().getGoodsById(maxId);
        for (int i = 0; i < maxValue; ++i) {
            pushToBag(goodToAdd);
        }
        setDeclaredGood(goodToAdd);
    }

    /**
     *  Metoda folosita de jucatorul basic cand nu poate sa joace onest, adica
     *  are doar carti ilegale.
     */
    @Override
    public void playIllegal() {
        Goods bestIllegalGood = null;
        int maxProfit = 0;
        int declaredId = 0;
        int index = 0;

        for (int i = 0; i < getGoodsInHand().size(); ++i) {
            if (getGoodsInHand().get(i).getProfit() > maxProfit) {
                maxProfit = getGoodsInHand().get(i).getProfit();
                bestIllegalGood = getGoodsInHand().get(i);
                index = i;
            }
        }
        pushToBag(bestIllegalGood);
        getGoodsInHand().remove(index);
        setDeclaredGood(GoodsFactory.getInstance().getGoodsById(declaredId));
    }
}
