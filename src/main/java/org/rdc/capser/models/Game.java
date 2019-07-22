package org.rdc.capser.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game  implements Comparable<Game>{

    public Game(int playerId,
                int opponentId,
                GameType gameType,
                int playerScore,
                int opponentScore,
                int playerSinks,
                int opponentSinks,
                int winner) {
        this.playerId = playerId;
        this.opponentId = opponentId;
        this.gameType = gameType;
        this.playerScore = playerScore;
        this.opponentScore = opponentScore;
        this.opponentSinks = opponentSinks;
        this.playerSinks = playerSinks;
        this.winner = winner;
        this.gameDate = new Date();
        this.playerRebottles = getOpponentSinks() - opponentScore;
        this.opponentRebottles = getPlayerSinks() - playerScore;
        if (this.playerRebottles < 0) {
            this.playerRebottles = 0;
        }
        if (this.opponentRebottles < 0) {
            this.opponentRebottles = 0;
        }
    }

    private Date gameDate;
    private int playerId;
    private int opponentId;
    private GameType gameType;
    private int playerScore;
    private int opponentScore;
    private int playerSinks;
    private int opponentSinks;
    private int winner;
    private int playerRebottles;
    private int opponentRebottles;


    public Date getGameDate() {
        return gameDate;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public int getPlayerId() {
        return playerId;
    }


    public int getOpponentRebottles() {
        return opponentRebottles;
    }

    public int getPlayerRebottles() {
        return playerRebottles;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getOpponentSinks() {
        return opponentSinks;
    }

    public int getPlayerSinks() {
        return playerSinks;
    }

    public int getWinner() {
        return winner;
    }

    public void setOpponentSinks(int opponentSinks) {
        this.opponentSinks = opponentSinks;
    }

    public void setPlayerSinks(int playerSinks) {
        this.playerSinks = playerSinks;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    @Override
    public int compareTo(Game u) {
        if(u.getGameDate().toInstant().toEpochMilli() > this.getGameDate().toInstant().toEpochMilli())
        {
            return 1;
        }else{
            return -1;
        }
    }
}
