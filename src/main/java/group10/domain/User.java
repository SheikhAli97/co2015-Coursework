package group10.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * User class responsible for holding a user's
 * credentials, (eventually) profile information,
 * story's created etc.
 */
@Entity
@Table
public class User {

    /**
     * The unique identifier (primary key) for the
     * user profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;
    /**
     * The user's name / unique login.
     */
    @Column(unique = true)
    private String username;
    /**
     * The user's password, will need
     * to be stored in encrypted form.
     */
    @Column
    private String password;

    /**
     * A number corresponding to the number of
     * contributions this user has made.
     */
    @Column
    private int numContributions;

    /**
     * The collection of stories this user has contributed to by either creating the story or adding a segment.
     */
    @ManyToMany(mappedBy = "contributors")
    private Set<Story> contributions;

    /**
     * The collection of stories this user has liked.
     */
    @ManyToMany
    @JoinTable(name = "user_likedStories", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "story_id"))
    private List<Story> likedStories;

    /**
     * Creates an instance of User.
     *
     * @param u The username for the user.
     * @param p The password for the user.
     * @param c The contributions for the user.
     */
    public User(String u, String p, int c) {
        this.username = u;
        this.password = p;
        this.numContributions = c;
        this.contributions = new HashSet<>();
        this.likedStories = new ArrayList<>();
    }

    /**
     * Creates an instance of User using 4 fields.
     *
     * @param u The username for the user.
     * @param p The password for the user.
     * @param c The contributions for the user.
     * @param i The ID for the user.
     */
    public User(String u, String p, int c, long i) {
        this.username = u;
        this.password = p;
        this.numContributions = c;
        this.userID = i;

    }

    /**
     * Creates an instance of User.
     *
     * @param u - The username for this user.
     * @param p - The password for this user.
     */
    public User(String u, String p) {
        this(u, p, 0);
    }

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {

        return username;

    }

    /**
     * @param u Sets the username.
     */
    public void setUsername(String u) {

        this.username = u;

    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {

        return password;

    }

    /**
     * @param c Sets the number of contributions.
     */
    public void setNumContributions(int c) {

        this.numContributions = c;

    }

    /**
     * @return Returns the number of contributions.
     */
    public int getNumContributions() {

        return numContributions;

    }

    /**
     * @return Returns the unique identifier, userID.
     */
    public long getUserID() {

        return userID;

    }

    /**
     * Set the user id for this user.
     *
     * @param uid - The id to set.
     */
    public void setUserID(long uid) {
        this.userID = uid;
    }

    /**
     * Set the password for this user.
     *
     * @param pass - The password to set.
     */

    public void setPassword(String pass) {
        this.password = pass;
    }


    /**
     * Get the stories this user has liked.
     *
     * @return - The stories this user has liked.
     */
    public List<Story> getLikedStories() {
        return likedStories;
    }

    /**
     * Set the liked stories.
     *
     * @param stories - The new value to assign.
     */

    public void setLikedStories(List<Story> stories) {
        this.likedStories = stories;
    }

    /**
     * Likes a story by adding to the set of liked list and increases that stories like counter.
     *
     * @param story - The story this user has liked.
     * @return - True if user liked the story.
     */
    public boolean likeStory(Story story) {
        if (this.likedStories.add(story)) {
            story.addLike();
            return true;
        }
        return false;
    }

    /**
     * Remove the passed in story from the collection of like stories.
     * If remove is performed dec the like counter for the user.
     *
     * @param story - The story to remove from the collection.
     */
    public void removeLike(Story story) {
        if (this.likedStories.remove(story)) {
            story.decLike();
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        User other = (User) obj;
        return this.username.equalsIgnoreCase(other.getUsername());
    }

    /**
     * Get the list of contributions this user has made.
     *
     * @return - The set of all stories this user has authored/contributed to.
     */
    public Set<Story> getContributions() {
        return contributions;
    }

    /**
     * Adds the pased in story to the set of stories that this user has contributed to.
     * If the save is successful the number of contributions is increased.
     *
     * @param s - The story to add.
     */
    public void addStory(Story s) {
        if (this.contributions.add(s)) {
            this.numContributions++;
        }
    }
}
