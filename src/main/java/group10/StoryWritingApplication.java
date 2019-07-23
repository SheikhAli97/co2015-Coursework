package group10;

import group10.persistence.StoryRepository;
import group10.utility.MainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoryWritingApplication implements CommandLineRunner {

    private static MainStore store;

    public static MainStore getStore() {
        return store;
    }

    public static void main(String[] args) {
        SpringApplication.run(StoryWritingApplication.class, args);
    }

    // @Autowired
    // private StoryRepository storyRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialise the List for our View to Utilise.
        store = new MainStore();
        StoryWritingApplication.getStore().init();
        // uncomment the following line to populate db with some values
        // use only when connected to local sql server.
        // storyRepository.saveAll(store.getStoryDomain());
    }
}

