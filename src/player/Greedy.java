package com.tema1.player;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;
import java.util.ArrayList;

public class Greedy extends Basic {

    /**
     *  Metoda este folosita pentru strategia de comerciant a jucatorului greedy.
     *  Cand runda curenta este para, se apeleaza metoda evenRoundStrategy() pentru a adauga
     *  un bun ilegal in sac.
     * @param roundNumber numarul rundei de joc
     */
    @Override
    public void playMerchant(final int roundNumber) {
        boolean hasLegal = false;
        for (int i = 0; i < getGoodsInHand().size(); ++i) {
            if (getGoodsInHand().get(i).getType() == GoodsType.Legal) {
                hasLegal = true;
            }
        }
        if (hasLegal) {
            playLegal();
        } else {
            playIllegal();
        }
        boolean hasIllegal = false;
        for (int i = 0; i < getGoodsInHand().size(); ++i) {
            if (getGoodsInHand().get(i).getType() == GoodsType.Illegal) {
                hasIllegal = true;
            }
        }
        if (isEven(roundNumber) && getBag().size() < MAX_GOODS_BAG && hasIllegal) {
            evenRoundStrategy();
        }
    }

    /**
     *  Metoda este folosita pentru a verifica toti jucatorii care nu dau mita.
     * @param players vectorul cu jucatorii
     */
    @Override
    public void playSheriff(final ArrayList<Player> players) {
        for (Player currPlayer : players) {
            if (getTotalCoins() > MIN_COINS_SHERIFF) {
                if (!currPlayer.equals(this)) {
                    if (!currPlayer.hasBribe()) {
                        checkPlayer(currPlayer);
                    } else {
                        addToTotalCoins(currPlayer.getBribe());
                    }
                    currPlayer.populateMarketStand();
                }
            }
        }
    }

    /**
     *  Metoda este folosita in runda para pentru a adauga un bun ilegal in sac.
     */
    private void evenRoundStrategy() {
        Goods bestIllegalGood = null;
        int maxProfit = 0;
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
    }

    /**
     *  Metoda este folosita pentru a determina daca runda curenta este para sau nu.
     * @param roundNumber numarul rundei
     * @return true daca runda este para, false daca este impara
     */
    private boolean isEven(final int roundNumber) {
        if (roundNumber % 2 == 0) {
            return true;
        }
        return false;
    }

}
