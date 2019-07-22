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
    private int totalPointsMade;
    private int totalPointsLost;
    private int totalSinksMade;
    private int totalSinksLost;
    private float sinksMadeToLostRatio;



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

    public int getTotalPointsLost() {
        return totalPointsLost;
    }

    public int getTotalPointsMade() {
        return totalPointsMade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSinksLost() {
        return totalSinksLost;
    }

    public int getTotalSinksMade() {
        return totalSinksMade;
    }

    public void setTotalPointsLost(int totalPointsLost) {
        this.totalPointsLost = totalPointsLost;
    }

    public void setTotalPointsMade(int totalPointsMade) {
        this.totalPointsMade = totalPointsMade;
    }

    public void setTotalSinksLost(int totalSinksLost) {
        this.totalSinksLost = totalSinksLost;
    }

    public void setTotalSinksMade(int totalSinksMade) {
        this.totalSinksMade = totalSinksMade;
    }

    public float getSinksMadeToLostRatio() {
        return sinksMadeToLostRatio;
    }

    public void setSinksMadeToLostRatio(float sinksMadeToLostRatio) {
        this.sinksMadeToLostRatio = sinksMadeToLostRatio;
    }
}
