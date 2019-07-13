package org.rdc.capser.utilities;

import org.rdc.capser.models.Player;

import java.util.List;

public class EloRating {

    static float Probability(float rating1,
                             float rating2) {
        return 1.0f * 1.0f / (1 + 1.0f *
                (float) (Math.pow(10, 1.0f *
                        (rating1 - rating2) / 400)));
    }

    // Function to calculate Elo rating
    // K is a constant.
    // d determines whether Player A wins
    // or Player B.
    public static List<Player> calculate(List<Player> players,
                                  int K, boolean d) {

        if (players.size() < 2) {
            System.out.println("Not enough players in the list");
            return null;
        }

        Player player1 = players.get(0);
        Player player2 = players.get(1);

        float player2Probability = Probability(player1.getPoints(), player2.getPoints());
        float player1Probability = Probability(player2.getPoints(), player1.getPoints());

        if (d) {
            players.get(0).setPoints(player1.getPoints() + K * (1 - player1Probability));
            players.get(1).setPoints(player2.getPoints() + K * (0 - player2Probability));
        } else {
            players.get(0).setPoints(player1.getPoints() + K * (0 - player1Probability));
            players.get(1).setPoints(player2.getPoints() + K * (1 - player2Probability));
        }

        return players;

    }

}
