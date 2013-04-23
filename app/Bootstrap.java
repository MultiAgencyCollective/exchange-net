import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
                    randomWord(), // projectTitle
                    randomWord(), // artist
                    imageBlob, // myImage
                    randomWord(), // description
                    randomWordCommaList(), // tags
                    randomWordCommaList(), // livingInspirations
                    randomWordCommaList(), // pastInspirations
                    randomWordCommaList(), // nonArtistInspirations
                    randomWordCommaList(), // emails
                    randomWord() // message
                );
                
                project.save();
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
    
    private String randomWordCommaList() {
        final int maxWords = 5;
        final int wordCount = 1 + (int) (Math.random() * maxWords);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            builder.append(randomWord());
            if (i < wordCount - 1) {
                builder.append(", ");
            }
        }
        
        return builder.toString();
    }
    
    private String randomWord() {
        String[] words = {"foo", "bar", "baz", "bat", "qux", "quux"};
        return words[(int) (Math.random() * words.length)];
    }
}