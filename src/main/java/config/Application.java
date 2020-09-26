package config;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@ComponentScan({"service", "controller","repository","config"})
@EnableR2dbcRepositories({"repository"})
@EntityScan({"domain"})
@Log
public class Application  {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
