package com.tema1.game;

import com.tema1.goods.Goods;

import java.util.Comparator;

public class GoodsComparator implements Comparator<Goods> {

    /**
     *  Metoda este folosita pentru compararea a doua bunuri in vederea sortarii listelor
     *  de bunuri.
     * @param good1 bunul de comparat
     * @param good2 bunul cu care este comparat
     * @return bunul mai profitabil, iar in caz de egalitate cel cu id-ul mai mare
     */
    @Override
    public int compare(final Goods good1, final Goods good2) {
        Integer profit1 = good1.getProfit();
        Integer profit2 = good2.getProfit();
        Integer compareProfit =  profit2.compareTo(profit1);
        Integer id1 = good1.getId();
        Integer id2 = good2.getId();
        Integer compareId = id2.compareTo(id1);
        if (compareProfit == 0) {
            return compareId;
        } else {
            return compareProfit;
        }
    }
}
