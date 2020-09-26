package repository.stream.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.DatabaseConfig;
import constant.StreamRepositoryConstant;
import domain.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import repository.stream.StreamRepository;
import repository.stream.UserStreamRepository;

@Repository
@Scope(value = "prototype")
public class UserStreamRepositoryImplementation extends StreamRepository implements UserStreamRepository {
    public UserStreamRepositoryImplementation(DatabaseConfig config) {
        super(config, StreamRepositoryConstant.USER_STREAM);
    }

    @Override
    public Flux<User> getUserStream() {
        ObjectMapper mapper = new ObjectMapper();
        return getConnection()
                .getNotifications()
                .map(notification -> {
                    String result = notification.getParameter();
                    try {
                        assert result != null;
                        JsonNode jsonNode = mapper.readTree(result);
                        String operation = jsonNode.get("operation").asText();
                        String user = jsonNode.get("record").toString();

                        return mapper.readValue(user, User.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    return null;
                });
    }
}
