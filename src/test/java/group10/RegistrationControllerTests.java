package group10;

import group10.domain.User;
import group10.persistence.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(secure = false) // testing functionality only as there's no security for registration.
@SpringBootTest
public class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    /**
     * Delete all entries in the User Repository before each test.
     */

    @Before
    public void nukeRepository() {
        repository.deleteAllInBatch();
    }


    // register page shown

    @Test
    public void registerPageShown() throws Exception {
        mockMvc.perform(get("/user-registerPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerPage"));
    }

    // Test creation of user start ========
    @Test
    public void createUser_FullFields() throws Exception {
        mockMvc.perform(post("/user-create").flashAttr("User", new User()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "name1")
                .param("password", "password1")
                .with(csrf()))
                .andExpect(redirectedUrl("/user-login"))
                .andDo(print());

        // ensure user is saved to the database
        assertThat(repository.findAll().size(), is(1));

        // assert saved fields are correct
        assertThat(repository.findAll().get(0).getUsername(), is("name1"));
    }

    @Test
    public void createExistingUser() throws Exception {
        // ensure db is empty first
        assertThat(repository.findAll().size(), is(0));


        User u = new User();
        u.setUsername("user1");
        u.setPassword("pass1");
        // save a new user
        repository.save(u);

        // check save is successful
        assertThat(repository.findAll().size(), is(1));

        // attempt to create a user with above credentials through form
        mockMvc.perform(post("/user-create").flashAttr("User", new User()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user1")
                .param("password", "pass1")
                .with(csrf()))
                .andExpect(forwardedUrl("/user-registerPage"))
                .andExpect(model().attributeHasFieldErrors("User", "username")) // model will contain error pointing to username
                .andDo(print());
    }
}
