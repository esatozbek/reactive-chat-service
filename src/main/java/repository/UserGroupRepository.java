package repository;

import domain.UserGroup;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserGroupRepository extends ReactiveCrudRepository<UserGroup, Long> {
    @Query("select * from user_group where group_id = :groupId")
    Flux<UserGroup> findUserGroupByUserId(@Param("groupId") Long groupId);
}
