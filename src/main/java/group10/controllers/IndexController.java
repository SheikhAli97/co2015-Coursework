package group10.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that maps to main landing page.
 */

@Controller
public class IndexController {

    /**
     * Maps to localhost:port/ and displays landing page.
     *
     * @param value The tab Id to select.
     * @param model The model to add the ID to.
     * @return The view page to render 'index.jsp'
     */
    @RequestMapping("/")
    public String index(@RequestParam(value = "tId", defaultValue = "latest") String value, Model model) {

        model.addAttribute("tab_id", value);
        return "index";
    }

}
