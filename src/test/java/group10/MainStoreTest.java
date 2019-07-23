package group10;

import group10.domain.Category;
import group10.domain.Story;
import group10.utility.MainStore;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class MainStoreTest {

    @Test
    public void listIsEmpty() {
        MainStore store = new MainStore();
        assertTrue(store.getStoryDomain().isEmpty());
    }

    @Test
    public void itemAdded() {
        MainStore store = new MainStore();
        store.getStoryDomain().add(new Story("1","1", Category.Romance));
        assertThat(store.getStoryDomain(), hasSize(1));
    }

    @Test
    public void canGenerateDefaultStories() {
        MainStore store = new MainStore();
        store.init();
        assertThat(store.getStoryDomain(), hasSize(9));
    }
}
