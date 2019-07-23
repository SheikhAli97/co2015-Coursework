package group10.utility;

import group10.domain.Category;
import group10.domain.Story;
import group10.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The MainStore class stores all default and current stories as a placeholder
 * until SQL Server is implemented, and/or to implement sorting methods etc.
 *
 * @author ben
 */

public class MainStore {

    /**
     * Story Store didn't look right as a variable name.
     */

    private List<Story> storyDomain;

    /**
     * Initialise default story values.
     */
    public MainStore() {
        storyDomain = new ArrayList<>();
    }

    /**
     * Fills List with random Stories to use.
     */
    public void init() {
        Story story = new Story( "Fifty Shade of Blood", "To Live is to Bleed, to die is to eat.", Category.Action);
        Story story2 = new Story( "Alarmed To Be In Love", "I think i might fall in love with you", Category.Comedy);
        Story story3 = new Story( "In The Beginning", "There was a boom, followed by more booms.", Category.Romance);
        Story story4 = new Story( "What's your favourite word?", "Everyone knows its a penguin!", Category.Adventure);
        Story story5 = new Story( "Long Live the King", "He Was my everything, I Tried to Tell them", Category.Fantasy);
        Story story6 = new Story( "A Bottle of Strawberries", "Today is a strawberry type of day", Category.Scifi);
        Story story7 = new Story( "Dark Side of the Moon", "Do you ever wonder, what such a ...", Category.Satire);
        Story story8 = new Story( "The girl Who Had Lightning", "Some people were meant to burn.", Category.Mystery);
        Story story9 = new Story( "Dreams Afar", "Humans are dark and filthy creatures.", Category.Other);
        storyDomain.addAll(Arrays.asList(story, story2, story3, story4, story5, story6, story7, story8, story9));
        storyDomain.forEach(s -> s.setAuthor(new User("admin",new BCryptPasswordEncoder().encode("admin"))));
    }

    public List<Story> getStoryDomain() {
        return this.storyDomain;
    }
}
