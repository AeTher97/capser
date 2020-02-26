package org.rdc.capser.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "players")
@Data
@Entity
public class Player implements Comparable<Player> {

    @Id
    @GeneratedValue
    private Long id;
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

    public Player() {
    }

    public Player(String name, float startingPoints) {
        this.name = name;
        this.points = startingPoints;
    }


    @Override
    public int compareTo(Player u) {
        if (u.getPoints() > this.getPoints()) {
            return 1;
        } else {
            return -1;
        }
    }


}
