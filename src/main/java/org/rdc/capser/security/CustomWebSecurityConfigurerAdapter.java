package org.rdc.capser.security;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.rdc.capser.models.Creds;
import org.rdc.capser.models.CredsList;
import org.rdc.capser.models.Game;
import org.rdc.capser.models.GamesList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/index.html?").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/games.html").permitAll()
                .antMatchers("/players").permitAll()
                .antMatchers("/games").permitAll()
                .antMatchers("/version").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();

    }

    private final String CREDS_PATH = "D:/ServerDataDev/creds.txt";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication)
            throws Exception {
        try {
            Gson gson = new Gson();
            JsonReader reader2 = new JsonReader(new FileReader(CREDS_PATH));

            CredsList result = gson.fromJson(reader2, CredsList.class);
            reader2.close();

            for (Creds creds : result.getData()) {
                authentication.inMemoryAuthentication().withUser(String.valueOf(creds.getId())).password("{noop}" + creds.getPassword()).authorities("USER");
            }

        } catch (IOException e) {
            e.printStackTrace();

        }


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}