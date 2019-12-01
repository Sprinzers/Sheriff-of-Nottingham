package com.tema1.main;

import com.tema1.game.CardsDeck;
import com.tema1.game.Leaderboard;
import com.tema1.player.Player;
import com.tema1.player.PlayerFactory;

import java.util.ArrayList;
import java.util.List;

public final class Main {
    private Main() {
         /*
         just to trick checkstyle
          */
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();

        List<Integer> cards = gameInput.getAssetIds();
        PlayerFactory playerFactory = new PlayerFactory();
        ArrayList<Player> players = new ArrayList<Player>();
        for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
            Player player = playerFactory.getPlayer(gameInput.getPlayerNames().get(i));
            player.setId(i);
            players.add(player);
        }

        CardsDeck.createInstance(cards);
        CardsDeck deck = CardsDeck.getInstance();

        for (int currRound = 0; currRound < gameInput.getRounds(); ++currRound) {
            for (int currSubround = 0; currSubround < players.size(); ++currSubround) {
                for (Player currPlayer : players) {
                    currPlayer.resetBag();
                    if (currPlayer.getId() != currSubround) {
                        currPlayer.burnInHandCards();
                        currPlayer.drawCards();
                        currPlayer.playMerchant(currRound + 1);
                    }
                }
                players.get(currSubround).playSheriff(players);
            }
        }
        Leaderboard leaderboard = new Leaderboard();

        for (Player currPlayer : players) {
            currPlayer.giveIllegalBonus();
            currPlayer.computeScore();
        }

        leaderboard.giveBonus(players);
        leaderboard.sortLeaderboard(players);
    }
}
