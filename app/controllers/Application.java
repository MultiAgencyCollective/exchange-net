package controllers;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import models.Project;
import models.ProjectForJson;
import models.ProjectToken;
import play.cache.Cache;
import play.db.jpa.Blob;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;
import play.mvc.Http;

import com.google.gson.Gson;

public class Application extends Controller {
    
    public static final int MAX_PROJECTS_PER_IP_ADDRESS = 1000;
    
    private static final int MAX_TO_RETURN = 10;
    
    
    public static void captcha(final String id) {
        final Images.Captcha myCaptcha = Images.captcha();
        
        // display text in black color
        String code = myCaptcha.getText("#000000");
        // store in cache for 10 minutes
        Cache.set(id, code, "10mn");
        renderBinary(myCaptcha);
        try {
            myCaptcha.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void form() {
        testMobile();
        final String randomId = Codec.UUID();
        final String popularTags = getPopularTagsString(5);
        render(randomId, popularTags);
    }
    
    private static String getPopularTagsString(final int count) {
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        final List<String> popularTags = getPopularTags(count);
        for (String tag: popularTags) {
            builder.append(tag);
            i++;
            if (i < count) {
                builder.append(", ");
            }
        }
        
        return builder.toString();
    }
    
    private static List<String> getPopularTags(final int count) {
        List<String> result = new ArrayList<String>();
        
        Map<String, Integer> tagsToCount = new HashMap<String, Integer>();
        List<Project> projects = Project.findAll();        
        for (Project project: projects) {
            project.initializeSets();
            for (ProjectToken token: project.tagSet) {
                String string = token.text;
                if (tagsToCount.containsKey(string)) {
                    tagsToCount.put(string, tagsToCount.get(string) + 1);
                } else {
                    tagsToCount.put(string, 1);
                }
            }
        }
        
        while (result.size() < count && !tagsToCount.isEmpty()) {
            int max = 0;
            String tag = null;
            for (Entry<String, Integer> entry: tagsToCount.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    tag = entry.getKey();
                }
            }
            
            if (tag == null) {
                System.err.println("error in getting popular tags");
                break;
            }
            
            result.add(tag);
            tagsToCount.remove(tag);
        }
        
        return result;
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
    
    /*
     * Works only if testMobile() has run previously on a call from Java code, 
     * otherwise always returns false.
     */
    public static boolean isMobile() {
        String isMobile = flash.get("isMobile");
        return isMobile != null && isMobile.equals("true");
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
    
    public static void contact() {
        render();
    }
    
    public static void about() {
        render();
    }
    
    public static void projectEdited(final String urlString) {
        flash.put("urlString", urlString);
        render();
    }
    
    public static void projectAdded(
        final boolean canEdit,
        final String urlString
    ) {
        if (canEdit) {
            flash.put("messageSent", true);
        } else {
            flash.put("messageSent", false);
        }
        
        flash.put("urlString", urlString);
        
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

    
    public static void doCancelEdit(
        final String projectTitle,
        final String artists,
        final String description,
        final String startYear,
        final String tags,
        final String peers,
        final String otherInspirations,
        final String url,
        final String idString,
        final String uuid
    ) {         
        if (projectTitle != null) {
            flash.put("projectTitle", projectTitle);
            flash.put("artists", artists);
            flash.put("description", description);
            flash.put("startYear", startYear);
            flash.put("tags", tags);
            flash.put("peers", peers);
            flash.put("otherInspirations", otherInspirations);
            flash.put("url", url);
            flash.put("uuid", uuid);
            
            final String myProjectId = idString;
            final String randomId = Codec.UUID();
            renderTemplate("Application/edit.html", myProjectId, randomId);
        }
    }
    
    public static void projectDeleted() {
        render();
    }
    
    public static void userDeleteProject(
        final String uuid
    ) {
        List<Project> projects = Project.findAll();
        Project target = null;
        for (Project project: projects) {
            if (project.uuid.toString().equals(uuid)) {
                target = project;
                break;
            }
        }
        
        if (target != null) {
            deleteProject(target);
            render("Application/projectDeleted.html");
        } else {
            render("Application/error.html");
        }
    }
    
    public static void deleteWarning(
        final String uuid
    ) {
        List<Project> projects = Project.findAll();
        String name = null;
        for (Project project: projects) {
            if (project.uuid.toString().equals(uuid)) {
                name = project.projectTitle;
                break;
            }
        }
        
        render(uuid, name);
    }
    
    public static void editProject(
        final String projectTitle,
        final String artists,
        final Blob myImage,
        final String startYear,
        final String description,
        final String tags,
        final String peers,
        final String otherInspirations,
        final String url,
        final String code,
        final String randomId,
        final String uuid
    ) {
        List<Project> projects = Project.findAll();
        Project target = null;
        for (Project project: projects) {
            if (project.uuid.toString().equals(uuid)) {
                target = project;
                break;
            }
        }
        
        if (target == null) {
            render("Application/error.html");
            return;
        }
        
        if (captchaSuccess(code, randomId)) {
            Cache.delete(randomId);
            
            boolean success = true;
            if (!EditChecks.isEmpty(projectTitle)) {
                if (EditChecks.validText(
                    projectTitle, 
                    InitialChecks.MAX_50)
                ) {
                    if (!EditChecks.titleAvailable(
                        projectTitle, 
                        target.projectTitle)
                    ) {
                        flash.put(
                            "projectTitleError", 
                            "That title is taken."
                        );
                        success = false;
                    }
                } else {
                    flash.put(
                        "projectTitleError", 
                        "Title must contain a letter."
                    );
                    success = false;
                }
            }
            
            final String listWarning = 
                "List must have " 
                + InitialChecks.MAX_LIST_ITEMS 
                + " or fewer items.";
            
            if (!EditChecks.isEmpty(artists)) {
                if (!EditChecks.validText(
                    artists, 
                    InitialChecks.MAX_255)
                ) {
                    flash.put(
                        "artistsError", 
                        "Text must contain a letter."
                    );
                    success = false;
                } else if (!EditChecks.validItemCount(artists)) {
                    flash.put("artistsError", listWarning);
                    success = false;
                }
            }
            
            if (myImage != null) {
                if (!EditChecks.isPhoto(myImage)) {
                    flash.put(
                        "myImageError", 
                        "Not an image file."
                    );
                    success = false;
                } else if (!EditChecks.validBlobSize(myImage)) {
                    flash.put(
                        "myImageError", 
                        "Image must be smaller than 4 MB."
                    );
                    success = false;
                }
            }
            
            if (!EditChecks.isEmpty(startYear)) {
                try {
                    int yearInt = Integer.parseInt(startYear);
                    
                    if (!EditChecks.validYear(yearInt)) {
                        flash.put(
                            "startYearError", 
                            "Enter a year between 1900 and 2020."
                        );
                        success = false;
                    }
                } catch (final NumberFormatException e) {
                    flash.put(
                        "startYearError", 
                        "Enter a year between 1900 and 2020."
                    );
                    success = false;
                }

            }
            
            if (!EditChecks.isEmpty(description)) {
                if (!EditChecks.validText(
                    description, 
                    InitialChecks.MAX_DESCRIPTION_LENGTH)
                ) {
                    flash.put(
                        "descriptionError", 
                        "Text must contain a letter."
                    );
                    success = false;
                }
            }
            
            if (!EditChecks.isEmpty(tags)) {
                if (!EditChecks.validText(tags, InitialChecks.MAX_255)) {
                    flash.put(
                        "tagsError", 
                        "Text must contain a letter."
                    );
                    success = false;
                } else if (!EditChecks.validItemCount(tags)) {
                    flash.put("tagsError", listWarning);
                    success = false;
                }
            }
            
            if (!EditChecks.isEmpty(peers)) {
                if (!EditChecks.validText(peers, InitialChecks.MAX_255)) {
                    flash.put(
                        "peersError", 
                        "Text must contain a letter."
                    );
                    success = false;
                } else if (!EditChecks.validItemCount(peers)) {
                    flash.put("peersError", listWarning);
                    success = false;
                }
            }
            
            if (!EditChecks.isEmpty(otherInspirations)) {
                if (!EditChecks.validText(
                    otherInspirations, 
                    InitialChecks.MAX_255)
                ) {
                    flash.put(
                        "otherInspirationsError", 
                        "Text must contain a letter."
                    );
                    success = false;
                } else if (!EditChecks.validItemCount(otherInspirations)) {
                    flash.put("otherInspirationsError", listWarning);
                    success = false;
                }
            }
            
            if (!EditChecks.isEmpty(url)) {
                if (!EditChecks.validUrl(url)) {
                    flash.put(
                        "urlError", 
                        "Not a valid URL."
                    );
                    success = false;
                }
            }
                        
            if (success) {
                if (!EditChecks.isEmpty(projectTitle)) {
                    target.projectTitle = projectTitle;
                }
                if (!EditChecks.isEmpty(artists)) {
                    target.artists = artists;
                }
                if (myImage != null) {
                    target.myImage = myImage;
                    target.refreshImage();
                }
                if (!EditChecks.isEmpty(startYear)) {
                    int yearInt = Integer.parseInt(startYear);
                    target.startYear = yearInt;
                }
                if (!EditChecks.isEmpty(description)) {
                    target.description = description;
                }
                if (!EditChecks.isEmpty(tags)) {
                    target.tags = tags;
                }
                if (!EditChecks.isEmpty(peers)) {
                    target.peers = peers;
                }
                if (!EditChecks.isEmpty(otherInspirations)) {
                    target.otherInspirations = otherInspirations;
                }
                if (!EditChecks.isEmpty(url)) {
                    target.url = url;
                    target.refreshUrl();
                } else {
                    target.url = null;
                }
                
                target.refreshSetsAndDescription();
                target.save();
                projectEdited(getUrlString(target.projectTitle));
            } else {
                flash.error("Project is not valid.");
                doCancelEdit(
                    target.projectTitle,
                    target.artists,
                    target.description,
                    "" + target.startYear,
                    target.tags,
                    target.peers,
                    target.otherInspirations,
                    target.url,
                    target.id.toString(),
                    target.uuid
                );
            }
        } else {
            flash.put(
                "myCode", 
                "Invalid code."
            );
            
            flash.error("Code is not valid.");
            doCancelEdit(
                target.projectTitle,
                target.artists,
                target.description,
                "" + target.startYear,
                target.tags,
                target.peers,
                target.otherInspirations,
                target.url,
                target.id.toString(),
                target.uuid
            );
        }
    }
    
    public static void addProject(
        final Project newProject,
        final String code,
        final String randomId
    ) {        
        if (captchaSuccess(code, randomId)) {
            Cache.delete(randomId);

            if (validation.valid(newProject).ok) {
                final String requestIPAddress = request.remoteAddress;
                
                final int maxProjects = 10000;
                if (Project.count() >= maxProjects) {
                    MyLogger.logTooManyProjectsExist(requestIPAddress);
                    flash.error("Too many projects exist.");
                    doCancelProject(newProject);
                    return;
                }
                
                System.out.println(newProject.uuid);
                doAddProject(newProject, requestIPAddress);
            } else {
                MyLogger.logIncompleteProjectSubmission(validation.errors());
                flash.error("Project is not valid.");
                doCancelProject(newProject);
            }            
        } else {
            flash.put(
                "myCode", 
                "Invalid code. Re-upload your photo, and try again."
            );
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
        toAdd.initializeUrl();
        toAdd.save();
        MyLogger.logProjectAdded(toAdd, ipAddress, Project.count());
        if (!toAdd.isTest) {
            sendProjectCreatedEmail(toAdd);
            sendFormInviteEmails(toAdd);
        }
        
        boolean canEdit = 
            toAdd.creatorEmail != null 
            && toAdd.creatorEmail.length() != 0;
        String urlString = getUrlString(toAdd.projectTitle);
        projectAdded(canEdit, urlString);
    }
    
    private static String removeWhitespace(final String string) {
        return string.replaceAll("\\s", "");
    }
    
    private static void sendFormInviteEmails(final Project project) {
        if (project == null) {
            return;
        }
        
        project.initializeSets();
        String sender = "ExchangeArchive";
        if (
            project.sender != null
            && project.sender.length() != 0
        ) {
            sender = removeWhitespace(project.sender);
        }
        
        for (ProjectToken email: project.emailSet) {
            try {
                new EmailWorker(
                    sender, // from
                    sender 
                    + " Says: Add Your Work to the Web " 
                    + "Database of Exchange Artwork", // subject
                    email.text, // to
                    getInviteEmailText(project) // HTML text
                ).execute();
            } catch (Exception e) {
                MyLogger.logCouldNotSendEmail(
                    "invite", 
                    email.text, 
                    e.getMessage()
                );
                e.printStackTrace();
            }
        }
    }
    
    private static void sendProjectCreatedEmail(final Project project) {
        if (
            project.creatorEmail != null
            && project.creatorEmail.length() != 0
        ) {
            try {
                new EmailWorker(
                    "ExchangeArchive", // from
                    "How to Edit Your New Project in the Archive", // subject
                    project.creatorEmail, // to
                    getProjectAddedEmailText(project) // HTML message
                ).execute();
            } catch (Exception e) {
                MyLogger.logCouldNotSendEmail(
                    "projectCreated", 
                    project.creatorEmail, 
                    e.getMessage()
                );
                e.printStackTrace();
            }
        }
    }
    
    private static String getUrlString(final String projectTitle) {
        String result = "";
        try {
            result = new URI(
                "http", 
                "www.theexchangearchive.com", 
                "/application/project",
                "name=" + projectTitle,
                null
            ).toASCIIString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    private static String getInviteEmailText(final Project project) {
        
        final StringBuilder builder = new StringBuilder();
        if (
            project.sender != null
            && project.sender.length() != 0
        ) {
            builder.append("<b>").append(escapeHtml(project.sender)).
                append("</b> invited you to submit a project ");
            builder.append("to The Exchange Archive!<br /><br />");
        }
        
        if (
            project.sender != null
            && project.sender.length() != 0
            && project.message != null
            && project.message.length() != 0
        ) {
            builder.append("Your message from <b>").
                append(escapeHtml(project.sender)).append("</b>:<br />");
        }
        
        if (
            project.message != null 
            && project.message.length() != 0
        ) {
            builder.append(escapeHtml(project.message)).append("<br /><br />");
        }
        
        builder.append("<b>Submit a Project:</b><br />");
        builder.append("<a href=\"http://www.theexchangearchive.com/form\">Submit Your Project</a><br />");
        builder.append("No registration required.<br /><br />");
        builder.append("<b>About THE EXCHANGE ARCHIVE:</b><br />");
        builder.append("All artists work in a dialog with others. The Exchange Archive supports");
        builder.append(" artistic dialog by showing the inspirations that flow between projects.<br /><br />");
        builder.append("<b>See Your Friend's Project:</b><br />");
        
        String projectUrlString = getUrlString(project.projectTitle);
        
        builder.append("<a href=\"").append(projectUrlString).append("\">")
            .append(escapeHtml(project.projectTitle))
            .append("</a><br /><br />");
        builder.append("<b>Or Visit the Archive:</b><br />");
        builder.append("<a href=\"http://www.theexchangearchive.com\">theexchangearchive.com</a><br /><br />");
        builder.append("Thank You!<br />");
        
        return builder.toString();
    }
    
    private static String getProjectAddedEmailText(final Project project) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Your project was added.<br /><br />");
        
        builder.append("<b>Title:</b> ").
            append(escapeHtml(project.projectTitle)).append("<br />");
        builder.append("<b>Artists:</b> ").
            append(escapeHtml(project.artists)).append("<br /><br />");
        
        builder.append("Link to your project's page:<br />");
        
        String projectUrlString = getUrlString(project.projectTitle);
        
        builder.append("<a href=\"").append(projectUrlString).append("\">")
            .append(projectUrlString)
            .append("</a><br /><br />");
        
        builder.append("Link to <b>edit</b> your project:<br />");
        builder.append(
        "<a href=\"http://www.theexchangearchive.com/application/edit?aUUID="
            ).append(project.uuid).append("\">").append("Edit ")
            .append(escapeHtml(project.projectTitle))
            .append("</a><br /><br />");
        
        builder.append("Explore <b>THE EXCHANGE ARCHIVE</b>:<br />");
        builder.append(
            "Visit us at <a href=\"http:www.theexchangearchive.com\">" 
            + "theexchangearchive.com</a>.<br /><br />"
        );
        
        builder.append("Thank you!");
        return builder.toString();
    }
    
    private static void doCancelProject(
        final Project toCancel
    ) {
        if (toCancel != null) {
            deleteProject(toCancel);
        }
        
        if (toCancel != null) {
            flash.put("projectTitle", toCancel.projectTitle);
            flash.put("artists", toCancel.artists);
            flash.put("description", toCancel.description);
            flash.put("startYear", toCancel.startYear);
            flash.put("tags", toCancel.tags);
            flash.put("peers", toCancel.peers);
            flash.put("otherInspirations", toCancel.otherInspirations);
            flash.put("url", toCancel.url);
            flash.put("emails", toCancel.emails);
            flash.put("sender", toCancel.sender);
            flash.put("message", toCancel.message);
            flash.put("creatorEmail", toCancel.creatorEmail);
        }
        
        final String randomId = Codec.UUID();
        render("Application/form.html", randomId);
    }
    
    public static void search(final String target) {
        List<Project> allProjects = getMatchingProjects(target);
        List<Project> projects = new ArrayList<Project>();
        for (
            int i = 0; 
            i < allProjects.size() && i < MAX_TO_RETURN; 
            i++
        ) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            currentProject.initializeUrl();
            projects.add(currentProject);
        }
        
        final int offset = 0;
        final boolean hasNext = allProjects.size() > MAX_TO_RETURN;
        render(projects, offset, hasNext, target);
    }
    
    public static void searchResult(final String target, int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = getMatchingProjects(target);
        
        List<Project> projects = new ArrayList<Project>();
        for (
            int i = offset; 
            i < allProjects.size() && i - offset < MAX_TO_RETURN; 
            i++
        ) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            currentProject.initializeUrl();
            projects.add(currentProject);
        }
        
        final boolean hasNext = allProjects.size() > offset + MAX_TO_RETURN;
        render(projects, offset, hasNext, target);
    }
    
    private static List<Project> getMatchingProjects(final String target) {
        final List<Project> result = new ArrayList<Project>();
        final List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
                
        final Pattern pattern = 
            Pattern.compile(Pattern.quote(target), Pattern.CASE_INSENSITIVE);
        
        for (Project project: allProjects) {
            project.initializeSets();
            
            if (
                pattern.matcher(project.projectTitle).find()
            ) {
                result.add(project);
            } else {
                boolean found = false;
                for (ProjectToken token: project.artistSet) {
                    if (pattern.matcher(token.text).find()) {
                        found = true;
                        result.add(project);
                        break;
                    }
                }
                if (found) {
                    continue;
                }
                for (ProjectToken token: project.tagSet) {
                    if (pattern.matcher(token.text).find()) {
                        found = true;
                        result.add(project);
                        break;
                    }
                }
                if (found) {
                    continue;
                }
                for (ProjectToken token: project.peerSet) {
                    if (pattern.matcher(token.text).find()) {
                        found = true;
                        result.add(project);
                        break;
                    }
                }
                if (found) {
                    continue;
                }
                for (ProjectToken token: project.otherInspirationSet) {
                    if (pattern.matcher(token.text).find()) {
                        found = true;
                        result.add(project);
                        break;
                    }
                }
            }
        }
        
        return result;
    }
    
    public static void edit(final String aUUID) {
        Project myProject = null;
        List<Project> projects = Project.findAll();
        for (Project project: projects) {
            if (project.uuid.equals(aUUID)) {
                myProject = project;
                myProject.initializeSets();
                myProject.initializeUrl();
                break;
            }
        }
                
        String myProjectId = null;
        if (myProject != null) {
            flash.put("projectTitle", myProject.projectTitle);
            flash.put("artists", myProject.artists);
            flash.put("description", myProject.description);
            flash.put("startYear", myProject.startYear);
            flash.put("tags", myProject.tags);
            flash.put("peers", myProject.peers);
            flash.put("otherInspirations", myProject.otherInspirations);
            flash.put("url", myProject.url);
            flash.put("uuid", myProject.uuid.toString());
            
            myProjectId = myProject.id.toString();
            
            final String randomId = Codec.UUID();
            final String popularTags = getPopularTagsString(5);
            render(myProjectId, randomId, popularTags);
        }
        
        renderTemplate("Application/error.html");
    }
    
    public static void project(final String name) {
        Project target = null;
        List<Project> projects = Project.findAll();
        for (Project project: projects) {
            if (project.projectTitle.equals(name)) {
                target = project;
                target.initializeSets();
                target.initializeUrl();
                break;
            }
        }
        
        render(target, name);
    }
    
    public static void fullProjectInset(final String name) {
        Project target = null;
        List<Project> projects = Project.findAll();
        for (Project project: projects) {
            if (project.projectTitle.equals(name)) {
                target = project;
                target.initializeSets();
                target.initializeUrl();
                break;
            }
        }
        
        render(target);
    }
    
    private static List<Project> getTagProjects(final String tag) {
        final List<Project> result = new ArrayList<Project>();
        final List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
        
        ProjectToken target = new ProjectToken(tag);
        
        for (Project project: allProjects) {
            project.initializeSets();
            if (Util.containsIgnoreCase(project.tagSet, target.text)) {
                result.add(project);
            }
        }
        
        return result;
    }
    
    private static List<Project> getInspirationProjects(
        final String inspiration
    ) {
        final List<Project> result = new ArrayList<Project>();
        final List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
        
        ProjectToken target = new ProjectToken(inspiration);
        
        for (Project project: allProjects) {
            project.initializeSets();
            if (
                Util.containsIgnoreCase(project.peerSet, target.text)
                || Util.containsIgnoreCase(project.otherInspirationSet, target.text)
            ) {
                result.add(project);
            }
        }
        
        return result;
    }
    
    public static void inspiration(final String inspiration, int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = getInspirationProjects(inspiration);
        
        List<Project> projects = new ArrayList<Project>();
        for (
            int i = offset; 
            i < allProjects.size() && i - offset < MAX_TO_RETURN; 
            i++
        ) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            currentProject.initializeUrl();
            projects.add(currentProject);
        }
        
        final boolean hasNext = allProjects.size() > offset + MAX_TO_RETURN;
        render(projects, offset, hasNext, inspiration);
    }
    
    public static void tag(final String tag, int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = getTagProjects(tag);
        
        List<Project> projects = new ArrayList<Project>();
        for (
            int i = offset; 
            i < allProjects.size() && i - offset < MAX_TO_RETURN; 
            i++
        ) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            currentProject.initializeUrl();
            projects.add(currentProject);
        }
        
        final boolean hasNext = allProjects.size() > offset + MAX_TO_RETURN;
        render(projects, offset, hasNext, tag);
    }
    
    private static List<Project> getArtistProjects(final String artist) {
        final List<Project> result = new ArrayList<Project>();
        final List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
        
        for (Project project: allProjects) {
            project.initializeSets();
            for (ProjectToken token: project.artistSet) {
                if (token.text.equalsIgnoreCase(artist)) {
                    result.add(project);
                }
            }
        }
        
        return result;
    }
    
    public static void photos(int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = Project.findAll();
        
        int index = 0;
        Project project0 = null, 
            project1 = null, 
            project2 = null, 
            project3 = null, 
            project4 = null, 
            project5 = null, 
            project6 = null, 
            project7 = null, 
            project8 = null;
        if (allProjects.size() > offset + index) {
            project0 = allProjects.get(offset + index);
            index++;
            if (allProjects.size() > offset + index) {
                project1 = allProjects.get(offset + index);
                index++;
                if (allProjects.size() > offset + index) {
                    project2 = allProjects.get(offset + index);
                    index++;
                    if (allProjects.size() > offset + index) {
                        project3 = allProjects.get(offset + index);
                        index++;
                        if (allProjects.size() > offset + index) {
                            project4 = allProjects.get(offset + index);
                            index++;
                            if (allProjects.size() > offset + index) {
                                project5 = allProjects.get(offset + index);
                                index++;
                                if (allProjects.size() > offset + index) {
                                    project6 = allProjects.get(offset + index);
                                    index++;
                                    if (allProjects.size() > offset + index) {
                                        project7 = 
                                            allProjects.get(offset + index);
                                        index++;
                                        if (
                                            allProjects.size() > offset + index
                                        ) {
                                            project8 = 
                                                allProjects.get(offset + index);
                                            index++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        final int imageCount = index;
        final boolean hasNext = allProjects.size() > offset + imageCount;
        render(
            project0,
            project1,
            project2,
            project3,
            project4,
            project5,
            project6,
            project7,
            project8,
            offset, 
            hasNext
        );
    }
    
    public static void artist(final String artist, int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = getArtistProjects(artist);
        
        List<Project> projects = new ArrayList<Project>();
        for (
            int i = offset; 
            i < allProjects.size() && i - offset < MAX_TO_RETURN; 
            i++
        ) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            currentProject.initializeUrl();
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
        for (
            int i = offset; 
            i < allProjects.size() && i - offset < MAX_TO_RETURN; 
            i++
        ) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            currentProject.initializeUrl();
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
    
    public static void projects(int offset) {
        if (offset < 0 || offset >= Project.count()) {
            offset = 0;
        }
        
        List<Project> allProjects = Project.find(
            "order by projectTitle asc"
        ).fetch();
        List<Project> projects = new ArrayList<Project>();
        for (
            int i = offset; 
            i < allProjects.size() && i - offset < MAX_TO_RETURN; 
            i++
        ) {
            Project currentProject = allProjects.get(i);
            currentProject.initializeSets();
            currentProject.initializeUrl();
            projects.add(currentProject);
        }
        
        final boolean hasNext = allProjects.size() > offset + MAX_TO_RETURN;
        render(projects, offset, hasNext);
    }
    
    public static void getProjectByTitle(final String projectTitle) {
        List<Project> projects = Project.findAll();
        Project project = null;
        for (Project currentProject: projects) {
            if (currentProject.projectTitle.equals(projectTitle)) {
                project = currentProject;
                project.initializeSets();
                project.initializeUrl();
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
        
        if (project.imageBytes == null) {
            renderBinary(new File("public/images/placeholder.png"));
        } else {
            ByteArrayInputStream inputStream = 
                new ByteArrayInputStream(project.imageBytes);
            response.current().contentType = project.imageMimeType;
            renderBinary(inputStream);
        }
    }
    
    private static void deleteProject(final Project project) {
        if (project == null) {
            return;
        }

        if (project.myImage != null && project.myImage.getFile() != null) {
            boolean deleted = project.myImage.getFile().delete();
            if (!deleted) {
                MyLogger.logCouldNotDeleteImage(project);
            }
        }
        
        project.delete();
     }
}
