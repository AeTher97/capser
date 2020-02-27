package org.rdc.capser.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rdc.capser.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final PlayerService playerService;


    public CustomAuthenticationSuccessHandler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        Map<String, Object> data = new HashMap<>();
        data.put(
                "username",
                authentication.getPrincipal().toString());
        data.put(
                "id",
                playerService.getIdFromName(authentication.getPrincipal().toString()));

        httpServletResponse.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }
}
