package group10.persistence;

import group10.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Responsible for Handling CRUD and Other JPA functions for the Story Table/Class.
 */
public interface StoryRepository extends JpaRepository<Story, Long> {
    /**
     * Returns an Optional<Story> if there exists a Story with Matching Title and Description.
     *
     * @param title        The title to match against.
     * @param introduction The description to match again.
     * @return An Object that is only present if a match is found.
     */
    Optional<Story> findByTitleAndIntroduction(String title, String introduction);

    /**
     * Returns an Optional<Story> if there exists a story with Matching title.
     *
     * @param title The title of the story to match against.
     * @return Present if Story with same title Exists
     */
    Optional<Story> findByTitle(String title);

    /**
     * Return a list of all stories where the root parent is null (i.e the returned objects are story roots).
     *
     * @return - The list of stories where the storyparent == null.
     */
    // only display root objects
    List<Story> findAllByParentIsNull();

    /**
     * Find all stories whos parent is the passed in param.
     *
     * @param parent - THe parent to find the children/branches for.
     * @return - THe list of all branches from the parent story.
     */
    List<Story> findAllByParentOrderByLikeCountDesc(Story parent);

}
