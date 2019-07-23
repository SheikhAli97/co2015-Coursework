package group10.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The purpose of this class is to hold an overall story made up of extracts,
 * including statistics relating to the stories and its contributors.
 *
 * @author ben
 */
@Entity
public class Story {

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storyID")
    private long id;

    /**
     * The title given to the story.
     */
    @Column
    private String title;

    /**
     * The introduction of the story.
     */
    @Column
    private String introduction;

    /**
     * The category chosen by the Author.
     * Change to dynamic list.
     */
    @Enumerated(EnumType.STRING)
    private Category category;

    /**
     * The extracts that belong to this instance.
     */
    @ManyToMany
    @JoinTable(name = "story_extracts", joinColumns = @JoinColumn(name = "story_id"), inverseJoinColumns = @JoinColumn(name = "extract_id"))
    @OrderColumn
    private List<StoryExtract> storyExtracts;

    /**
     * The imageUrl to use as the cover photo. Can be null.
     */
    @Column
    private String imageUrl;

    /**
     * The user who created this story.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authorID")
    private User author;


    /**
     * Status of each story, Closed if the maximum allowed contributions is reached.
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * All the contributors to this story. Created in a new table called story_contributors. where:
     * Story_id = this id.
     * contributor_id = the id of the users.
     */
    @ManyToMany
    @JoinTable(name = "story_contributors", joinColumns = @JoinColumn(name = "story_id"), inverseJoinColumns = @JoinColumn(name = "contributor_id"))
    private Set<User> contributors;

    /**
     * The total number of likes this story has received.
     */
    @Column(name = "likes")
    private long likeCount;

    /**
     * The parent of this object. null if this is the top level object.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Story parent = null;

    /**
     * The list of branches from this object.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "story_branches", joinColumns = @JoinColumn(name = "story_id"), inverseJoinColumns = @JoinColumn(name = "branch_id"))
    private List<Story> branches;

    /**
     * Default constructor.
     */
    public Story() {
        this.storyExtracts = new LinkedList<>();
        this.status = Status.OPEN;
        this.contributors = new HashSet<>();
        branches = new ArrayList<>();
    }


    /**
     * Creates an instance of this class with only 3 fields.
     *
     * @param titl  The title of the story
     * @param intro The Introduction to the Story.
     * @param categ The category of the story.
     */

    public Story(String titl, String intro, Category categ) {
        this();
        this.title = titl;
        this.introduction = intro;
        this.category = categ;
    }


    /**
     * @return Returns our id.
     */
    public long getId() {
        return id;

    }

    /**
     * @param i Sets the id.
     */
    public void setId(long i) {
        id = i;
    }

    /**
     * @return introduction
     * Returns the introduction.
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param desc Sets the introduction.
     */
    public void setIntroduction(String desc) {
        introduction = desc;
    }


    /**
     * @return title
     */

    public String getTitle() {

        return this.title;

    }

    /**
     * Assigns the value passed in to the title field.
     *
     * @param titl The value to assign to the title field.
     */
    public void setTitle(String titl) {
        this.title = titl;
    }

    /**
     * Returns the Category assigned to this instance.
     *
     * @return The Category of this instance
     */

    public Category getCategory() {
        return category;
    }

    /**
     * Updates the current Category to the passed in one.
     *
     * @param categ The Category to assign to this instance.
     */

    public void setCategory(Category categ) {
        this.category = categ;
    }

    /**
     * Returns the list of StoryExtract that are apart of this class.
     *
     * @return The List of StoryExtracts of this instance.
     */

    public List<StoryExtract> getStoryExtracts() {
        return storyExtracts;
    }

    /**
     * Sets the Current StoryExtract List to the passed in one.
     *
     * @param storyExtractss The List of StoryExtracts to assign to this instance.
     */

    public void setStoryExtracts(List<StoryExtract> storyExtractss) {
        this.storyExtracts = storyExtractss;
    }

    /**
     * Retrieves the Size of the current Main Branch.
     *
     * @return The size of the mainBranch.
     */

    public int getStorySize() {
        return this.storyExtracts.size();
    }

    /**
     * Retrieves the imageUrl url set for this instance.
     *
     * @return - The imageUrl belonging to this.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the imageUrl associated with this instance.
     *
     * @param url - The imageUrl url.
     */
    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    /**
     * Get the author who started this story.
     *
     * @return - The author who created this instance.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Set the author for this story.
     *
     * @param u - The author to set.
     */

    public void setAuthor(User u) {
        this.author = u;
    }

    /**
     * Add to list of extracts.
     *
     * @param ex - The extract to add.
     */
    public void addExtract(StoryExtract ex) {
        this.storyExtracts.add(ex);
    }

    /**
     * Get the current status of the story.
     *
     * @return - The status of this object.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the status of the story.
     *
     * @param s - The status to assign.
     */
    public void setStatus(Status s) {
        this.status = s;
    }

    /**
     * Check if story is open to contributions.
     *
     * @return - True if story is open.
     */
    public boolean isOpen() {
        return this.status.equals(Status.OPEN);
    }

    /**
     * Add the passed in user to the contribution set and update the user side as well.
     *
     * @param con - The user who contributed to this story.
     */
    public void addContributor(User con) {
        this.contributors.add(con);
        // also add it to the user side.
        con.addStory(this);
    }

    /**
     * Retrieve the set of contributors.
     *
     * @return - The contributors of this story.
     */
    public Set<User> getContributors() {
        return this.contributors;
    }

    /**
     * Assign contributors to passed in param.
     *
     * @param con - The set to assign.
     */
    public void setContributors(Set<User> con) {
        this.contributors = con;
    }

    /**
     * Retrieve the number of contributors to this story.
     *
     * @return - The number of unique contributors to the story.
     */
    public int getContributorSize() {
        return this.contributors.size();
    }


    /**
     * Retrieve the total number of likes this story has.
     *
     * @return - The number of likes.
     */
    public long getLikeCount() {
        return likeCount;
    }

    /**
     * Set the number of likes to a story.
     *
     * @param likes - The likeCount to assign.
     */
    public void setLikeCount(long likes) {
        this.likeCount = likes;
    }

    /**
     * Increases the like counter by 1.
     */
    public void addLike() {
        this.likeCount++;
    }


    /**
     * Decreases the like counter by 1 if its not <= 0 already.
     */
    public void decLike() {
        if (likeCount <= 0) {
            return;
        }
        this.likeCount--;
    }

    /**
     * Add a story to the list of branches of this object.
     *
     * @param b - The new child to add=.
     */
    public void addBranch(Story b) {
        this.branches.add(b);
    }

    /**
     * Assign the parent of this object.
     *
     * @param p - The story to assign as the parent.
     */
    public void setParent(Story p) {
        this.parent = p;
    }

    /**
     * Get the parent of this object.
     *
     * @return - The parent of this instance.
     */
    public Story getParent() {
        return this.parent;
    }

    /**
     * Fetch the list of branches of this story.
     *
     * @return - List of all branches for this story.
     */
    public List<Story> getBranches() {
        return this.branches;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.introduction);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Story)) {
            return false;
        }

        // Two objects are the same if they have same title, introduction.
        Story other = (Story) obj;
        return this.title.equalsIgnoreCase(other.getTitle())
                && this.introduction.equalsIgnoreCase(other.getIntroduction())
                && this.getId() == other.getId();
    }

}
