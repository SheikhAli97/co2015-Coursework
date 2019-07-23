package group10.persistence;

import group10.domain.StoryExtract;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository Responsible for Handling CRUD and Other JPA functions for the StoryExtract Table/Class.
 */
public interface ExtractRepository extends JpaRepository<StoryExtract, Long> {
}
