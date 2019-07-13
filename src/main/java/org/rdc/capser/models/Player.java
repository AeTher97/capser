package org.rdc.capser.models;

public class Player implements Comparable<Player> {

    private int id;
    private String name;
    private float points;

    public Player(int id, String name, float startingPoints) {
        this.id = id;
        this.name = name;
        this.points = startingPoints;
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
