package group10.controllers;

import group10.domain.Story;
import group10.persistence.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller responsible for handling browsing mechanism.
 */
@Controller
public class BrowseController {

    /**
     * Autowires an instance of StoryRepository to be used to perform db functions.
     */
    @Autowired
    private StoryRepository storyRepository;

    /**
     * @param filter The selected filter as a String.
     * @param model  Supplies attributes used to render our JSP
     * @return The 'Explore.jsp'
     */
    @RequestMapping("/explore")
    public String index(@RequestParam(defaultValue = "All") String filter, Model model) {
        model.addAttribute("storyList", storyRepository.findAllByParentIsNull());
        model.addAttribute("title", filter);
        model.addAttribute("leadText", "With the right book, every reader would be a flame");

        return "explore";
    }

    /**
     * View All stories where the root branch is the story with the passed in story id.
     *
     * @param storyId - THe story id to  match against.
     * @param model   - The model to add the attributed to.
     * @return - The explore page showing all stories where the passed in story is the root object.
     */
    @RequestMapping("/viewBranches/")
    public String viewBranches(@RequestParam(value = "id") long storyId, Model model) {
        Story story = storyRepository.getOne(storyId);
        if (story == null) {
            // show all top level stories if story with id cannot be found.
            return "redirect:/explore";
        }
        model.addAttribute("storyList", storyRepository.findAllByParentOrderByLikeCountDesc(story));
        model.addAttribute("title", "Branched ");
        model.addAttribute("leadText", "All branches for " + story.getTitle());
        return "/explore";
    }


}
