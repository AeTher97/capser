package org.rdc.capser;

import org.rdc.capser.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Capser {


    public static void main(String[] args) {

        if (Config.cloud()) {
            String ENV_PORT = System.getenv().get("PORT");
            String ENV_DYNO = System.getenv().get("DYNO");
            if (ENV_PORT != null && ENV_DYNO != null) {
                System.getProperties().put("server.port", ENV_PORT);
            }
        }
        SpringApplication.run(Capser.class, args);
    }

}
