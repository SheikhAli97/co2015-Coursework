package group10.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles the mappings for user login's.
 */
@Controller
public class AuthenticationController {

    /**
     * Displays the login page for the user.
     *
     * @return The loginPage jsp
     */
    @RequestMapping("/user-login")
    public String loginPage() {
        return "loginPage";
    }


    /**
     * Displays the main home page upon user logging successfully.
     *
     * @return - The landing page. (Will be changed to dashboard).
     */

    @RequestMapping("/success-login")
    public String successLogin() {
        // redirect to dashboard upon login.
        return "index";
    }

    /**
     * Displays the login form with an error message if login attempt is not successful.
     *
     * @param model - The model to add the error attribute to.
     * @return - The login page.
     */

    @RequestMapping("/error-login")
    public String error(Model model) {
        model.addAttribute("errorMessage", "Incorrect Username or password");
        return "forward:/user-login";
    }

    /**
     * Displays the login form after the user logs out.
     *
     * @param model - The model attribute to add the logout attribute to.
     * @return - The login page.
     */

    @RequestMapping("/user-logout")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return "forward:/user-login";
    }
}
