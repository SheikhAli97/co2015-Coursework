package group10.utility;

import group10.domain.Story;
import group10.persistence.StoryRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Validator responsible for handling Story Object validations not catchable on the client side.
 */

public class StoryValidator implements Validator {

    /**
     * StoryRepository to
     */
    private StoryRepository storyRepository;

    /**
     * Initialises our storyRespository Field.
     *
     * @param storyRepo The storyRepostiry to assign.
     */

    public StoryValidator(StoryRepository storyRepo) {
        this.storyRepository = storyRepo;
    }

    /**
     * Checks wether the instance of the class passed as argument is able to be validated.
     *
     * @param clazz Class of the Object.
     * @return True if the class of the object is Story
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Story.class.equals(clazz);
    }

    /**
     * Validates a target objects.
     *
     * @param target The object to validate.
     * @param errors The field errors which are causing problems.
     */

    @Override
    public void validate(Object target, Errors errors) {
        Story other = (Story) target;
        // check if both title and description only exists
        Optional<Story> byTitleAndIntroduction = storyRepository.findByTitleAndIntroduction(other.getTitle(), other.getIntroduction());
        if (byTitleAndIntroduction.isPresent()) {
            // data already exists so an error
            errors.rejectValue("title", "", "Title Already Exists!");
            errors.rejectValue("introduction", "", "Introduction Already Exists!");
            return;
        }

        //check if only the title of the current story exists
        Optional<Story> storyWithSameTitle = storyRepository.findByTitle(other.getTitle());
        if (storyWithSameTitle.isPresent()) {
            errors.rejectValue("title", "", "Title Already Exists!");
        }
    }
}
