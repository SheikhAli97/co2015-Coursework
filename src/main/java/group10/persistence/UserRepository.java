package group10.persistence;

import group10.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository Responsible for Handling CRUD and Other JPA functions for the User Table/Class.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Returns an Optional<Username> if there exists a User with the username provided.
     *
     * @param username       The username to match against.
     * @return An Object that is only present if a match is found.
     */

    Optional<User> findByUsername(String username);

    /**
     * Returns an Optional<User> if there exists a story with Matching title.
     * @param username The username we will check for.
     * @param password The hashed password which we will check against.
     * @return Present if the Username and Password are correct.
     */
    Optional<User> findByUsernameAndPassword(String username, String password);
}
