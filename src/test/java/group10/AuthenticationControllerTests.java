package group10;

import group10.domain.User;
import group10.persistence.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTests {
    @Autowired
    private UserRepository repository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private User admin;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        if(repository.findAll().isEmpty()){
            admin = new User("admin", new BCryptPasswordEncoder().encode("admin"));
            repository.save(admin);
        }
    }

    @Test
    public void loginAvailableForAll() throws Exception {
        mockMvc
                .perform(get("/user-login"))
                .andExpect(status().isOk());
    }


     @Test
    public void adminCanLogin() throws Exception {
        mockMvc
                .perform(formLogin("/login").user("admin").password("admin"))
                .andExpect(authenticated().withUsername("admin"))
                .andExpect(redirectedUrl("/success-login"));
    }

    @Test
     public void adminCanLogout() throws Exception {
        mockMvc
                .perform(formLogin("/login").user("admin").password("admin"))
                .andExpect(authenticated().withUsername("admin"))
                .andExpect(redirectedUrl("/success-login"));


        mockMvc
                .perform(logout("/logout"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/user-logout"));
    }

    /**
     * Attempting to login with unregistered credentials causes the url to redirect to the same login page.
     *
     * @throws Exception
     */
    @Test
    public void notRegistered() throws Exception {
        mockMvc
                .perform(formLogin("/login").user("name").password("password"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/error-login"));
    }

    // get tests for jacoco coverage as above operations do not count??

    @Test
    public void logoutPage() throws Exception {
        mockMvc.perform(get("/user-logout")).andExpect(status().isOk()).andExpect(forwardedUrl("/user-login"));
    }

    @Test
    public void errorPage() throws Exception {
        mockMvc.perform(get("/error-login")).andExpect(status().isOk()).andExpect(forwardedUrl("/user-login")).andExpect(model().attributeExists("errorMessage"));
    }

    //
    @Test
    public void successPage() throws Exception {
        mockMvc.perform(get("/success-login")).andExpect(status().isOk()).andExpect(view().name("index"));
    }


}
