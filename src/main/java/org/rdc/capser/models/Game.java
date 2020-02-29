package org.rdc.capser.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "games")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game implements Comparable<Game> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long playerId;

    private Date gameDate;
    private Long opponentId;
    private Long winner;
    private GameType gameType;
    private int playerScore;
    private int opponentScore;
    private int playerSinks;
    private int opponentSinks;
    private Float playerPointsChange;
    private Float opponentPointsChange;

    public Game(Long playerId,
                Long opponentId,
                GameType gameType,
                int playerScore,
                int opponentScore,
                int playerSinks,
                int opponentSinks,
                Long winnerId,
                Float playerPointsChange,
                Float opponentPointsChange) {
        this.playerId = playerId;
        this.opponentId = opponentId;
        this.gameType = gameType;
        this.playerScore = playerScore;
        this.opponentScore = opponentScore;
        this.opponentSinks = opponentSinks;
        this.playerSinks = playerSinks;
        this.winner = winnerId;
        this.gameDate = new Date();
        this.playerPointsChange = playerPointsChange;
        this.opponentPointsChange = opponentPointsChange;

        this.playerRebottles = getOpponentSinks() - opponentScore;
        this.opponentRebottles = getPlayerSinks() - playerScore;
        if (this.playerRebottles < 0) {
            this.playerRebottles = 0;
        }
        if (this.opponentRebottles < 0) {
            this.opponentRebottles = 0;
        }
    }

    private int playerRebottles;
    private int opponentRebottles;

    @Override
    public int compareTo(Game u) {
        if (u.getGameDate().toInstant().toEpochMilli() > this.getGameDate().toInstant().toEpochMilli()) {
            return 1;
        } else {
            return -1;
        }
    }
}
