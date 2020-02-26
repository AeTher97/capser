package org.rdc.capser.services;

import org.rdc.capser.models.Game;
import org.rdc.capser.models.requests.GameSaveRequest;
import org.rdc.capser.repositories.GamesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GamesRepository gamesRepository;

    public GameService(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    public Page<Game> getGames(Pageable pageable) {
        return gamesRepository.findAll(pageable);
    }


    public void saveGame(GameSaveRequest gameSaveRequest) {
//        Long playerId = dataService.findPlayerByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
//        Long opponentId = (long) gameRequest.getOpponentId();
//        GameType gameType = gameRequest.getGameType();
//        int playerScore = gameRequest.getPlayerScore();
//        int opponentScore = gameRequest.getOpponentScore();
//        int playerSinks = gameRequest.getPlayerSinks();
//        int opponentSinks = gameRequest.getOpponentSinks();
//
//
//        if (playerId > opponentId) {
//            Long tempId = playerId;
//            playerId = opponentId;
//            opponentId = tempId;
//
//            int tempScore = playerScore;
//            playerScore = opponentScore;
//            opponentScore = tempScore;
//
//            int tempSinks = playerSinks;
//            playerSinks = opponentSinks;
//            opponentSinks = tempSinks;
//        }
//
//        if (opponentScore == playerScore) {
//            return ErrorForm.errorForm("Game cannot end in a draw", "/gamePost.html");
//        }
//
//        if (opponentScore < 11 && playerScore < 11) {
//            return ErrorForm.errorForm("Game must end with one of the players obtaining 11 points", "/gamePost.html");
//        }
//
//        List<Game> list = dataService.getGamesList();
//
//
//        if (gameType == GameType.OVERTIME && Math.abs(opponentScore - playerScore) != 2) {
//            return ErrorForm.errorForm("Overtime game must finish with 2 points advantage", "/gamePost.html");
//        } else if (gameType == GameType.SUDDEN_DEATH && (opponentScore != 11 && playerScore != 11)) {
//            return ErrorForm.errorForm("Sudden death game must finish with 11 points", "/gamePost.html");
//        }
//
//        if (opponentId == playerId) {
//            return ErrorForm.errorForm("Cannot post game against yourself", "/gamePost.html");
//        }
//        Long winner;
//
//        boolean d;
//        if (playerScore > opponentScore) {
//            d = true;
//            winner = playerId;
//        } else {
//            d = false;
//            winner = opponentId;
//        }
//
//        Game game = new Game(playerId, opponentId, gameType, playerScore, opponentScore, playerSinks, opponentSinks, winner);
//        dataService.saveGame(game);
//
//        Player player1 = dataService.findPlayerById(playerId);
//        Player player2 = dataService.findPlayerById(opponentId);
//        List<Player> listToPass = new ArrayList<>();
//        listToPass.add(player1);
//        listToPass.add(player2);
//
//        List<Player> updatedList = EloRating.calculate(listToPass, 30, d);
//
//        assert updatedList != null;
//        for (Player player : updatedList) {
//            updatePlayer(player);
//            dataService.savePlayer(player);
//        }
//        return ErrorForm.successForm("Game saved successfully");
//
//
//
//        Game game = Game.builder()
//                .gameDate(new Date(System.currentTimeMillis()))
//                .gameType(gameSaveRequest.getGameType())
//                .opponentId(gameSaveRequest.getOpponentId())
//                .opponentRebottles(gameSaveRequest.get);
//        game.
//        gamesRepository.save(game);
//    }
    }
}
