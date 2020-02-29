package org.rdc.capser.security;

import org.rdc.capser.models.Creds;
import org.rdc.capser.models.Player;
import org.rdc.capser.repositories.UserRepository;
import org.rdc.capser.services.PlayerService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;
    private PlayerService playerService;
    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserRepository userRepository, PlayerService playerService) {
        this.userRepository = userRepository;
        this.playerService = playerService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Creds user = userRepository.findCredsByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with this username doesn't exist");
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            Player player = playerService.getPlayerById(playerService.getIdFromName(user.getUsername()));
            player.setLastSeen(new Date(System.currentTimeMillis()));
            playerService.savePlayer(player);
            return new UsernamePasswordAuthenticationToken(
                    username, password, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
