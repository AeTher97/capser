package org.rdc.capser.models;

public class Player implements Comparable<Player> {

    private int id;
    private String name;
    private float points;
    private float averageRebottles;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private float winLossRatio;



    public Player(int id, String name, float startingPoints) {
        this.id = id;
        this.name = name;
        this.points = startingPoints;
    }

    public void setAverageRebottles(float averageRebottles) {
        this.averageRebottles = averageRebottles;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setWinLossRatio(float winLossRatio) {
        this.winLossRatio = winLossRatio;
    }

    public float getAverageRebottles() {
        return averageRebottles;
    }

    public float getWinLossRatio() {
        return winLossRatio;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    @Override
    public int compareTo(Player u) {
        if(u.getPoints() > this.getPoints())
        {
            return 1;
        }else{
            return -1;
        }
    }
}
