package repository.stream.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.DatabaseConfig;
import constant.StreamRepositoryConstant;
import domain.Group;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import repository.stream.GroupStreamRepository;
import repository.stream.StreamRepository;

@Repository
@Scope(value = "prototype")
public class GroupStreamRepositoryImplementation extends StreamRepository implements GroupStreamRepository {
    public GroupStreamRepositoryImplementation(DatabaseConfig config) {
        super(config, StreamRepositoryConstant.GROUP_STREAM);
    }

    @Override
    public Flux<Group> getGroupStream() {
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

                        return mapper.readValue(user, Group.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    return null;
                });
    }
}
