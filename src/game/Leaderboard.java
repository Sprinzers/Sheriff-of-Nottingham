package com.tema1.game;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.LegalGoods;
import com.tema1.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Leaderboard {
    private static final int NUM_LEGAL_GOODS = 10;
    private static final int MAX_ID_LEGAL = 9;

    public Leaderboard() {
    }

    /**
     *  Metoda este folosita pentru a acorda King si Queen bonus.
     *  Aceste bonusuri sunt adaugate la scorul jucatorilor.
     * @param players lista cu toti jucatorii
     */
    public final void giveBonus(final ArrayList<Player> players) {
        for (int id = 0; id <= MAX_ID_LEGAL; ++id) {
            int kingId = Integer.MAX_VALUE;
            int kingValue = 0;
            int queenId = Integer.MAX_VALUE;
            int queenValue = 0;
            Goods searchGood = GoodsFactory.getInstance().getGoodsById(id);

            for (Player currPlayer : players) {
                for (Map.Entry<Goods, Integer> entry : currPlayer.getMarketStand().entrySet()) {
                    if (entry.getKey().equals(searchGood)) {
                        if (entry.getValue() > kingValue) {
                            queenValue = kingValue;
                            queenId = kingId;
                            kingValue = entry.getValue();
                            kingId = currPlayer.getId();
                        }
                        if (entry.getValue() < kingValue && entry.getValue() > queenValue) {
                            queenValue = entry.getValue();
                            queenId = currPlayer.getId();
                        }
                        if (entry.getValue() == kingValue) {
                            if (entry.getValue() == queenValue) {
                                continue;
                            }
                            if (currPlayer.getId() > kingId) {
                                queenValue = entry.getValue();
                                queenId = currPlayer.getId();
                            }
                        }
                    }
                }
            }
            if (kingValue > 0) {
                Goods kingGood = GoodsFactory.getInstance().getGoodsById(id);
                LegalGoods kingBonus = (LegalGoods) kingGood;
                players.get(kingId).setScore(kingBonus.getKingBonus());
            }

            if (queenValue > 0 && queenId != kingId) {
                Goods queenGood = GoodsFactory.getInstance().getGoodsById(id);
                LegalGoods queenBonus = (LegalGoods) queenGood;
                players.get(queenId).setScore(queenBonus.getQueenBonus());
            }
        }
    }

    /**
     *  Metoda este foloista pentru a ordona clasamentul final.
     * @param players lista cu toti jucatorii
     */
    public final void sortLeaderboard(final ArrayList<Player> players) {
        Collections.sort(players, new PlayerComparator());

        for (Player currPlayer : players) {
            System.out.println(currPlayer.getId() + " " + currPlayer.getPlayerName()
                    + " " + currPlayer.getScore());
        }
    }
}
