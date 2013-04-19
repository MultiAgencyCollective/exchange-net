import java.util.ArrayList;

import models.Project;
import play.db.jpa.Blob;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
	public void doJob() {

	    //Check if the database is empty
        if(Project.count() == 0) {
        	System.err.println("REMOVING ALL DATABASE ENTRIES....");
        	Fixtures.deleteDatabase();
        	System.err.println("About to create and add new seed data to database");
        
        	ArrayList<Long> ids = new ArrayList<Long>();
        	
        	for (int i = 0; i< 20; i++) {
        		Project project = new Project(
    		        randomWord(), 
    		        randomWord(), 
    		        new Blob(), 
    		        randomWord(), 
    		        randomWord(), 
    		        randomWord(), 
    		        randomWord(), 
    		        randomWord(), 
    		        randomWord(), 
    		        randomWord()
		        );
        		project.save();
        		ids.add(project.id);
        	}
        }
    }
	
	public String randomWord() {
	    String[] words = {"foo", "bar", "baz", "bat", "qux", "quux"};
	    return words[(int) (Math.random() * words.length)];
	}
}