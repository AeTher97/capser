package org.rdc.capser.services;

import org.rdc.capser.models.Creds;
import org.rdc.capser.models.Game;
import org.rdc.capser.models.Player;
import org.rdc.capser.models.RegisterRequest;
import org.rdc.capser.repositories.GamesRepository;
import org.rdc.capser.repositories.PlayersRepository;
import org.rdc.capser.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class DataService {

    private UserRepository userRepository;
    private PlayersRepository playersRepository;
    private GamesRepository gamesRepository;
    private PasswordEncoder passwordEncoder;

    public DataService(UserRepository userRepository, PlayersRepository playersRepository, GamesRepository gamesRepository, PasswordEncoder passwordEncoder) {
        this.gamesRepository = gamesRepository;
        this.userRepository = userRepository;
        this.playersRepository = playersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Player> getPlayersList() throws FileNotFoundException {

        return playersRepository.findAll();
    }

    public void savePlayer(Player player) {

        playersRepository.save(player);

    }


    public List<Game> getGamesList() {
        return gamesRepository.findAll();
    }

    public void saveGame(Game game) {
        gamesRepository.save(game);
    }

    public void addUser(RegisterRequest registerRequest) {
        Creds user = new Creds(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()));
        Player player = new Player(registerRequest.getUsername(), 500);
        playersRepository.save(player);
        userRepository.save(user);
    }


    public Player findPlayerById(Long id) {
        return playersRepository.findPlayerById(id);
    }

    public Player findPlayerByUsername(String username) {
        return playersRepository.findPlayerByName(username);
    }

    public List<Game> getPlayerGames(Long playerId) {
        return gamesRepository.findAllByPlayerIdOrOpponentId(playerId, playerId);
    }

    public List<Game> getOpponentGames(Long opponentId) {
        return gamesRepository.findAllByOpponentId(opponentId);
    }

}
