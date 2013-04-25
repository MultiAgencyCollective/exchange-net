package controllers;

import java.io.IOException;
import java.util.List;

import models.GeneralData;
import models.Project;
import play.cache.Cache;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;


public class Application extends Controller {
    
    public static final int MAX_PROJECTS_PER_IP_ADDRESS = 1000;
    
    public static void captcha(final String id) {
        final Images.Captcha myCaptcha = Images.captcha();
        String code = myCaptcha.getText("#000000");
        Cache.set(id, code, "5mn");
        renderBinary(myCaptcha);
        try {
            myCaptcha.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void index() {
        final int maxToReturn = 100;
        final List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).fetch(maxToReturn);
        
        final String randomId = Codec.UUID();
        render(projects, randomId);
    }
    
    public static void addProject(
        final Project newProject,
        final String code,
        final String randomId
    ) {        
        if (validation.equals(code, Cache.get(randomId)).ok) {
            if (validation.valid(newProject).ok) {
                final String requestIPAddress = request.remoteAddress;
                final GeneralData myGeneralData = 
                    (GeneralData) GeneralData.findAll().get(0);
                final models.Number submissionCount = 
                    myGeneralData.ipToSubmissionCount.get(requestIPAddress);
                if (
                    submissionCount != null 
                    && submissionCount.value >= MAX_PROJECTS_PER_IP_ADDRESS
                ) {
                    MyLogger.logTooManyProjectsSubmitted(requestIPAddress);
                    flash.error("Too many projects submitted.");
                    doCancelProject(newProject);
                    return;
                }
                
                doAddProject(newProject, requestIPAddress);
                if (submissionCount == null) {
                    final models.Number one = new models.Number();
                    one.value = 1;
                    myGeneralData.ipToSubmissionCount.
                        put(requestIPAddress, one);
                } else {
                    submissionCount.value++;
                }
                
                myGeneralData.save();
            } else {
                MyLogger.logIncompleteProjectSubmission(validation.errors());
                doCancelProject(newProject);
            }
        } else {
            System.out.println("code: " + code + ". randomId: " + randomId);
            doCancelProject(newProject);
        }

    }
    
    private static void doAddProject(
        final Project toAdd,
        final String ipAddress
    ) {
        // load the Set<Token> sets from the input Strings
        toAdd.initializeSets();
        toAdd.save();
        MyLogger.logProjectAdded(toAdd, ipAddress, Project.count());
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
        if (toCancel != null) {
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
        }
        
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