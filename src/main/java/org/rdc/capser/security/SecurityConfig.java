package org.rdc.capser.security;

import org.rdc.capser.security.handler.CustomAuthenticationFailureHandler;
import org.rdc.capser.security.handler.CustomAuthenticationSuccessHandler;
import org.rdc.capser.services.PlayerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomAuthenticationProvider authenticationProvider;
    private final PlayerService playerService;

    public SecurityConfig(CustomAuthenticationProvider authenticationProvider, PlayerService playerService) {
        this.authenticationProvider = authenticationProvider;
        this.playerService = playerService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        this.authenticationProvider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.csrf().disable().cors().configurationSource(corsFilter()).and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/anonymous*").anonymous()
                .antMatchers("/login*").permitAll()
//                .antMatchers("/stats/*").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/index.html?").permitAll()
                .antMatchers("/games.html").permitAll()
                .antMatchers("/players").permitAll()
                .antMatchers("/games").permitAll()
                .antMatchers("/version").permitAll()
                .antMatchers("/register.html").permitAll()
                .antMatchers("/capsStyle.css").permitAll()
                .antMatchers("/commandments.html").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/perform/login")
                .defaultSuccessUrl("/index.html", true)
                .successHandler(new CustomAuthenticationSuccessHandler(playerService))
                .failureHandler(new CustomAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/perform/logout")
                .deleteCookies("JSESSIONID");
    }

    private UrlBasedCorsConfigurationSource corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler(PlayerService playerService) {
        return new CustomAuthenticationSuccessHandler(playerService);
    }
}
