package controllers;

import java.io.File;
import java.io.IOException;

import models.Project;
import play.data.validation.Validation;
import play.db.jpa.Blob;
import play.db.jpa.JPABase;


public abstract class EditChecks {

    
    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }
    
    public static boolean titleAvailable(
        final Object newTitle,
        final String oldTitle
    ) {
        if (newTitle == null) {
            return false;
        }
        if (!(newTitle instanceof String)) {
            return false;
        }
        
        final String newTitleString = (String) newTitle;
        if (oldTitle.equals(newTitle)) {
            return true;
        }
        
        for (JPABase existingProject: Project.findAll()) {
            if (newTitleString.equals(
                ((Project) existingProject).projectTitle)
            ) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean validItemCount(
        final Object list
    ) {
        if (list == null) {
            return false;
        }
        if (!(list instanceof String)) {
            return false;
        }
        final String listString = (String) list;
        
        if (Project.countTokens(listString) > InitialChecks.MAX_LIST_ITEMS) {
            return false;
        }
        
        return true;
    }
    
    public static boolean validText(
        final Object name,
        final int maxLength
    ) {
        if (name == null) {
            return false;
        }
        if (!(name instanceof String)) {
            return false;
        }
        final String nameString = (String) name;
        if (
            nameString.length() == 0 
            || nameString.length() > maxLength
        ) {
            return false;
        }          
        
        // allow newline characters in description, 
        // so enable DOTALL mode with (?s)
        if (!nameString.matches("(?s).*[a-zA-Z]+.*")) {
            System.out.println("name string:");
            System.out.println(nameString);
            return false;
        }
        
        return true;
    }
    
    public static boolean validBlobSize(final Object input) {
        if (input == null) {
            return false;
        }
        if (!(input instanceof Blob)) {
            return false;
        }
        final Blob myBlob = (Blob) input;
        final File blobFile = myBlob.getFile();
        
        if (blobFile == null) {
            return false;
        }

        // max file size of 4 MB
        final long maxFileBytes = 1048576L * 4L;
        if (blobFile.length() > maxFileBytes) {
            return false;
        }
        
        return true;
    }
        
    public static boolean isPhoto(final Object input) {
        
        if (input == null) {
            return false;
        }
        if (!(input instanceof Blob)) {
            return false;
        }
        final Blob myBlob = (Blob) input;
        final File blobFile = myBlob.getFile();
        
        String mimeType;
        try {
            mimeType = InitialChecks.PhotoCheck.detectMimeType(blobFile);
            if (mimeType == null) {
                return false;
            }
            if (!mimeType.contains("image")) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static boolean validYear(
        final Object year
    ) {
        if (year == null) {
            return false;
        }
        if (!(year instanceof Integer)) {
            return false;
        }
        final int yearInt = (Integer) year;
        final int minYear = 1900;
        final int maxYear = 2020;
        
        // special case: 0 may mean no year entered
        if (yearInt == 0) {
            return false;
        }
        
        if (yearInt < minYear || yearInt > maxYear) {
            return false;
        }
        
        return true;
    }
    
    public static boolean validUrl(
        final Object url
    ) {
        if (url == null) {
            return true;
        }
        if (!(url instanceof String)) {
            return false;
        }
        final String urlString = (String) url;
        if (urlString.length() == 0) {
            return true;
        } 
        
        if (urlString.length() > InitialChecks.MAX_100) {
            return false;
        }
        
        if (Validation.current().url(urlString).ok) {
            return true;
        }
        
        final String prefix = "http://";
        if (Validation.current().url(prefix + urlString).ok) {
            return true;
        }
        
        return false;
    }
}
