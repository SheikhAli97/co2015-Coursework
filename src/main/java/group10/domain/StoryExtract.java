package group10.domain;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The intent of this class is to story
 * an extract of a full story and any
 * relevant methods.
 *
 * @author ben
 */
@Entity
@Table(name = "extracts")
public class StoryExtract {

    /**
     * The Collection of stories this extract belongs to.
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "storyExtracts")
    private Set<Story> stories;

    /**
     * The unique id of this object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long extractId;

    /**
     * The text contributed by a User.
     */
    @Column
    private String content;

    /**
     * The User who created this instance.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authorID")
    private User extractAuthor;

    /**
     * Creates an Instance of this class.
     *
     * @param con  - The content of this extract/segment.
     * @param stor - The story this extract belongs to.
     */
    public StoryExtract(String con, @NonNull Story stor) {
        this();
        this.content = con;
        this.stories.add(stor);
    }

    /**
     * Default constructor.
     */
    public StoryExtract() {
        this.stories = new HashSet<>();
    }

    /**
     * Retrieve the story this extract belongs to.
     *
     * @return - The story this object belongs to.
     */
    public Set<Story> getStories() {
        return stories;
    }

    /**
     * Set the story this extract belongs to.
     *
     * @param s - The Story this extract belongs to.
     */
    public void addStory(Story s) {
        this.stories.add(s);
    }

    /**
     * Retrieve the current Id of this instance. Null if not persisted.
     *
     * @return - The unique id for this instance.
     */
    public long getExtractId() {
        return extractId;
    }

    /**
     * Set the unique id for this instance.
     *
     * @param id - The id to assign.
     */

    public void setExtractId(long id) {
        this.extractId = id;
    }

    /**
     * Retrieves the content of this instance.
     *
     * @return The content of this instance
     */

    public String getContent() {
        return content;
    }

    /**
     * Sets the content for this instance.
     *
     * @param contentt The Content to assign to this instance.
     */

    public void setContent(String contentt) {
        this.content = contentt;
    }

    /**
     * Two Objects are the same if they have exactly matching fields.
     *
     * @param obj The obj to test equality against
     * @return True if this object and the passed in obj can be considered equal
     */

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StoryExtract)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        StoryExtract other = (StoryExtract) obj;
        return new HashSet<>(this.stories).equals(new HashSet<>(other.getStories())) && this.content.equalsIgnoreCase(other.getContent());
    }


    /**
     * Returns a hashcode value for the object.
     *
     * @return a hashcode value for this particular object.
     */


    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<>(this.stories).hashCode(), this.content);
    }

    /**
     * Get the creator of this extract.
     *
     * @return - The user who created this story.
     */
    public User getExtractAuthor() {
        return extractAuthor;
    }

    /**
     * Assign a user to be the author of this extract.
     *
     * @param auth - The user to assign as author.
     */

    public void setExtractAuthor(User auth) {
        this.extractAuthor = auth;
    }
}
