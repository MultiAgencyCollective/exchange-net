import models.Project;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.Blob;
import play.test.Fixtures;
import play.test.UnitTest;

public final class BasicTest extends UnitTest {
    
    @Before
    public static void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public static void createAndRetrieveProject() {
        new Project(
            "My Title", 
            "My Name",
            new Blob(),
            "This is my description.", 
            1950,
            "foo, bar, baz", 
            "Nicholas Cage, Rihanna", 
            "plants, rocks", 
            "someEmail@email.com",
            "My Name",
            "read my email"
        ).save();
        Project someProject = 
            Project.find("byDescription", "This is my description.").first();
        assertNotNull(someProject);
        assertEquals("plants, rocks", someProject.otherInspirations);
    }

}
