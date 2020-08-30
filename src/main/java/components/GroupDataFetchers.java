package components;

import domain.Group;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.GroupService;

import java.util.HashMap;
import java.util.Map;

@Component
public class GroupDataFetchers {
    @Autowired
    private GroupService groupService;

    public DataFetcher findGroupByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            return groupService.findById(id);
        };
    }

    public DataFetcher findGroupByTitle() {
        return dataFetchingEnvironment -> {
            String title = dataFetchingEnvironment.getArgument("title");
            return groupService.findGroupByTitle(title);
        };
    }

    public DataFetcher findGroupByTitleContaining() {
        return dataFetchingEnvironment -> {
            String title = dataFetchingEnvironment.getArgument("title");
            return groupService.findGroupByTitleContaining(title);
        };
    }

    public DataFetcher createGroupDataFetcher() {
        return dataFetchingEnvironment -> {
            Group group = Group.mapFromArguments(dataFetchingEnvironment.getArguments());
            return groupService.create(group);
        };
    }

    public DataFetcher updateGroupDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            Group group = Group.mapFromArguments(dataFetchingEnvironment.getArguments());
            return groupService.update(id, group);
        };
    }

    public DataFetcher deleteGroupDataFetcher() {
        return dataFetchingEnvironment -> {
            Long id = dataFetchingEnvironment.getArgument("id");
            groupService.delete(id);
            return id;
        };
    }

    public Map<String, DataFetcher> getQueries() {
        Map<String, DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put("group", findGroupByIdDataFetcher());
        dataFetcherMap.put("groupByTitle", findGroupByTitle());
        dataFetcherMap.put("groups", findGroupByTitleContaining());
        return dataFetcherMap;
    }

    public Map<String, DataFetcher> getMutations() {
        Map<String, DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put("createGroup", createGroupDataFetcher());
        dataFetcherMap.put("updateGroup", updateGroupDataFetcher());
        dataFetcherMap.put("deleteGroup", deleteGroupDataFetcher());
        return dataFetcherMap;
    }
}