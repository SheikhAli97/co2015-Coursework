package group10.services;

import group10.domain.Story;
import group10.domain.User;
import group10.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * handles all logic responsible for user class.
 */
@Service
public class UserServices {

    /**
     * The repository for handling user related operations.
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * Searches the database for the given username.
     *
     * @param username - THe username to search against.
     * @return - The user object with the passed in username.
     */
    public User fetchUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with provided name does not exist"));
    }

    /**
     * Given a username adds the passed in story to list of liked stories by the user.
     * ALso increments the like counter ont he story object.
     *
     * @param username - THe username of the user.
     * @param story    - The story to add to the collection
     * @return - True if story was added to users liked set and like counter was incremented.
     */
    public boolean likeStory(String username, Story story) {
        // check not null probably.
        User user = userRepository.findByUsername(username).get();

        // cannot like a story u created
        if (story.getAuthor().equals(user)) {
            return false;
        }

        // if you already liked the story.
        if (user.getLikedStories().contains(story)) {
            return false;
        }
        // otherwise save
        user.likeStory(story);
        userRepository.save(user);
        return true;

    }

    /**
     * Removes the given story from the list of like stories for a user with given username.
     * Also decrements the like counter for story.
     *
     * @param username - The username to search for.
     * @param story    - The story to remove.
     */
    public void unlikeStory(String username, Story story) {
        User user = fetchUserByName(username);
        user.removeLike(story);
        userRepository.save(user);
    }

    /**
     * Checks whether the given user has liked the passed in story.
     *
     * @param u     - The user to check for.
     * @param story - The story to check against.
     * @return - True if the user has liked the story.
     */
    public boolean isLikedByUser(User u, Story story) {
        return story.getAuthor().equals(u) || u.getLikedStories().contains(story);
    }
}
