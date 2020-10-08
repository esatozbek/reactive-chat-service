package repository.reactive.implementation;

import domain.UserContact;
import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.reactive.UserContactRepository;

import java.util.function.BiFunction;

@Repository
public class UserContactRepositoryImpl implements UserContactRepository {
    private DatabaseClient databaseClient;
    private UserContactMapper mapper;


    public UserContactRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
        this.mapper = new UserContactMapper();
    }

    @Override
    public Mono<UserContact> save(UserContact userContact) {
        String query = "INSERT INTO user_contact(\"user_id\", \"contact_id\") VALUES (:userId, :contactId)";

        return databaseClient
                .insert()
                .into("user_contact")
                .value("user_id", userContact.getUserId())
                .value("contact_id", userContact.getContactId())
                .map(mapper::apply)
                .one();
    }

    @Override
    public Mono<Integer> delete(Long userId, Long contactId) {
        CriteriaDefinition criteriaDefinition = Criteria.from(Criteria.where("user_id").is(userId), Criteria.where("contact_id").is(contactId));

        return databaseClient
                .delete()
                .from("user_contact")
                .matching(criteriaDefinition)
                .fetch()
                .rowsUpdated();
    }

    @Override
    public Flux<UserContact> findContactsFromUser(Long userId) {
        try {
            String query = this.getClass().getInterfaces()[0].getMethod("findContactsFromUser", Long.class).getAnnotation(Query.class).value();

            return databaseClient
                    .execute(query)
                    .bind("userId", userId)
                    .map(mapper::apply)
                    .all();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    class UserContactMapper implements BiFunction<Row, Object, UserContact> {
        @Override
        public UserContact apply(Row row, Object o) {
            Long userId = row.get("user_id", Long.class);
            Long contactId = row.get("contact_id", Long.class);
            return new UserContact(userId, contactId);
        }
    }
}
