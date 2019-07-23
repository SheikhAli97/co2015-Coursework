package group10.controllers;

import group10.domain.Category;
import group10.domain.Story;
import group10.domain.StoryExtract;
import group10.domain.User;
import group10.persistence.StoryRepository;
import group10.services.StoryServices;
import group10.services.UserServices;
import group10.utility.StoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Simple Controller to handle all Functionality to do with Story.
 */

@Controller
@RequestMapping("/story")
public class StoryController {

    /**
     * Autowires an instance of StoryRepository to be used by the validator.
     */

    @Autowired
    private StoryRepository storyRepository;

    /**
     * The service responsible for operations related to story.
     */
    @Autowired
    private StoryServices storyServices;

    /**
     * The service responsible for operations related to user.
     */
    @Autowired
    private UserServices userServices;


    /**
     * Used to populate the WebDataBinder which will be used to populate/valida form objects.
     *
     * @param binder The WebDataBinder
     */

    @InitBinder("Story")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new StoryValidator(storyRepository));
    }

    /**
     * Displays a view to handle Story Creation.
     *
     * @param model The view model.
     * @return The Story Creation view.
     */

    @RequestMapping("/new")
    public String index(Model model) {
        model.addAttribute("categories", Category.values());
        return "storyCreation";
    }

    /**
     * Saves the ModelAttribute Story Object from the post request.
     *
     * @param story     The Story Object to add.
     * @param result    The Outcome of the object validation.
     * @param model     The view model.
     * @param f         - The file to upload if any.
     * @param principal - Represents the logged in user who is making this request.
     * @return Redirects to the list of all Stories with the passed in Story Added.
     */

    @RequestMapping(value = "/create/", method = RequestMethod.POST)
    public String addStory(@Valid @ModelAttribute("Story") Story story, BindingResult result,
                           @RequestParam(value = "file", required = false) MultipartFile f, Principal principal,
                           Model model) {

        if (result.hasErrors()) {
            model.addAttribute("Story", story);
            return "forward:/story/new";
        }

        User loggedInUser = userServices.fetchUserByName(principal.getName());
        storyServices.createStory(story, loggedInUser, f);
        return "redirect:/explore";
    }

    /**
     * Displays the contents of a given story.
     *
     * @param storyId   - THe id of the story to display.
     * @param model     - THe view model to add the object into.
     * @param principal - Representation the logged in user who is making this request, can be null.
     * @return - Story Display view.
     */
    @RequestMapping("/display")
    public String displayStory(@RequestParam(value = "id") long storyId, Model model, Principal principal) {


        if (principal != null) {
            User u = userServices.fetchUserByName(principal.getName());
            // story service will return true if the user has not contributed before, false otherwise.
            model.addAttribute("canContribute", storyServices.validUserToContribute(storyId, u));
            // story service will return true if the user is the author of the current story.
            model.addAttribute("isAuthor", storyServices.isAuthor(storyId, u));
            // check if user has liked the story
            model.addAttribute("likedAlready", userServices.isLikedByUser(u, storyRepository.getOne(storyId)));
        } else {
            // all unregistered users by default can contribute (attempting to do so forces them to login first)
            model.addAttribute("canContribute", true);
            model.addAttribute("isAuthor", false);
            model.addAttribute("likedAlready", false);
        }
        model.addAttribute("story", storyRepository.getOne(storyId));
        return "storyDetails";
    }

    /**
     * Adds the Extract to the List of Contributions.
     *
     * @param storyExtract - The extract to add.
     * @param storyId      - The id of the story to add the extract to.
     * @param principal    - Representation the logged in user who is making this request.
     * @param redirect     - Redirects to /display with error message.
     * @return - story display with newly added extract if successful.
     */

    @RequestMapping(value = "/addContribution/", method = RequestMethod.POST)
    public String addContribution(@ModelAttribute StoryExtract storyExtract, @RequestParam(value = "id") long storyId, Principal principal, RedirectAttributes redirect) {

        String view = "redirect:/story/display?id=" + storyId;

        // use validator instead of this probably.
        if (storyServices.contentExistsInStory(storyId, storyExtract.getContent())) {
            redirect.addFlashAttribute("existingContent", storyExtract.getContent());
            redirect.addFlashAttribute("errorMessage", "Content already exists");
            return view;
        }
        User loggedInUser = userServices.fetchUserByName(principal.getName());
        storyServices.addExtract(storyId, storyExtract, loggedInUser);
        return view;
    }

    /**
     * Perfoms a like or unlike operation by adding/removing stories from the users collection of liked stories.
     *
     * @param storyId   - The storyId to increment/decrement like and add to users collection of liked stories.
     * @param action    - The action to perform - Like/unlike.
     * @param principal - Representation the logged in user who is making this request.
     * @return - story display with like counter modified accordingly.
     */
    @RequestMapping("/like/")
    private String likeStory(@RequestParam(value = "id") long storyId, @RequestParam(value = "action") String action, Principal principal) {
        Story currentStory = storyRepository.getOne(storyId);
        if (action.equals("like")) {
            // handle like logic.
            userServices.likeStory(principal.getName(), currentStory);
        } else {
            // handle unlike logic.
            userServices.unlikeStory(principal.getName(), currentStory);
        }
        return "redirect:/story/display?id=" + storyId;
    }


    /**
     * Branches a story with given id from a particular point int its extract list. It then adds the extract passed in to the new list.
     *
     * @param storyId   - The storyid of the Object to branch from.
     * @param point     - THe point in the extract list to branch from.
     * @param extract   - THe extract to add after branching
     * @param model     - The model to add errors to if any.
     * @param principal - The authenticated user making the request.
     * @return - Displays the branched story if successful otherwise redirect to the same page.
     */
    @RequestMapping("/branch/")
    public String branchStory(@RequestParam(value = "id") long storyId,
                              @RequestParam(value = "branchPoint") String point, @ModelAttribute StoryExtract extract,
                              Model model, Principal principal) {
        User branchAuthor = userServices.fetchUserByName(principal.getName());
        long result = storyServices.branchStory(storyId, Integer.valueOf(point), extract, branchAuthor);

        // the user is trying to branch and add content that already existed in the story.
        if (result == -1L) {
            model.addAttribute("modalError", true);
            // redirect to the same page the user was on.
            return "redirect:/story/display?id=" + storyId;
        }

        // redirect to the branched story if you get here
        return "redirect:/story/display?id=" + result;

    }

}
