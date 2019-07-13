package org.rdc.capser.services;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.tomcat.jni.File;
import org.rdc.capser.models.Game;
import org.rdc.capser.models.GamesList;
import org.rdc.capser.models.Player;
import org.rdc.capser.models.PlayerList;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DataService {

    private final String PLAYERS_LIST_PATH = "D:/ServerData/players.txt";
    private final String GAMES_LIST_PATH = "D:/ServerData/games.txt";

    public PlayerList getPlayersList() throws FileNotFoundException {

        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(PLAYERS_LIST_PATH));
            if (gson.fromJson(reader, PlayerList.class) == null) {
                PlayerList playerList = new PlayerList();
                playerList.setData(new ArrayList<Player>());
                playerList.setNumberOfPlayers(0);
                return playerList;
            }

            reader.close();
            JsonReader reader2 = new JsonReader(new FileReader(PLAYERS_LIST_PATH));

            PlayerList result = gson.fromJson(reader2, PlayerList.class);
            reader2.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void savePlayersList(PlayerList playerList) throws FileNotFoundException {

        PrintWriter out = new PrintWriter(PLAYERS_LIST_PATH);
        Gson gson = new Gson();
        out.print(gson.toJson(playerList));
        out.close();
    }

    public GamesList getGamesList() throws FileNotFoundException {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(GAMES_LIST_PATH));
            if (gson.fromJson(reader, GamesList.class) == null) {
                GamesList gamesList = new GamesList();
                gamesList.setData(new ArrayList<Game>());
                gamesList.setNumberOfGames(0);
                return gamesList;
            }
            reader.close();
            JsonReader reader2 = new JsonReader(new FileReader(GAMES_LIST_PATH));

            GamesList result = gson.fromJson(reader2, GamesList.class);
            reader2.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }

    public void saveGamesList(GamesList gamesList) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(GAMES_LIST_PATH);
        Gson gson = new Gson();
        out.print(gson.toJson(gamesList));
        out.close();
    }

    public String getPlayerName(int id) {
        try {
            PlayerList list = getPlayersList();
            List<Player> players = list.getData();

            for (Player player : players) {
                if (player.getId() == id) {
                    return player.getName();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Player findPlayerById(int id) throws FileNotFoundException {
        for (Player player : getPlayersList().getData()) {
            if (player.getId() == id) {
                return player;
            }

        }
        return null;
    }
}
