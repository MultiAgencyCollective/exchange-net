package controllers;

import java.util.List;

import models.Project;
import play.mvc.Controller;

public class Application extends Controller {
    
    public static void index4() {
        final int maxToReturn = 100;
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).from(1).fetch(maxToReturn);
        
        render(projects);
    }
    
    
    public static void addProject2(final Project newProject) {
        if (validation.valid(newProject).ok) {
            // load the Set<Token> sets from the input Strings
            newProject.initializeSets();
            newProject.save();
            flash.success("Thanks for posting");
            index4();
        } else {
            final int maxToReturn = 100;
            List<Project> projects = Project.find(
                "order by projectTitle asc"
            ).from(1).fetch(maxToReturn);
            flash.clear();
            flash.put("projectTitle", newProject.projectTitle);
            flash.put("artist", newProject.artist);
            flash.put("myImage", newProject.myImage);
            flash.put("description", newProject.description);
            flash.put("tags", newProject.tags);
            flash.put("livingInspirations", newProject.livingInspirations);
            flash.put("pastInspirations", newProject.pastInspirations);
            flash.put(
                "nonArtistInspirations", 
                newProject.nonArtistInspirations
            );
            flash.put("emails", newProject.emails);
            flash.put("message", newProject.message);
            render("Application/index4.html", projects);
        }
    }
    
    public static void data() {
        final int maxToReturn = 100;
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).from(1).fetch(maxToReturn);
        render(projects);
    }
    
    public static void getImage(final long id) {
        Project project = Project.findById(id);
        if (project == null) {
            return;
        }

        if (project.myImage == null) {
            return;
        }
        
        response.setContentTypeIfNotSet(project.myImage.type());
        renderBinary(project.myImage.get());
    }
}