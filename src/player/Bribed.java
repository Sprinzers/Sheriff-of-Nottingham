package com.tema1.player;

import com.tema1.game.GoodsComparator;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.Collections;

public class Bribed extends Basic {
    private static final int MIN_COINS_BRIBE = 5;
    private static final int DEFAULT_ID = 0;
    private static final int NO_BRIBE = 0;
    private static final int SMALL_BRIBE = 5;
    private static final int BIG_BRIBE = 10;
    private static final int FIRST_ID = 0;
    private static final int OFFSET = 1;
    private int leftNeighbourId;
    private int rightNeighbourId;

    /**
     *  Metoda este folosita pentru strategia de comerciant a jucatorului bribed.
     *  Toate bunurile ilegale sunt mai profitabile decat cele legale, de aceea ordonarea
     *  listei de bunuri dupa profit duce la ordinea dorita pentru a le introduce in sacul
     *  jucatorului bribed.
     */
    @Override
    public void playMerchant(final int roundNumber) {
        int illegalCount = 0;
        int legalCount = 0;
        int addedIllegalGoodsCount = 0;
        int addedGoods = 0;
        int budget = getTotalCoins();

        ArrayList<Goods> allGoods = getGoodsInHand();

        for (Goods currGood : allGoods) {
            if (currGood.getType() == GoodsType.Illegal) {
                ++illegalCount;
            } else if (currGood.getType() == GoodsType.Legal) {
                ++legalCount;
            }
        }

        Collections.sort(allGoods, new GoodsComparator());

        if (this.getTotalCoins() <= MIN_COINS_BRIBE || illegalCount == 0) {
            if (legalCount > 0) {
                super.playLegal();
            } else {
                super.playIllegal();
            }
            super.giveBribe(NO_BRIBE);
        } else {
            for (Goods currGood : allGoods) {
                if (addedGoods < MAX_GOODS_BAG) {
                    if (budget - currGood.getPenalty() > 0) {
                        pushToBag(currGood);
                        ++addedGoods;
                        budget -= currGood.getPenalty();
                        if (currGood.getType() == GoodsType.Illegal) {
                            ++addedIllegalGoodsCount;
                        }
                    }
                }
            }
            setDeclaredGood(GoodsFactory.getInstance().getGoodsById(DEFAULT_ID));
        }
        if (addedIllegalGoodsCount > 2) {
            giveBribe(BIG_BRIBE);
        } else if (addedIllegalGoodsCount > 0) {
            giveBribe(SMALL_BRIBE);
        }
    }

    /**
     *  Metoda este folosita pentru a determina vecinii jucatorului bribed, considerand ca
     *  jocul se desfasoara in cerc.
     * @param players lista cu toti jucatorii
     */
    private void findNeighbours(final ArrayList<Player> players) {
        if (getId() == FIRST_ID) {
            leftNeighbourId = players.get(players.size() - OFFSET).getId();
            rightNeighbourId = getId() + OFFSET;
        } else if (getId() == (players.size() - OFFSET)) {
            leftNeighbourId = getId() - OFFSET;
            rightNeighbourId = FIRST_ID;
        } else {
            leftNeighbourId = getId() - OFFSET;
            rightNeighbourId = getId() + OFFSET;
        }
    }

    /**
     *  Metoda este folosita pentru strategia de serif a jucatorului de tip bribed.
     *  Acesta isi verifica doar vecinii, dar accepta mita de la ceilalti jucatori.
     * @param players lista cu toti jucatorii
     */
    @Override
    public void playSheriff(final ArrayList<Player> players) {
        findNeighbours(players);

        if (getTotalCoins() < MIN_COINS_SHERIFF) {
            for (Player currPlayer : players) {
                if (!currPlayer.equals(this) && rightNeighbourId != currPlayer.getId()
                        && leftNeighbourId != currPlayer.getId()) {
                        if (currPlayer.hasBribe()) {
                            addToTotalCoins(currPlayer.getBribe());
                        }
                    }
                currPlayer.populateMarketStand();
            }
        } else {
            checkPlayer(players.get(leftNeighbourId));
            players.get(leftNeighbourId).populateMarketStand();
            if (rightNeighbourId != leftNeighbourId) {
                checkPlayer(players.get(rightNeighbourId));
                players.get(rightNeighbourId).populateMarketStand();
            }
            for (Player currPlayer : players) {
                if (!currPlayer.equals(this)) {
                    if (currPlayer.getId() != leftNeighbourId
                            && currPlayer.getId() != rightNeighbourId) {
                        if (currPlayer.hasBribe()) {
                            addToTotalCoins(currPlayer.getBribe());
                        }
                        currPlayer.populateMarketStand();
                    }
                }
            }
        }
    }
}
