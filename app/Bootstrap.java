import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import models.Project;
import play.db.jpa.Blob;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.MimeTypes;
import play.test.Fixtures;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
	public final void doJob() {

	    //Check if the database is empty
        if (Project.count() == 0) {
        	System.err.println("REMOVING ALL DATABASE ENTRIES....");
        	Fixtures.deleteDatabase();
        	System.err.println(
    	        "About to create and add new seed data to database"
	        );
        
        	ArrayList<Long> ids = new ArrayList<Long>();
        	
        	final int numberOfRecords = 20;
        	for (int i = 0; i < numberOfRecords; i++) {
        	    final Blob imageBlob = new Blob();
        	    final File imageFile = randomImageFile();
        	    try {
                    imageBlob.set(
                        new FileInputStream(imageFile), 
                        MimeTypes.getContentType(imageFile.getName())
                    );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        		Project project = new Project(
    		        randomWord(), 
    		        randomWord(), 
    		        imageBlob, 
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
	
	private File randomImageFile() {
	    final String folder = 
            "/Users/masonwright/dropbox/photos/badlands trip/";
	    final String[] imageFiles = {
	            "95970001.jpg", 
	            "95970005.jpg", 
	            "95970009.jpg", 
	            "95970002.jpg", 
	            "95970006.jpg"
            };
	    final String choice = 
            imageFiles[(int) (Math.random() * imageFiles.length)];
	    return new File(folder + choice);
	}
	
	private String randomWord() {
	    String[] words = {"foo", "bar", "baz", "bat", "qux", "quux"};
	    return words[(int) (Math.random() * words.length)];
	}
}