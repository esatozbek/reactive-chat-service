package repository.stream;

import config.DatabaseConfig;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class StreamRepository {
    private String query;
    private PostgresqlConnection connection;

    public StreamRepository(DatabaseConfig config, String query) {
        this.connection = Mono.from(config.connectionFactory().create()).cast(PostgresqlConnection.class).block();
        this.query = query;
    }


    @PostConstruct
    private void postConstruct() {
        connection
                .createStatement(query)
                .execute()
                .flatMap(PostgresqlResult::getRowsUpdated)
                .subscribe();
    }

    @PreDestroy
    private void preDestroy() {
        connection.close().subscribe();
    }

    public PostgresqlConnection getConnection() {
        return connection;
    }
}
