package group10;

import group10.domain.Category;
import group10.domain.Story;
import group10.domain.User;
import group10.persistence.StoryRepository;
import group10.persistence.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StoryRepositoryTests {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    private User admin;


    @Before
    public void setAdmin() {
        admin = new User("admin", new BCryptPasswordEncoder().encode("admin"));
        userRepository.save(admin);
    }

    @Test
    public void saveTest() {
        // fresh new Story.
        Story s1 = new Story("Title1", "Description1", Category.Adventure);

        // need to set author now
        s1.setAuthor(admin);
        // Save to database.
        storyRepository.save(s1);

        // check it is saved successfully.
        assertThat(storyRepository.findAll().size(), is(1));

        // fetch object from database.
        Story fromDb = storyRepository.findAll().get(0);

        // ensure each field is the same.
        // id is auto-generated so no need to test it.
        assertThat(fromDb.getTitle(), equalTo(s1.getTitle()));
        assertThat(fromDb.getIntroduction(), equalTo(s1.getIntroduction()));
        assertThat(fromDb.getCategory(), equalTo(s1.getCategory()));
    }

    @Test
    public void findByTitleAndDescription() {
        // List of Story objects with random titles.
        List<Story> stories = getTestList();

        // check storyRepo is empty first
        assertThat(storyRepository.findAll().size(), is(0));

        // save all to the database.
        storyRepository.saveAll(stories);

        // check they are successfully saved
        assertThat(storyRepository.findAll().size(), is(4));

        // search for an Existing title and description
        Optional<Story> optionalStory = storyRepository.findByTitleAndIntroduction("1", "1");

        // it will be present
        assertTrue(optionalStory.isPresent());

        // get the object
        Story fromDb = optionalStory.get();

        // match fields to known values.
        assertThat(fromDb.getTitle(), is("1"));
        assertThat(fromDb.getIntroduction(), is("1"));
        assertThat(fromDb.getAuthor().getUsername(), is("admin"));

    }

    @Test
    public void findByTitle() {
        // List of Story objects with random titles.
        List<Story> stories = getTestList();

        // check storyRepo is empty first
        assertThat(storyRepository.findAll().size(), is(0));

        // save all to the database.
        storyRepository.saveAll(stories);

        // check they are successfully saved
        assertThat(storyRepository.findAll().size(), is(4));

        // search for an Existing title.
        Optional<Story> story = storyRepository.findByTitle("1");

        // object will be present.
        assertTrue(story.isPresent());

        // fetch the object
        Story fromDb = story.get();

        // match the title field
        assertThat(fromDb.getTitle(), is("1"));

        // match the author field
        assertThat(fromDb.getAuthor().getUsername(), is("admin"));
    }

    @Test
    public void findByTitleNotPresent() {
        // List of Story objects with random titles.
        List<Story> stories = getTestList();

        // check storyRepo is empty first
        assertThat(storyRepository.findAll().size(), is(0));

        // save all to the database.
        storyRepository.saveAll(stories);

        // check they are successfully saved
        assertThat(storyRepository.findAll().size(), is(4));

        // search for an Existing title.
        Optional<Story> story = storyRepository.findByTitle("10");

        // object will not be present.
        assertFalse(story.isPresent());
    }

    private List<Story> getTestList(){
        List<Story> list = Arrays.asList(new Story("1", "1", Category.Other), new Story("2", "2", Category.Other), new Story("3", "3", Category.Other), new Story("4", "5", Category.Other));
        list.forEach(s -> s.setAuthor(admin));
        return list;
    }


}
