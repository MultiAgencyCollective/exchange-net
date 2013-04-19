package controllers;

import java.util.List;

import models.Project;
import play.mvc.Controller;

public class Application extends Controller {
    
    public static void data() {
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).from(1).fetch(100);
        render(projects);
    }
    
    public static void getPicture(long id) {
        Project project = Project.findById(id);
        if (project == null) {
        	System.err.println( "There was no project found with id = " + id); 
        }
        //TODO:  create an "image not found" image to serve in case there's none found.
        response.setContentTypeIfNotSet(project.myImage.type());
        renderBinary(project.myImage.get());
    
    }
    
    public static void index4() {
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).from(1).fetch(100);
        
        render(projects);
    }
    
    
    public static void addProject2(Project newProject) {
        if (validation.valid(newProject).ok) {
            newProject.initializeSets();
            newProject.save();
            flash.success("Thanks for posting");
            index4();
        } else {
            List<Project> projects = Project.find(
                "order by projectTitle asc"
            ).from(1).fetch(100);
            flash.clear();
            flash.put("projectTitle", newProject.projectTitle);
            flash.put("artist", newProject.artist);
            flash.put("myImage", newProject.myImage);
            flash.put("description", newProject.description);
            flash.put("tags", newProject.tags);
            flash.put("livingInspirations", newProject.livingInspirations);
            flash.put("pastInspirations", newProject.pastInspirations);
            flash.put("nonArtistInspirations", newProject.nonArtistInspirations);
            flash.put("emails", newProject.emails);
            flash.put("message", newProject.message);
            render("Application/index4.html", projects);
        }
    }
    
    /*
    public static void addProject(
        final String projectTitle,
        final String artist,
        final File myImage,
        final String description,
        final String tags,
        final String livingInspirations,
        final String pastInspirations,
        final String nonArtistInspirations,
        final String emails,
        final String message
    ) {       
        final Picture newPicture = new Picture();
        System.out.println("my image: " + myImage);
        if (myImage != null) {
            InputStream data;
            try {
                data = new FileInputStream(myImage);
                InputStream dataBuffer = new BufferedInputStream(data);
                Blob myBlob = new Blob();
                myBlob.set(dataBuffer, MimeTypes.getContentType(myImage.getName()));
                newPicture.image = myBlob;
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        final Project newProject = new Project(
            projectTitle,
            artist,
            newPicture,
            description,
            tags,
            livingInspirations,
            pastInspirations,
            nonArtistInspirations,
            emails,
            message
        );
        
        if (validation.valid(newProject).ok && validation.valid(newPicture).ok) {
            newProject.save();
            flash.success("Thanks for posting");
            index4();
        } else {
            List<Project> projects = Project.find(
                "order by projectTitle asc"
            ).from(1).fetch(100);
            flash.clear();
            params.flash();
            for (play.data.validation.Error error: validation.errors()) {
                System.out.println(error.getKey() + " " + error.message());
            }
            render("Application/index4.html", projects);
        }
    }
    */
}