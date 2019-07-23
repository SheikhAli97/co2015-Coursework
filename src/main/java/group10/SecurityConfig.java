package group10;

import group10.services.CustomUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Java Class to securely manage the
 * registration and logging in of users.
 */

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configure the security for users which may eventually have privileges.
     *
     * @param http The HTTP Security configuration.
     * @throws Exception Handling of exceptions.
     */
    protected void configure(HttpSecurity http) throws Exception {
        // access for all
        http
                .authorizeRequests()
                .antMatchers("/", "/explore/**", "/user/register", "/resources/**", "/story/display")
                .permitAll();

        // form login
        http
                .formLogin()
                .loginPage("/user-login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/success-login")
                .failureUrl("/error-login")
                .permitAll();

        // form logout
        http
                .logout()
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/user-logout")
                .permitAll();

        // access for only authenticated users
        http
                .authorizeRequests()
                .antMatchers("/story/new", "/story/create", "/story/addContribution/", "/story/like/", "/story/branch/").authenticated();


        // exceptions
        http.exceptionHandling().accessDeniedPage("/access-denied");
    }

    /**
     * Wires the userDetailsService in which will process user details.
     */
    @Autowired
    private CustomUserService userService;


    /**
     * Configuration for the encryption of passwords.
     *
     * @param auth Manages authentication.
     * @throws Exception Handles exceptions.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        auth.userDetailsService(userService).passwordEncoder(pe);

    }

}
