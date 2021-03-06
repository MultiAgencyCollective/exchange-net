import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import models.Project;
import play.db.jpa.Blob;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.MimeTypes;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public static final Random MY_RANDOM = new Random();
    private static final String EMAIL = "abcde@gmail.com";
    
    @Override
    public final void doJob() {   
        // populateProjectDatabase();
    }
    
    private static boolean isNameTaken(final String name) {
        List<Project> projects = Project.findAll();
        for (Project project: projects) {
            if (project.projectTitle.equals(name)) {
                return true;
            }
        }
        
        return false;
    }
    
    @SuppressWarnings("unused")
    private static void populateProjectDatabase() {
        final int numberOfRecords = 10; 
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
            
            final int titleMaxChars = 30;
            String title = randomWords(titleMaxChars);
            while (isNameTaken(title)) {
                title = randomWords(titleMaxChars);
            }
            
            Project project = randomLengthProject(title, imageBlob);
            // Project project = maxLengthProject(title, imageBlob);
            project.initializeImage();

            project.save();
        }
    }
    
    private static String randomURL() {
        String[] urls = {
            "www.google.com", 
            "www.yahoo.com", 
            "www.msn.com", 
            "www.whitehouse.gov"
        };
        return urls[(int) (Math.random() * urls.length)];
    }
    
    
    @SuppressWarnings("unused")
    private static Project maxLengthProject(
        final String title, 
        final Blob imageBlob
    ) {
        final int shortMaxChars = 100;
        final int longMaxChars = 5000;
        
        Project result = new Project(
            title, // projectTitle
            randomWordsList(shortMaxChars), // artists
            imageBlob, // myImage
            randomWords(longMaxChars), // description
            randomYear(), // year
            randomWordsList(shortMaxChars), // tags
            randomWordsList(shortMaxChars), // peers
            randomWordsList(shortMaxChars), // otherInspirations
            randomURL(), // url
            randomWordsList(shortMaxChars), // emails
            randomName(), // sender
            randomWord(), // message
            EMAIL // creatorEmail
        );
        result.isTest = true;
        return result;
    }
    
    private static Project randomLengthProject(
        final String title, 
        final Blob imageBlob
    ) {
        Project result = new Project(
            title, // projectTitle
            randomWordCommaList(), // artists
            imageBlob, // myImage
            randomDescription(), // description
            randomYear(), // year
            randomTagCommaList(), // tags
            randomWordCommaList(), // peers
            randomWordCommaList(), // otherInspirations
            randomURL(), // url
            randomWordCommaList(), // emails
            randomName(), // sender
            randomWord(), // message
            EMAIL // creatorEmail
        );
        
        result.isTest = true;
        return result;
    }
    
    /*
    private static void addTestProject() {
        final Blob imageBlob = new Blob();
        final File imageFile =  
            new File(
                "/Users/masonwright/dropbox/photos/badlands trip/95970001.jpg"
            );
        try {
            imageBlob.set(
                new FileInputStream(imageFile), 
                MimeTypes.getContentType(imageFile.getName())
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final String testString = "exchange_db_test";
        Project project = new Project(
            testString, // projectTitle
            testString, // artist
            imageBlob, // myImage
            testString, // description
            randomYear(), // year
            testString, // tags
            testString, // peers
            testString, // otherInspirations
            randomURL(), // url
            testString, // emails
            testString, // sender
            testString, // message
            EMAIL // creatorEmail
        );
        project.isTest = true;
        
        project.save();
    }
    */
    
    
    private static File randomImageFile() {
        final String folder = 
            "public/images/";
        final String[] imageFiles = {
            "amelia.jpg",
            "caroline.jpg",
            "corey.jpg",
            "mason.jpeg",
            "pratim.png"
        };
        final String choice = 
            imageFiles[(int) (Math.random() * imageFiles.length)];
        return new File(folder + choice);
    }
    
    private static String randomTagWord() {
        String[] names = {
                "exchange", "barter", "network", "community",
                "commoning", "studio", "future", "workshop",
                "event", "sculpture", "class", "furniture",
                "research", "trade", "school", "engineering",
                "costume", "performance", "social"
            };
            return names[(int) (Math.random() * names.length)];
    }
    
    private static String randomTag() {
        final int maxWords = 3;
        final int wordCount = 1 + MY_RANDOM.nextInt(maxWords);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            builder.append(randomTagWord());
            if (i < wordCount - 1) {
                builder.append(" ");
            } 
        }
        
        return builder.toString();
    }
    
    private static String randomTagCommaList() {
        final int maxWords = 5;
        final int wordCount = 1 + MY_RANDOM.nextInt(maxWords);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            builder.append(randomTag());
            if (i < wordCount - 1) {
                builder.append(", ");
            } 
        }
        
        return builder.toString();
    }
    
    private static String randomWordCommaList() {
        final int maxWords = 5;
        final int wordCount = 1 + MY_RANDOM.nextInt(maxWords);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            builder.append(randomName());
            if (i < wordCount - 1) {
                builder.append(", ");
            } 
        }
        
        return builder.toString();
    }
    
    private static String randomDescription() {
        final int maxWords = 150;
        final int wordCount = 1 + MY_RANDOM.nextInt(maxWords);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            builder.append(randomWord());
            if (i < wordCount - 1) {
                builder.append(" ");
            } 
        }
        
        return builder.toString();
    }
    
    private static String randomName() {
        final int maxWords = 3;
        final int wordCount = 1 + MY_RANDOM.nextInt(maxWords);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            builder.append(randomWord());
            if (i < wordCount - 1) {
                builder.append(" ");
            } 
        }
        
        return builder.toString();
    }
    
    private static int randomYear() {
        final int minYear = 1900;
        final int yearRange = 120;
        return minYear + MY_RANDOM.nextInt(yearRange);
    }
    
    private static String randomWord() {
        String[] names = {"Muoi", "Bobbi", "Leoma", "Whitley", "Carolina", 
            "Sanora", "Helene", "Lakesha", "Patrick", "Ross", "Tawanda", 
            "Josiah", "Chandra", "Vivan", "Zachariah", "Margart", "Mariella", 
            "Lan", "Winifred", "Kira", "Tianna", "Lea", "Monty", "Ben", 
            "Galina", "Karlene", "Reagan", "Libby", "Signe", "Nina", 
            "Gregoria", "Sherrie", "Liana", "Margret", "Donita", "Crystle", 
            "Jamila", "Preston", "Josue", "Meggan", "Marcy", "Larry", 
            "Consuelo", "Beatriz", "Annabel", "Benito", "Amiee", "Jackelyn", 
            "Rodolfo", "Britni"
        };
        return names[(int) (Math.random() * names.length)];
    }
    
    private static String randomWords(final int maxChars) {
        StringBuilder builder = new StringBuilder();
        builder.append(randomWord());
        String nextString = randomWord();
        while (builder.length() + nextString.length() + 1 <= maxChars) {
            builder.append(" ");
            builder.append(nextString);
            nextString = randomWord();
        }
        
        return builder.toString();
    }
    
    private static String randomWordsList(final int maxChars) {
        StringBuilder builder = new StringBuilder();
        builder.append(randomName());
        String nextString = randomName();
        while (builder.length() + nextString.length() + 2 <= maxChars) {
            builder.append(", ");
            builder.append(nextString);
            nextString = randomName();
        }
        
        return builder.toString();
    }
}