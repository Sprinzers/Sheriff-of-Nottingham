package com.tema1.player;

public class PlayerFactory {
    public final Player getPlayer(final String playerType) {
        switch (playerType) {
            case "basic":
                return new Basic();

            case "bribed":
                return new Bribed();

            case "greedy":
                return new Greedy();

            default:
                return null;
        }
    }
}
