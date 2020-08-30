package components;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class UserDataFetchers {
    @Autowired
    UserService userService;

    public DataFetcher findUserByUsernameDataFetcher() {
        return dataFetchingEnvironment -> {
            String username = dataFetchingEnvironment.getArgument("username");
            if (Objects.isNull(username))
                throw new NullPointerException();
            return userService.findUserByUsername(username);
        };
    }

    public DataFetcher findUserByUsernameContainingDataFetcher() {
        return dataFetchingEnvironment -> {
            String username = dataFetchingEnvironment.getArgument("username");
            if (Objects.isNull(username))
                throw new NullPointerException();
            return userService.findUserByUsernameContaining(username);
        };
    }

    public DataFetcher findUserByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            return userService.findById(id);
        };
    }

    public DataFetcher createUserDataFetcher() {
        return dataFetchingEnvironment -> {
            User user = User.mapFromArguments(dataFetchingEnvironment.getArguments());
            userService.create(user);
            return user;
        };
    }

    public DataFetcher deleteUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            userService.delete(id);
            return id;
        };
    }

    public DataFetcher updateUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            User user = User.mapFromArguments(dataFetchingEnvironment.getArguments());
            return userService.update(id, user);
        };
    }

    public Map<String, DataFetcher> getQueries() {
        Map<String, DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put("userByUsername", findUserByUsernameDataFetcher());
        dataFetcherMap.put("users", findUserByUsernameContainingDataFetcher());
        dataFetcherMap.put("user", findUserByIdDataFetcher());
        return dataFetcherMap;
    }

    public Map<String, DataFetcher> getMutations() {
        Map<String, DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put("createUser", createUserDataFetcher());
        dataFetcherMap.put("deleteUser", deleteUserDataFetcher());
        dataFetcherMap.put("updateUser", updateUserDataFetcher());
        return dataFetcherMap;
    }
}
