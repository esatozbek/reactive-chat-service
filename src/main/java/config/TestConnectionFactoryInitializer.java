package config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;

@Configuration
@Slf4j
public class TestConnectionFactoryInitializer {

    @Autowired
    private Environment environment;

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        if ((environment.getActiveProfiles()[0].equals("test"))) {
            ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
            initializer.setConnectionFactory(connectionFactory);
            CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
            populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
            log.warn("Executing schema.sql file!");

            initializer.setDatabasePopulator(populator);
            return initializer;
        }
        return null;
    }
}
