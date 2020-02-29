package org.rdc.capser.models.requests;

import lombok.Data;
import org.rdc.capser.models.GameType;

@Data
public class GameSaveRequest {
    private int opponentId;
    private GameType gameType;
    private int playerScore;
    private int opponentScore;
    private int playerSinks;
    private int opponentSinks;

}
