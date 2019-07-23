package group10;

import group10.controllers.BrowseController;
import group10.domain.Category;
import group10.domain.Story;
import group10.persistence.StoryRepository;
import group10.persistence.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// disable security as all methods handled by this controller are permitted to all.
@RunWith(SpringRunner.class)
@WebMvcTest(value = BrowseController.class, secure = false)
public class BrowseControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private StoryRepository storyRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void explorePageShown() throws Exception {
        mockMvc.perform(get("/explore")).andExpect(status().isOk()).andExpect(view().name("explore"));
    }

    @Test
    public void defaultTitleShown() throws Exception {
        mockMvc.perform(get("/explore")).andExpect(status().isOk()).andExpect(view().name("explore")).andExpect(model().attribute("title", "All"));
    }

    @Test
    public void titleChangeWIthParam() throws Exception {
        mockMvc.perform(get("/explore").param("filter", "Latest")).andExpect(status().isOk()).andExpect(view().name("explore")).andExpect(model().attribute("title", "Latest"));
    }


    /**
     * Test that explore page is returned when the id passed in as param is not found in db.
     *
     * @throws Exception
     */

    @Test
    public void branchBadId() throws Exception {
        ResultActions result = mockMvc.perform(get("/viewBranches/").param("id", String.valueOf(0L)));

        // expect a redirect to the explore page as the story does not exist with the given id.
        result.andExpect(redirectedUrl("/explore"));

    }

    /**
     * Test that no stories are shown when the story has no branches.
     *
     * @throws Exception
     */
    @Test
    public void noBranchesStory() throws Exception {
        // set up the object
        Story testStory = new Story("test1", "Test1", Category.Adventure);
        testStory.setId(0L);

        // mock with mockito
        Mockito.when(storyRepository.getOne(0L)).thenReturn(testStory);

        // perform request
        ResultActions result = mockMvc.perform(get("/viewBranches/").param("id", String.valueOf(0L)));

        // storyList will be null as no branches to the story.
        result.andExpect(model().attribute("storyList", is(empty())));

        // lead text will include title of the story clicked from.
        result.andExpect(model().attribute("leadText", is("All branches for test1")));

        // expect the /explore page.
        result.andExpect(view().name("/explore"));
    }

    @Test
    public void viewStoryWithBranches() throws Exception {
        // root level story
        Story rootStory = new Story("rootStory", "RootIntro", Category.Adventure);
        rootStory.setId(0L);

        // branch story
        Story branchedStory = new Story(rootStory.getTitle(), rootStory.getIntroduction(), rootStory.getCategory());

        // set the relevant fields
        rootStory.addBranch(branchedStory);
        branchedStory.setParent(rootStory);

        // mock response from repository
        Mockito.when(storyRepository.getOne(0L)).thenReturn(rootStory);
        Mockito.when(storyRepository.findAllByParentOrderByLikeCountDesc(rootStory)).thenReturn(rootStory.getBranches());

        // perfom the view branches request
        ResultActions result = mockMvc.perform(get("/viewBranches/").param("id", String.valueOf(0L)));

        // model attribute will not be empty
        result.andExpect(model().attribute("storyList", is(not(empty()))));

        // lead text will include title of the story clicked from the root story.
        result.andExpect(model().attribute("leadText", is("All branches for rootStory")));

        // expect the /explore page.
        result.andExpect(view().name("/explore"));
    }
}
