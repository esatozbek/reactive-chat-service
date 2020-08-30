package repository;

import domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByTitle(String title);
    List<Group> findByTitleContaining(String title);
}
