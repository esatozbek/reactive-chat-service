package components;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Group;
import domain.Message;
import domain.User;
import dto.MessageDTO;
import enums.MessageStatus;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.GroupService;
import service.MessageService;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageDataFetchers {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    public DataFetcher findMessageByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            return messageService.findById(id);
        };
    }

    public DataFetcher findMessagesDataFetcher() {
        return dataFetchingEnvironment -> {
            MessageDTO messageDTO = MessageDTO.mapFromArguments(dataFetchingEnvironment.getArguments());
            return messageService.findMessages(messageDTO);
        };
    }

    public DataFetcher createMessageDataFetcher() {
        return dataFetchingEnvironment -> {
            MessageDTO dto = MessageDTO.mapFromArguments(dataFetchingEnvironment.getArguments());
            Message message = messageService.fetchMessageEntity(dto);
            return messageService.create(message);
        };
    }

    public DataFetcher updateMessageDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            MessageDTO dto = MessageDTO.mapFromArguments(dataFetchingEnvironment.getArguments());
            Message message = messageService.fetchMessageEntity(dto);
            return messageService.update(id, message);
        };
    }

    public DataFetcher deleteMessageDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            messageService.delete(id);
            return id;
        };
    }

    public Map<String, DataFetcher> getQueries() {
        Map<String, DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put("message", findMessageByIdDataFetcher());
        dataFetcherMap.put("messages", findMessagesDataFetcher());
        return dataFetcherMap;
    }

    public Map<String, DataFetcher> getMutations() {
        Map<String, DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put("createMessage", createMessageDataFetcher());
        dataFetcherMap.put("updateMessage", updateMessageDataFetcher());
        dataFetcherMap.put("deleteMessage", deleteMessageDataFetcher());
        return dataFetcherMap;
    }
}
