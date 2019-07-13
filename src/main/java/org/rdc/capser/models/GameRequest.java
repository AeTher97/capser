package org.rdc.capser.models;

public class GameRequest {

    private int playerId;
    private int opponentId;
    private GameType gameType;
    private int playerScore;
    private int opponentScore;
    private int playerSinks;
    private int opponentSinks;

    public void setPlayerSinks(int playerSinks) {
        this.playerSinks = playerSinks;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setOpponentSinks(int opponentSinks) {
        this.opponentSinks = opponentSinks;
    }

    public int getPlayerSinks() {
        return playerSinks;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getOpponentSinks() {
        return opponentSinks;
    }
}
