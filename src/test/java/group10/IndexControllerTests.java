package group10;

import group10.controllers.IndexController;
import group10.persistence.StoryRepository;
import group10.persistence.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
// disable security as all methods handled by this controller are permitted to all.

@RunWith(SpringRunner.class)
@WebMvcTest(value = IndexController.class, secure = false)
public class IndexControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoryRepository storyRepository;

    @MockBean
    private UserRepository userRepository;
    @Test
    public void mainLandingPageShows() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }
}
