package controllers;

import java.util.List;

import models.Project;
import play.Logger;

public abstract class MyLogger {

    public static void logProjectAdded(
        final Project project,
        final String ipAddress,
        final long projectCount
    ) {
        Logger.info(
            "ProjectAdded: " 
            + project.toString() 
            + ". sourceIP: "
            + ipAddress
            + ". countProjects: "
            + projectCount
            + unixTimeMillisString()
        );
    }
    
    public static void logCouldNotSendEmail(
        final String emailType,
        final String address,
        final String errorMessage
    ) {
        Logger.info(
            "CouldNotSendEmailOfType: " 
            + emailType
            + ". address: "
            + address
            + ". errorMessage: " 
            + errorMessage
            + unixTimeMillisString()
        );
    }
    
    public static void logTooManyProjectsSubmitted(
        final String ipAddress
    ) {
        Logger.info(
            "AttemptedAddTooManyProjectsFromIP: " 
            + ipAddress
            + unixTimeMillisString()
        );
    }
    
    public static void logTooManyUsersExist(
        final String ipAddress
    ) {
        Logger.info(
            "AttemptedAddWhenTooManyProjectsUsersFromIP: " 
            + ipAddress
            + unixTimeMillisString()
        );
    }
    
    public static void logTooManyProjectsExist(
        final String ipAddress
    ) {
        Logger.info(
            "AttemptedAddWhenTooManyProjectsExistFromIP: " 
            + ipAddress
            + unixTimeMillisString()
        );
    }
    
    public static void logCouldNotDeleteImage(final Project project) {
        Logger.info(
            "CouldNotDeleteImage: " 
            + project.toString() 
            + unixTimeMillisString()
        );
    }
    
    public static void logTooMuchImageDataStored(
        final String ipAddress
    ) {
        Logger.info(
            "AttemptedStoreTooMuchDataFromIP: " 
            + ipAddress
            + unixTimeMillisString()
        );
    }
    
    public static void logIncompleteProjectSubmission(
        final List<play.data.validation.Error> list
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append("IncompleteProjectSubmission. Errors:");
        for (play.data.validation.Error error: list) {
            builder.append(' ').append(error.toString());
        }
        builder.append(unixTimeMillisString());
        
        Logger.info(builder.toString());
    }
    
    private static String unixTimeMillisString() {
        return ". UnixTimeMillis: " + System.currentTimeMillis();
    }
}
