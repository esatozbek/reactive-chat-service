package repository.stream.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.DatabaseConfig;
import constant.StreamRepositoryConstant;
import domain.Message;
import domain.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import repository.stream.MessageStreamRepository;
import repository.stream.StreamRepository;

@Repository
@Scope(value = "prototype")
public class MessageStreamRepositoryImplementation extends StreamRepository implements MessageStreamRepository {
    public MessageStreamRepositoryImplementation(DatabaseConfig config) {
        super(config, StreamRepositoryConstant.MESSAGE_STREAM);
    }

    @Override
    public Flux<Message> getMessageStream() {
        ObjectMapper mapper = new ObjectMapper();
        return getConnection()
                .getNotifications()
                .map(notification -> {
                    String result = notification.getParameter();
                    try {
                        assert result != null;
                        JsonNode jsonNode = mapper.readTree(result);
                        String operation = jsonNode.get("operation").asText();
                        String message = jsonNode.get("record").toString();

                        return mapper.readValue(message, Message.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    return null;
                });
    }
}
