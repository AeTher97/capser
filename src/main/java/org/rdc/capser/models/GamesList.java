package org.rdc.capser.models;

import java.util.List;

public class GamesList {

    List<Game> data;
    int numberOfGames;

    public void setData(List<Game> data) {
        this.data = data;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public List<Game> getData() {
        return data;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }
}
