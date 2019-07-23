package group10;

import group10.domain.Category;
import group10.domain.Story;
import group10.domain.StoryExtract;
import group10.domain.User;
import group10.persistence.StoryRepository;
import group10.persistence.UserRepository;
import group10.services.StoryServices;
import group10.services.UserServices;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "admin") // spring auto configures an authenticated user for us with this
public class StoryControllerTests {

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServices userServices;

    @Autowired
    StoryServices storyServices;

    @Autowired
    MockMvc mockMvc;
    // will use an admin object to make requests.
    private static User admin;
    // will need a principal now
    private static Principal mockPrincipal;


    /**
     * Delete all entries in the storyRepository after each test.
     */

    @Before
    public void clearRepository() {
        storyRepository.deleteAllInBatch();
        if (userRepository.findAll().isEmpty())
            userRepository.save(admin);
    }

    /**
     * Set up the mock principal.
     */
    @BeforeClass
    public static void setMockPrincipal() {
        mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");
        admin = new User("admin", new BCryptPasswordEncoder().encode("admin"));

    }

    /**
     * Tests the index() method which displays our storyCreation jsp with category model attribute pre-added.
     *
     * @throws Exception Perform can throw an Exception.
     */
    @Test
    public void pageShow() throws Exception {
        mockMvc.perform(get("/story/new")).andExpect(status().isOk()).andExpect(view().name("storyCreation"))
                .andExpect(model().attribute("categories", Category.values()));

    }

    /**
     * Tests the addStory() method which will add to our database if all validations check out correctly.
     *
     * @throws Exception
     */

    @Test
    public void canAddStoriesWithProperFields() throws Exception {
        mockMvc.perform(multipart("/story/create/").file(new MockMultipartFile("file", new byte[0])).flashAttr("Story", new Story()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "title")
                .param("introduction", "introduction")
                .param("category", "Adventure")
                .principal(mockPrincipal)
                .with(csrf())
        ).andExpect(redirectedUrl("/explore"));

        // after the above operation our database should have only one entry
        assertThat(storyRepository.findAll().size(), is(1));

        // additionally fetch the story from db
        Story fromDb = storyRepository.findAll().get(0);

        // check each field matches
        assertThat(fromDb.getTitle(), is("title"));
        assertThat(fromDb.getIntroduction(), is("introduction"));
        assertThat(fromDb.getCategory().toString(), is("Adventure"));
    }

    /**
     * Tests that addStory() will not accept stories that already exists in the database.
     *
     * @throws Exception
     */
    @Test
    public void cannotAddExistingStoryWithExistingTitleAndDescription() throws Exception {
        // an existing entity in our test database
        Story story = new Story("Title1", "introduction1", Category.Other);


        // save it to our database using our service.
        storyServices.createStory(story, admin, null);

        // ensure that save is successful
        assertThat(storyRepository.findAll().size(), is(1));


        // perform post request with existing data.
        // will not save and our model will have field errors.
        mockMvc.perform(multipart("/story/create/").file(new MockMultipartFile("file", new byte[0])).flashAttr("Story", new Story()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Title1")
                .param("introduction", "introduction1")
                .param("category", "Other")
                .principal(mockPrincipal)
                .with(csrf()))
                .andDo(print())
                .andExpect(forwardedUrl("/story/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("Story"))
                .andExpect(model().attributeHasFieldErrors("Story", "title", "introduction"));


        // database will remain unchanged.
        assertThat(storyRepository.findAll().size(), is(1));
    }

    @Test
    public void cannotAddExistingTitle() throws Exception {
        Story someStory = new Story("the title", "the first line", Category.Adventure);
        someStory.setAuthor(admin);


        // save it to our database using our service.
        storyServices.createStory(someStory, admin, null);

        // ensure that save is successful
        assertThat(storyRepository.findAll().size(), is(1));

        // attempt to add story with the title already existing in database.
        // save will not be successful and our model will only contain title errors.
        mockMvc.perform(multipart("/story/create/").file("file", new byte[0]).flashAttr("Story", new Story()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "the title")
                .param("introduction1", "introduction1")
                .param("category", "Other")
                .principal(mockPrincipal)
                .with(csrf()))
                .andDo(print())
                .andExpect(forwardedUrl("/story/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("Story"))
                .andExpect(model().attributeHasFieldErrors("Story", "title"));

        // database will remain unchanged.
        assertThat(storyRepository.findAll().size(), is(1));
    }

    @Test
    @WithAnonymousUser
    @Transactional
    public void canAccessStoriesWithAnonymousUser() throws Exception {
        // test story object.
        Story testStory = new Story("Title", "Description", Category.Romance);

        // use the story service to save as our controller does.
        storyServices.createStory(testStory, admin, null);

        // check save is successful by interacting with the repository itself.
        assertThat(storyRepository.findAll().size(), is(1));

        // get the id assigned to the test story by JPA.
        long storyId = storyRepository.findAll().get(0).getId();

        // attempt a get request on /display with the known id above.
        mockMvc.perform(get("/story/display")
                .param("id", String.valueOf(storyId)))
                .andExpect(status().isOk())
                .andExpect(view().name("storyDetails"))
                .andExpect(model().attributeExists("story"))
                .andExpect(model().attribute("canContribute", true)) // will be true as anonymous user has not contributed before.
                .andExpect(model().attribute("isAuthor", false)) // will be false as the author is the admin.
                .andExpect(model().attribute("likedAlready", false)); // will be false as anonymous user could not have liked the story before.
    }

    @Test
    public void canAccessStoriesWIthAuthenticatedUser() throws Exception {
        // test story object.
        Story testStory = new Story("Title", "Description", Category.Romance);

        // use the story service to save this test story.
        storyServices.createStory(testStory, admin, null);

        // check save is successful by interacting with the repository itself.
        assertThat(storyRepository.findAll().size(), is(1));

        // attempt a get request on /display with the known id above.
        mockMvc.perform(get("/story/display")
                .param("id", String.valueOf(testStory.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("storyDetails"))
                .andExpect(model().attributeExists("story"))
                .andExpect(model().attribute("canContribute", false)) // will be false as the author who is the admin has already contributed the intro.
                .andExpect(model().attribute("isAuthor", true)) // will be true as the admin is the author
                .andExpect(model().attribute("likedAlready", true)); // will be true as the author automatically likes the story he creates.
    }

    @Test
    @WithMockUser(username = "admin2")
    @Transactional
    public void authUserCanContribute() throws Exception {
        // Create our story to contribute to.
        Story testStory = new Story("Story1", "Intro", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // save our admin2 user so he can contribute
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));
        userRepository.save(admin2);

        // perform post to /addContribution/
        mockMvc.perform(post("/story/addContribution/").flashAttr("StoryExtract", new StoryExtract()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "content1")
                .param("id", String.valueOf(testStory.getId()))
                .with(csrf()))
                .andExpect(redirectedUrl("/story/display?id=" + testStory.getId()));

        // check our testStory object has the extract added with the correct author

        // could also fetch from the storyrepoistory same result.
        assertThat(testStory.getStoryExtracts(), is(not(empty())));

        // check all fields are as expected
        StoryExtract extract = testStory.getStoryExtracts().get(0);
        assertThat(extract.getContent(), is("content1"));
        assertThat(extract.getExtractAuthor(), is(admin2));
    }

    @Test
    @WithMockUser(username = "admin2")
    @Transactional
    public void authUserCannotContributeExistingContent() throws Exception {
        // Create our story to contribute to.
        Story testStory = new Story("Story1", "Intro", Category.Adventure);
        StoryExtract extract = new StoryExtract("content1", testStory);

        // create our story with admin as the author
        storyServices.createStory(testStory, admin, null);

        // user creating this request
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));

        // user who created the extract.
        User admin3 = new User("admin3", new BCryptPasswordEncoder().encode("admin3"));

        // save the two users
        userRepository.save(admin2);
        userRepository.save(admin3);

        // add our extract using the service and the author as a admin 3.
        storyServices.addExtract(testStory.getId(), extract, admin3);

        // perform post to /addContribution/ with the same extract
        mockMvc.perform(post("/story/addContribution/").flashAttr("StoryExtract", new StoryExtract()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "content1")
                .param("id", String.valueOf(testStory.getId()))
                .with(csrf()))
                .andDo(print())
                .andExpect(flash().attribute("existingContent", extract.getContent()))
                .andExpect(flash().attribute("errorMessage", "Content already exists"))
                .andExpect(redirectedUrl("/story/display?id=" + testStory.getId()));
    }

    @Test
    @WithMockUser(username = "admin2")
    @Transactional
    public void cannot_contribute_twice_sameStory() throws Exception {
        // Create our story to contribute to.
        Story testStory = new Story("Story1", "Intro", Category.Adventure);
        StoryExtract extract = new StoryExtract("content1", testStory);

        // create our story with admin as the author
        storyServices.createStory(testStory, admin, null);

        // user creating this request
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));

        // save the user
        userRepository.save(admin2);

        // add our extract using the service and the author as a admin 3.
        storyServices.addExtract(testStory.getId(), extract, admin2);

        // set up mock principal to return admin2
        Mockito.when(mockPrincipal.getName()).thenReturn("admin2");

        // perform extract addition with the same user who contributed before
       ResultActions result =  mockMvc.perform(post("/story/addContribution/").flashAttr("StoryExtract", new StoryExtract()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "content111111")
                .param("id", String.valueOf(testStory.getId()))
                .with(csrf()))
                .andDo(print());

        // story data will not be changed as the user has already contributed.
        assertThat(testStory.getStoryExtracts().size(), is(1));

        // redirect to the same page the user attempted to contribute from
        result.andExpect(redirectedUrl("/story/display?id=" + testStory.getId()));

    }


//    Test like and dislike

    @Test
    @WithMockUser(username = "admin2")
    @Transactional
    public void authUserCanLikeAStory() throws Exception {

        // test story to like.
        Story testStory = new Story("Story1", "Intro1", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // user performing like operation
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));
        userRepository.save(admin2);

        Mockito.when(mockPrincipal.getName()).thenReturn("admin2");

        // perform like request
        ResultActions result = mockMvc.perform(get("/story/like/")
                .param("id", String.valueOf(testStory.getId()))
                .param("action", "like")
                .principal(mockPrincipal));

        // user is redirected to the page displaying the story
        result.andExpect(redirectedUrl("/story/display?id=" + testStory.getId()));

        // like counter for story is incremented by 1
        assertThat(testStory.getLikeCount(), is(1L));

        // story is added to the users list of liked stories
        assertThat(admin2.getLikedStories(), is(not(empty())));
        assertTrue(admin2.getLikedStories().contains(testStory));

    }

    @Test
    @WithAnonymousUser
    public void unAuthUserCannotLikeStory() throws Exception {
        // test story to like.
        Story testStory = new Story("Story1", "Intro1", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // perform like request
        ResultActions result = mockMvc.perform(get("/story/like/")
                .param("id", String.valueOf(testStory.getId()))
                .param("action", "like")
                .principal(mockPrincipal));


        // user is redirected to login page.
        result.andExpect(status().is(302));
        result.andExpect(redirectedUrl("http://localhost/user-login"));

    }


    @Test
    @WithMockUser(username = "admin2")
    @Transactional
    public void likeLikedStory() throws Exception {
        // test story to like.
        Story testStory = new Story("Story1", "Intro1", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // user performing like operation
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));
        userRepository.save(admin2);

        // like the story
        userServices.likeStory("admin2", testStory);

        Mockito.when(mockPrincipal.getName()).thenReturn("admin2");

        // perform like request
        ResultActions result = mockMvc.perform(get("/story/like/")
                .param("id", String.valueOf(testStory.getId()))
                .param("action", "like")
                .principal(mockPrincipal));

        // user is redirected to the page displaying the story
        result.andExpect(redirectedUrl("/story/display?id=" + testStory.getId()));

        // like counter for story is not changed and stays at 1.
        assertThat(testStory.getLikeCount(), is(1L));
    }

    @Test
    @WithMockUser(username = "admin2")
    @Transactional
    public void dislikeStory() throws Exception {
        // test story to like.
        Story testStory = new Story("Story1", "Intro1", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // user performing like operation
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));
        userRepository.save(admin2);

        // like the story
        userServices.likeStory("admin2", testStory);

        Mockito.when(mockPrincipal.getName()).thenReturn("admin2");

        // perform like request
        ResultActions result = mockMvc.perform(get("/story/like/")
                .param("id", String.valueOf(testStory.getId()))
                .param("action", "dislike")
                .principal(mockPrincipal));

        // user is redirected to the page displaying the story
        result.andExpect(redirectedUrl("/story/display?id=" + testStory.getId()));

        // like counter for story is decremented to 0.
        assertThat(testStory.getLikeCount(), is(0L));

        // story is removed from the users like stories
        assertFalse(admin2.getLikedStories().contains(testStory));

    }

    @Test
    @Transactional
    public void authorLikeStory() throws Exception {
        // test story to like.
        Story testStory = new Story("Story1", "Intro1", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // perform like request
        ResultActions result = mockMvc.perform(get("/story/like/")
                .param("id", String.valueOf(testStory.getId()))
                .param("action", "like")
                .principal(mockPrincipal));

        // user is redirected to the page displaying the story
        result.andExpect(redirectedUrl("/story/display?id=" + testStory.getId()));

        // like counter for story stays at 0.
        assertThat(testStory.getLikeCount(), is(0L));

        // story is removed from the users like stories
        assertFalse(admin.getLikedStories().contains(testStory));
    }

    @Test
    @Transactional
    public void spaceAvailableServiceTest() throws Exception {
        // test story to like.
        Story testStory = new Story("Story1", "Intro1", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // two extract
        StoryExtract extract = new StoryExtract("content1", testStory);
        StoryExtract extract2 = new StoryExtract("content2", testStory);

        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));
        User admin3 = new User("admin3", new BCryptPasswordEncoder().encode("admin3"));

        userRepository.save(admin2);
        userRepository.save(admin3);

        storyServices.addExtract(testStory.getId(), extract, admin2);
        storyServices.addExtract(testStory.getId(), extract2, admin3);

        assertFalse(storyServices.spaceAvailable(testStory.getId()));


    }

    @Test
    @Transactional
    public void cannotAddToClosedStory() throws Exception {
        // test story to like.
        Story testStory = new Story("Story1", "Intro1", Category.Adventure);
        storyServices.createStory(testStory, admin, null);

        // two extract
        StoryExtract extract = new StoryExtract("content1", testStory);
        StoryExtract extract2 = new StoryExtract("content2", testStory);
        StoryExtract extract3 = new StoryExtract("content3", testStory);

        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));
        User admin3 = new User("admin3", new BCryptPasswordEncoder().encode("admin3"));
        User admin4 = new User("admin4", new BCryptPasswordEncoder().encode("admin4"));


        userRepository.save(admin2);
        userRepository.save(admin3);
        userRepository.save(admin4);

        storyServices.addExtract(testStory.getId(), extract, admin2);
        storyServices.addExtract(testStory.getId(), extract2, admin3);


        assertFalse(storyServices.addExtract(testStory.getId(), extract3, admin4));


    }

    // testing branching for stories

    @Test
    @Transactional
    public void validBranch() throws Exception {
        // test story object
        Story testStory = new Story("rootStory", "rootIntro", Category.Romance);
        storyServices.createStory(testStory, admin, null);

        // Extract already in the story
        StoryExtract extract = new StoryExtract("First Extract", testStory);


        // user creating the extract
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));

        // save user and extract
        userRepository.save(admin2);
        storyServices.addExtract(testStory.getId(), extract, admin2);

        // perform post with an extract to add
        ResultActions result = mockMvc.perform(get("/story/branch/")
                .param("id", String.valueOf(testStory.getId()))
                .param("branchPoint", String.valueOf(1))
                .flashAttr("StoryExtract", new StoryExtract())
                .param("content", "Branched Extract")
                .principal(mockPrincipal)
                .with(csrf()));


        // story that will be shown is the branched stories id.
        result.andExpect(redirectedUrl("/story/display?id=" + testStory.getBranches().get(0).getId()));


    }


    /**
     * An Invalid branch is one where the branching user attempt to branch from a point by using an extract that exists already.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void notValidBranch() throws Exception {
        // story branching from
        Story root = new Story("root", "root", Category.Romance);
        storyServices.createStory(root, admin, null);

        // another user who has contributed
        User admin2 = new User("admin2", new BCryptPasswordEncoder().encode("admin2"));
        userRepository.save(admin2);

        // Extract to add.
        StoryExtract extract = new StoryExtract("First Extract", null);

        // save the extract to the story
        storyServices.addExtract(root.getId(), extract, admin2);

        // attempt to branch with same content as above extract
        ResultActions result = mockMvc.perform(get("/story/branch/")
                .param("id", String.valueOf(root.getId()))
                .param("branchPoint", String.valueOf(1))
                .flashAttr("StoryExtract", new StoryExtract())
                .param("content", "First Extract")
                .principal(mockPrincipal)
                .with(csrf()));

        // redirected to the same page user attempted to branch from.
        result.andExpect(redirectedUrl("/story/display?id=" + root.getId()));

    }
}
