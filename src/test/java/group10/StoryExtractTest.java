package group10;

import group10.domain.Category;
import group10.domain.Story;
import group10.domain.StoryExtract;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * This class tests the StoryExtract class found under the domain Package.
 * Only Tests hashcode and equality as the rest of the methods are straightforward.
 * The naming pattern being used =>  MethodName_StateBeingTested_ExpectedBehavior
 */

public class StoryExtractTest {

    // HashCode Testing Start

    /**
     * Expected Behavior Of Hashcode: Two StoryExtracts have same hashcode value if and only if all their fields match.
     * Thus there should only be one testcase that returns true, the rest should return false.
     */

    private static Story story1;
    private static Story story2;

    @BeforeClass
    public static void setStories() {
        story1 = new Story("Title1", "Intro1", Category.Romance);
        story2 = new Story("Title2", "Intro2", Category.Adventure);
    }


    /**
     * Expected Behavior Of Hashcode: Two StoryExtracts have same hashcode value if and only if:
     * they belong to the same story and have same content
     * Thus there should only be one testcase that returns true, the rest should return false.
     */

    @Test
    public void hashcode_MatchingFields_Equal() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("first extract", story1);
        assertThat(s1.hashCode(), is(s2.hashCode()));
    }

    @Test
    public void hashcode_SameStoryDifferentContent_NotEqual() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("second extract", story1);
        assertThat(s1.hashCode(), is(not(s2.hashCode())));
    }

    @Test
    public void hashcode_DifferentStorySameContent_notEqual() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("first extract", story2);

        assertThat(s1.hashCode(), is(not(s2.hashCode())));
    }

    @Test
    public void hashcode_DifferentStoryDifferentContent_notEqual() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("second extract", story2);
        assertThat(s1.hashCode(), is(not(s2.hashCode())));
    }
    // HashCode Testing End

    // Equals Testing Start: same results as above.

    @Test
    public void equals_SameFields_Equal() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("first extract", story1);
        assertThat(s1, equalTo(s2));
    }

    @Test
    public void equals_SameStoryDifferentContent_NotEqual() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("second extract", story1);
        assertThat(s1, is(not(s2)));
    }

    @Test
    public void equals_DifferentStorySameContent_NotEqual() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("first extract", story2);
        assertThat(s1, is(not(s2)));
    }

    @Test
    public void equals_DifferentStoryDifferentContent_NotEqual() {
        StoryExtract s1 = new StoryExtract("first extract", story1);
        StoryExtract s2 = new StoryExtract("second extract", story2);
        assertThat(s1, is(not(s2)));
    }
}
