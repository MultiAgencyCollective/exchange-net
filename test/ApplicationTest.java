import org.junit.Test;

import play.mvc.Http.Response;
import play.test.FunctionalTest;

public final class ApplicationTest extends FunctionalTest {

    @Test
    public static void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    
}