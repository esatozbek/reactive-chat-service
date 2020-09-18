package config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories
public class DatabaseConfig extends AbstractR2dbcConfiguration {
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Override
    public ConnectionFactory connectionFactory() {
        if (activeProfile.equals("test"))
            return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                    .option(DRIVER, "h2")
                    .option(HOST, "localhost")
                    .option(PORT, 7272)
                    .option(USER, "sa")
                    .option(PASSWORD, "password")
                    .build());
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, 5432)
                .option(USER, "postgres")
                .option(PASSWORD, "root")
                .option(DATABASE, "graphql_chat")
                .build());

    }
}
