package com.tema1.game;

import com.tema1.player.Player;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {
    /**
     *  Metoda este folosita pentru a compara doi jucarori dupa scor si apoi dupa id.
     * @param player1 jucatorul de comparat
     * @param player2 jucatorul cu care este comparat
     * @return jucatorul cu scorul mai mare, iar in caz de egalitate cel cu id-ul mai mic
     */
    @Override
    public int compare(final Player player1, final Player player2) {
        Integer score1 = player1.getScore();
        Integer score2 = player2.getScore();
        Integer compareScore = score2.compareTo(score1);
        Integer id1 = player1.getId();
        Integer id2 = player2.getId();
        Integer compareId = id1.compareTo(id2);

        if (compareScore == 0) {
            return compareId;
        } else {
            return compareScore;
        }
    }
}
