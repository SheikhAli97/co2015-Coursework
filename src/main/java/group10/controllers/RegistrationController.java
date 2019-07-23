package group10.controllers;

import group10.domain.User;
import group10.persistence.UserRepository;
import group10.utility.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Responsible for handling User Registration.
 */
@Controller
public class RegistrationController {

    /**
     * Autowires an instance of UserRepository to be used to perform db functions.
     */

    @Autowired
    private UserRepository userRepository;

    /**
     * Used to populate the WebDataBinder which will be used to populate/validate user objects.
     *
     * @param binder The WebDataBinder
     */

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(new UserValidator(this.userRepository));
    }

    /**
     * Displays the registration page for the user.
     *
     * @return - The registration view.
     */
    @RequestMapping("/user-registerPage")
    public String registerPage() {
        return "registerPage";
    }


    /**
     * Creates and Saves the User if no errors occur.
     *
     * @param user   - The submitted user form.
     * @param result - The result from the validation.
     * @param model  - The model attribute.
     * @return - LoginPage if registration is success else back to register page.
     */
    @RequestMapping(value = "/user-create", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute("User") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("User", user);
            return "forward:/user-registerPage";
        }

        // encrypt password first
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        // now save to the db
        userRepository.save(user);
        // Todo: redirect to dashboard.
        return "redirect:/user-login";
    }

}
