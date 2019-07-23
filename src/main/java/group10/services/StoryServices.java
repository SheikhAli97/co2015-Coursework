package group10.services;

import group10.domain.Status;
import group10.domain.Story;
import group10.domain.StoryExtract;
import group10.domain.User;
import group10.persistence.ExtractRepository;
import group10.persistence.StoryRepository;
import group10.utility.ImgurUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Handles all the logic to do with story classes.
 */
@Service
public class StoryServices {

    /**
     * The repository that is able to access the story database.
     */

    @Autowired
    private StoryRepository storyRepository;

    /**
     * Auttowires an instance of the repository needed for persisting StoryExtracts.
     */
    @Autowired
    private ExtractRepository extractRepository;

    /**
     * The maxium number of extracts permitted per story.
     */

    private static final int MAX_EXTRACTS = 2;

    /**
     * Creates a story and saves it to the database, The passed in story would have to be validated first by the controller
     * before invoking this method.
     *
     * @param story  - The story to create.
     * @param author - The author of the story.
     * @param file   - The file uploaded by the author.
     */

    public void createStory(Story story, User author, MultipartFile file) {
        story.setImageUrl(fetchUrl(file));
        story.setAuthor(author);
        story.addContributor(author);
        saveStory(story);
    }

    /**
     * Adds an extract to the passed in story id.
     *
     * @param storyId       - The storyId to add this extract to.
     * @param extract       - The Extract object to add.
     * @param extractAuthor - The Author of the extract.
     * @return - True if extract was added.
     */

    public boolean addExtract(long storyId, StoryExtract extract, User extractAuthor) {
        if (!validUserToContribute(storyId, extractAuthor)) {
            return false;
        }

        Story storyFromId = storyRepository.getOne(storyId);
        // set the author of this extract.
        extract.setExtractAuthor(extractAuthor);
        // add this extract to set of contributors in the story. This also calls the relevant user method to add to his set.
        storyFromId.addContributor(extractAuthor);
        return addExtract(storyFromId, extract);
    }

    /**
     * Checks whether the given user has contributed already to the story.
     *
     * @param storyId - The id of the story to search in.
     * @param user    - The user to search for.
     * @return - True if the user has not contributed to the story before.
     */
    public boolean validUserToContribute(long storyId, User user) {
        Story story = storyRepository.getOne(storyId);
        return story.getContributors().stream().noneMatch(user::equals);
    }

    /**
     * Checks if the passed in author is the author of the story with given storyId.
     *
     * @param storyId - The id of the story to look  in.
     * @param author  - THe author to match against.
     * @return - True if the user is the author of the story.
     */
    public boolean isAuthor(long storyId, User author) {
        Story story = storyRepository.getOne(storyId);
        return story.getAuthor().equals(author);
    }

    /**
     * Adds an extract to the passed in story.
     *
     * @param story   - The story to add the extract to.
     * @param extract - The extract to add.
     * @return - True if extract addition was successful.
     */

    private boolean addExtract(Story story, StoryExtract extract) {
        // open only if the story has space available.
        if (!story.isOpen()) {
            return false;
        }
//        extract.setStory(story);
        extract.addStory(story);
        story.addExtract(extract);
        // update the status if no more spaces available
        if (!spaceAvailable(story)) {
            story.setStatus(Status.CLOSED);
        }
        extractRepository.save(extract);
        saveStory(story);
        return true;
    }


    /**
     * Checks that story is able to fit the addition of one extract.
     *
     * @param story - The story to check space in.
     * @return - True if space is available to add one element.
     */
    public boolean spaceAvailable(Story story) {
        return spaceAvailable(story, 1);
    }


    /**
     * Checks that story is able to fit the addition of n extract.
     *
     * @param story - The story to check available space in.
     * @param n     - The number of available spaces to check for.
     * @return - True if space is available to add all elements.
     */
    private boolean spaceAvailable(Story story, int n) {
        return story.getStorySize() + n <= MAX_EXTRACTS;
    }

    /**
     * Checks that story with storyId is able to fit the addition of one extract.
     *
     * @param storyId - The id of the story object to check space in.
     * @return - True if space is available for one element.
     */
    public boolean spaceAvailable(long storyId) {
        return spaceAvailable(storyRepository.getOne(storyId), 1);
    }


    /**
     * Retrieves the Imgur url as a result of uploading f.
     *
     * @param f - The file to upload and get the url for.
     * @return - The url to the image.
     */
    private String fetchUrl(MultipartFile f) {
        if (f == null) {
            return null;
        }

        String url;
        try {
            File imageFile = ImgurUploader.getFileFromMulti(f);
            if (imageFile.length() == 0) {
                throw new IOException("file length");
            }
            url = ImgurUploader.upload(imageFile);
            return url;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Persists the passed in story.
     *
     * @param s - The story to persist.
     */
    private void saveStory(Story s) {
        storyRepository.save(s);
    }

    /**
     * Checks to see if the content already exists in the story with storyId.
     *
     * @param storyId - The id of the story to check the extract contents of.
     * @param content - The content to match against.
     * @return - True if the passed in content exists in the story.
     */
    public boolean contentExistsInStory(long storyId, String content) {
        Story story = storyRepository.getOne(storyId);
        return story != null && story.getStoryExtracts().stream().anyMatch(extract -> extract.getContent().equalsIgnoreCase(content));
    }

    /**
     * Branches a story with the given story id at the point specified and adds the extract passed in.
     * If(extract) already exists somewhere else in the story, then the branching will not occur.
     *
     * @param storyId       - The story to branch from.
     * @param pointToBranch - The point at the story to branch at.
     * @param extract       - The story extract to add after the point.
     * @param branchingUser - The authenticated user creating the branch.
     * @return (- 1L) implies save was not done, any number >0L implies save was successful.
     */
    public long branchStory(long storyId, int pointToBranch, StoryExtract extract, User branchingUser) {
        // do not branch if the extract ur adding already exists in the story.
        if (contentExistsInStory(storyId, extract.getContent())) {
            return -1L;
        }

        Story parentStory = storyRepository.getOne(storyId);

        // Extract a sublist of the extacts
        List<StoryExtract> subList = new LinkedList<>(parentStory.getStoryExtracts().subList(0, pointToBranch));

        // set the author for the extract
        extract.setExtractAuthor(branchingUser);

        // add to the sublist
        subList.add(extract);

        // create a new Story with exact same values as the parent story
        Story newStory = new Story(parentStory.getTitle(), parentStory.getIntroduction(), parentStory.getCategory());
        newStory.setAuthor(parentStory.getAuthor());

        // go through sublist and add the new story as one of the stories the segment belongs to
        subList.forEach(e -> e.addStory(newStory));

        // set the extract for the new story
        newStory.setStoryExtracts(subList);

        // update the status of the newStory
        if (!spaceAvailable(newStory)) {
            newStory.setStatus(Status.CLOSED);
        }

        // set other fields for the branched story
        newStory.setParent(parentStory);
        newStory.addContributor(branchingUser);

        // othersides of the jpa
        parentStory.addBranch(newStory);
        branchingUser.addStory(newStory);

        extractRepository.saveAll(subList);
        storyRepository.save(newStory);

        return newStory.getId();
    }


}
