package org.rdc.capser.utilities;

import org.rdc.capser.models.Game;

import java.time.Instant;
import java.util.List;

public class utilMethods {

    public static double determineTimeWithoutAGame(List<Game> games) {
        double shortestTime;
        Instant now = Instant.now();
        if(games.size()==0)
        {
            return 999999999;
        }
        shortestTime = now.toEpochMilli() - games.get(0).getGameDate().toInstant().toEpochMilli();

        for (Game game : games) {
            if (now.toEpochMilli() - game.getGameDate().toInstant().toEpochMilli() < shortestTime){
                shortestTime = now.toEpochMilli() - game.getGameDate().toInstant().toEpochMilli();
            }
        }

        return shortestTime;

    }
}
