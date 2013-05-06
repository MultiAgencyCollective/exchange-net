package controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.GeneralData;
import models.Project;
import models.ProjectForJson;
import play.cache.Cache;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;
import play.mvc.Http;

import com.google.gson.Gson;


public class Application extends Controller {
    
    public static final int MAX_PROJECTS_PER_IP_ADDRESS = 1000;
    
    private static final int MAX_TO_RETURN = 10;
    
    /*
     * Works only if testMobile() has run previously on a call from Java code, 
     * otherwise always returns false.
     */
    public static boolean isMobile() {
        String isMobile = flash.get("isMobile");
        return isMobile != null && isMobile.equals("true");
    }
    
    private static void testMobile() {
        Http.Header uaHeader = request.headers.get("user-agent");
        String ua = uaHeader.toString();
        if(ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
            flash.put("isMobile", true);
        } else {
            flash.put("isMobile", false);
        }
        flash.put("ua", ua);
    }
    
    public static void captcha(final String id) {
        final Images.Captcha myCaptcha = Images.captcha();
        
        // display text in black color
        String code = myCaptcha.getText("#000000");
        // store in cache for 5 minutes
        Cache.set(id, code, "5mn");
        renderBinary(myCaptcha);
        try {
            myCaptcha.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void graph() {
        List<Project> projects = Project.findAll();
        
        List<ProjectForJson> projectsForJson = new ArrayList<ProjectForJson>();
        
        for (Project project: projects) {
            projectsForJson.add(new ProjectForJson(project));
        }
        
        com.google.gson.Gson gson2 = new Gson();
        String projectsJson = gson2.toJson(projectsForJson);
        
        render(projectsJson);
    }
    
    public static void form() {
        testMobile();
        final String randomId = Codec.UUID();
        render(randomId);
    }
    
    public static void projectAdded() {
        render();
    }
    
    private static boolean captchaSuccess(
        final String code,
        final String randomId
    ) {
        Object input = Cache.get(randomId);
        if (input == null || !(input instanceof String)) {
            return false;
        }
        
        final String stringInput = (String) input;
        return code.equalsIgnoreCase(stringInput);
    }
    
    public static void addProject(
        final Project newProject,
        final String code,
        final String randomId
    ) {        
        if (captchaSuccess(code, randomId)) {
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
                
                // don't allow more than 10,000 15 MB 
                // files (max file size), or about 150 GB
                final long maxImageBytes = 157286400000L;
                if (
                    myGeneralData.totalImagesSize 
                    + newProject.myImage.getFile().length()
                    > maxImageBytes
                ) {
                    MyLogger.logTooMuchImageDataStored(requestIPAddress);
                    flash.error("Too much image data already submitted.");
                    doCancelProject(newProject);
                    return;
                }
                
                final int maxProjects = 10000;
                if (Project.count() >= maxProjects) {
                    MyLogger.logTooManyProjectsExist(requestIPAddress);
                    flash.error("Too many projects exist.");
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
                
                myGeneralData.totalImagesSize 
                    += newProject.myImage.getFile().length();
                myGeneralData.save();
            } else {
                MyLogger.logIncompleteProjectSubmission(validation.errors());
                flash.error("Submission is not valid.");
                doCancelProject(newProject);
            }
            
            Cache.delete(randomId);
        } else {
            flash.put("myCode", "Invalid code. Re-upload your photo, and try again.");
            flash.error("Code is not valid.");
            doCancelProject(newProject);
        }
    }
    
    private static void doAddProject(
        final Project toAdd,
        final String ipAddress
    ) {
        // load the Set<Token> sets from the input Strings
        toAdd.initializeSets();
        toAdd.initializeImage();
        toAdd.save();
        MyLogger.logProjectAdded(toAdd, ipAddress, Project.count());
        
        projectAdded();
    }
    
    private static void doCancelProject(
        final Project toCancel
    ) {
        if (toCancel != null) {
            deleteProject(toCancel);
        }
        
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
            flash.put("sender", toCancel.sender);
            flash.put("message", toCancel.message);
        }
        
        final String randomId = Codec.UUID();
        render("Application/form.html", randomId);
    }
    
    public static void fullProject(final String name) {
        Project target = null;
        List<Project> projects = Project.findAll();
        for (Project project: projects) {
            if (project.projectTitle.equals(name)) {
                target = project;
                break;
            }
        }
        
        render(target);
    }
    
    private static List<Project> getArtistProjects(final String artist) {
        final List<Project> result = new ArrayList<Project>();
        final List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
        
        for (Project project: allProjects) {
            if (project.artist.equals(artist)) {
                result.add(project);
            }
        }
        
        return result;
    }
    
    public static void artist(final String artist, int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = getArtistProjects(artist);
        
        List<Project> projects = new ArrayList<Project>();
        for (int i = offset; i < allProjects.size() && i - offset < MAX_TO_RETURN; i++) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            projects.add(currentProject);
        }
        
        final boolean hasNext = allProjects.size() > offset + MAX_TO_RETURN;
        render(projects, offset, hasNext, artist);
    }
    
    public static void fullProjects(int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
        List<Project> projects = new ArrayList<Project>();
        for (int i = offset; i < allProjects.size() && i - offset < MAX_TO_RETURN; i++) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            projects.add(currentProject);
        }
        
        final boolean hasNext = allProjects.size() > offset + MAX_TO_RETURN;
        render(projects, offset, hasNext);
    }
    
    public static boolean previous(final int offset) {
        return offset > 0;
    }
    
    public static boolean next(final int offset) {
        return Project.count() > offset + MAX_TO_RETURN;
    }
    
    public static void listProjects(int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
        List<Project> projects = new ArrayList<Project>();
        for (int i = offset; i < allProjects.size() && i - offset < MAX_TO_RETURN; i++) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            projects.add(currentProject);
        }
        
        final boolean hasNext = allProjects.size() > offset + MAX_TO_RETURN;
        render(projects, offset, hasNext);
    }
    
    
    public static void data123() {
        List<Project> projects = Project.find(
            "order by projectTitle asc"
        ).fetch(MAX_TO_RETURN);
        for (Project project: projects) {
            project.initializeSets();
        }
        render(projects);
    }
    
    public static void getProjectByTitle(final String projectTitle) {
        List<Project> projects = Project.findAll();
        Project project = null;
        for (Project currentProject: projects) {
            if (currentProject.projectTitle.equals(projectTitle)) {
                project = currentProject;
                break;
            }
        }
        
        if (project != null) {
            render(project);
        }
    }
    
    public static void getImage2(final long id) {
        Project project = Project.findById(id);
        if (project == null) {
            return;
        }
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(project.imageBytes);
        response.current().contentType = project.imageMimeType;
        renderBinary(inputStream);
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