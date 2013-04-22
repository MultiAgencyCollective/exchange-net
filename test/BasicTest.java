import models.Project;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.Blob;
import play.test.Fixtures;
import play.test.UnitTest;

public final class BasicTest extends UnitTest {
    
    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveProject() {
        new Project(
            "My Title", 
            "My Name",
            new Blob(), 
            "This is my description.", 
            "foo, bar, baz", 
            "Nicholas Cage, Rihanna", 
            "M. C. Escher, Rube Goldberg", 
            "plants, rocks", 
            "someEmail@email.com", 
            "read my email"
        ).save();
        Project someProject = 
            Project.find("byDescription", "This is my description.").first();
        assertNotNull(someProject);
        assertEquals("plants, rocks", someProject.nonArtistInspirations);
    }

}
