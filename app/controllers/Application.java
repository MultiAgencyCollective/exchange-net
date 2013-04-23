package controllers;

import java.util.List;

import models.GeneralData;
import models.Project;
import play.mvc.Controller;


public class Application extends Controller {
    
    public static final int MAX_PROJECTS_PER_IP_ADDRESS = 1000;
    
    public static void index() {
        final int maxToReturn = 100;
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).fetch(maxToReturn);
        
        render(projects);
    }
    
    public static void addProject(final Project newProject) {        
        if (validation.valid(newProject).ok) {
            final String requestIPAddress = request.remoteAddress;
            final GeneralData myGeneralData = (GeneralData) GeneralData.findAll().get(0);
            final models.Number submissionCount = 
                myGeneralData.ipToSubmissionCount.get(requestIPAddress);
            if (submissionCount.value >= MAX_PROJECTS_PER_IP_ADDRESS) {
                flash.error("Too many projects submitted.", (Object) null);
                doCancelProject(newProject);
                return;
            }
            
            doAddProject(newProject);
            submissionCount.value++;
        } else {
            doCancelProject(newProject);
        }
    }
    
    private static void doAddProject(final Project toAdd) {
        // load the Set<Token> sets from the input Strings
        toAdd.initializeSets();
        toAdd.save();
        flash.success("Thanks for posting");
        index();
    }
    
    private static void doCancelProject(final Project toCancel) {
        if (toCancel != null) {
            deleteProject(toCancel);
        }
        
        final int maxToReturn = 100;
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).fetch(maxToReturn);
        flash.clear();
        flash.put("projectTitle", toCancel.projectTitle);
        flash.put("artist", toCancel.artist);
        flash.put("description", toCancel.description);
        flash.put("tags", toCancel.tags);
        flash.put("livingInspirations", toCancel.livingInspirations);
        flash.put("pastInspirations", toCancel.pastInspirations);
        flash.put(
            "nonArtistInspirations", 
            toCancel.nonArtistInspirations
        );
        flash.put("emails", toCancel.emails);
        flash.put("message", toCancel.message);
        render("Application/index.html", projects);
    }
    
    public static void data() {
        final int maxToReturn = 100;
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).fetch(maxToReturn);
        for (Project project: projects) {
            project.initializeSets();
            System.out.println(project);
        }
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
    
    private static void deleteProject(final Project project) {
        if (project == null) {
            return;
        }

        if (project.myImage != null) {
            project.myImage.getFile().delete();
        }
        
        project.delete();
     }
}