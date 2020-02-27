package org.rdc.capser.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.rdc.capser.models.Game;
import org.rdc.capser.models.GameType;

import java.util.Date;


@Data
@AllArgsConstructor
@Builder
public class GameResponse implements Comparable<org.rdc.capser.models.Game> {

    private Long id;
    private Long playerId;
    private Date gameDate;
    private Long opponentId;
    private String winner;
    private GameType gameType;
    private int playerScore;
    private int opponentScore;
    private int playerSinks;
    private int opponentSinks;
    private String opponentName;
    private String playerName;
    private Float playerPointsChange;
    private Float opponentPointsChange;
    private int playerRebuttals;
    private int opponentRebuttals;

    public GameResponse(Game game
    ) {
        this.id = game.getId();
        this.playerId = game.getPlayerId();
        this.opponentId = game.getOpponentId();
        this.gameType = game.getGameType();
        this.playerScore = game.getPlayerScore();
        this.opponentScore = game.getOpponentScore();
        this.opponentSinks = game.getOpponentSinks();
        this.playerSinks = game.getPlayerSinks();
        this.gameDate = game.getGameDate();
        this.opponentPointsChange = game.getOpponentPointsChange();
        this.playerPointsChange = game.getPlayerPointsChange();

        this.playerRebuttals = getOpponentSinks() - opponentScore;
        this.opponentRebuttals = getPlayerSinks() - playerScore;
        if (this.playerRebuttals < 0) {
            this.playerRebuttals = 0;
        }
        if (this.opponentRebuttals < 0) {
            this.opponentRebuttals = 0;
        }
    }

    @Override
    public int compareTo(org.rdc.capser.models.Game u) {
        if (u.getGameDate().toInstant().toEpochMilli() > this.getGameDate().toInstant().toEpochMilli()) {
            return 1;
        } else {
            return -1;
        }
    }
}
