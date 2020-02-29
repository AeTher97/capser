package org.rdc.capser.configuration;

import org.rdc.capser.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@ConditionalOnProperty(value = "cloud", havingValue = "true")
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        URI dbUri;
        if (Config.cloud()) {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } else {
            dbUri = new URI("postgres://pxggbtgwxyfmfm:d8c92540e10681b7b78cee0ed59e327b6eb1b104146da41ce5672b50daab8639@ec2-54-228-252-67.eu-west-1.compute.amazonaws.com:5432/d9pak96da54rda");
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();


    }
}
