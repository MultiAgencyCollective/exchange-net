package controllers;

import java.io.File;
import java.io.IOException;

import models.Project;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;

import play.data.validation.Check;
import play.data.validation.Validation;
import play.db.jpa.Blob;
import play.db.jpa.JPABase;

public abstract class InitialChecks {

    public static final int MAX_50 = 50;
    public static final int MAX_100 = 100;
    public static final int MAX_MESSAGE_LENGTH = 500;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    public static final int MAX_LIST_ITEMS = 10;
    
    public static final class ProjectTitleCheck extends Check {

        @Override
        public boolean isSatisfied(
            final Object project, 
            final Object title
        ) {
            setMessage("Please enter a title.");
            if (title == null) {
                return false;
            }
            if (!(title instanceof String)) {
                return false;
            }
            final String titleString = (String) title;
            if (
                titleString.length() == 0 
                || titleString.length() > MAX_50
            ) {
                return false;
            }          
            
            if (!titleString.matches(".*[a-zA-Z]+.*")) {
                setMessage("Title must contain a letter.");
                return false;
            }
            
            for (JPABase existingProject: Project.findAll()) {
                setMessage("That title is already used.");
                if (titleString.equals(
                    ((Project) existingProject).projectTitle)
                ) {
                    return false;
                }
            }
            
            return true;
        }
        
    }
    
    public static final class URLCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
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
            
            if (urlString.length() > MAX_100) {
                setMessage("The URL is too long.");
                return false;
            }
            
            setMessage("Not a valid URL.");
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
    
    public static final class NameCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object name
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
                || nameString.length() > MAX_50
            ) {
                return false;
            }
            
            if (!nameString.matches(".*[a-zA-Z]+.*")) {
                setMessage("Name must contain a letter.");
                return false;
            }
            
            return true;
        }
    }
    
    public static final class YearCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object year
        ) {
            setMessage("Please enter a year between 1900 and 2020.");
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
    }
    
    public static final class ListCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object list
        ) {
            if (list == null) {
                return false;
            }
            if (!(list instanceof String)) {
                return false;
            }
            final String listString = (String) list;
            if (
                listString.length() == 0 
                || listString.length() > MAX_100
            ) {
                return false;
            }
            
            if (!listString.matches(".*[a-zA-Z]+.*")) {
                setMessage("List must contain a letter.");
                return false;
            }
            
            if (Project.countTokens(listString) > MAX_LIST_ITEMS) {
                setMessage(
                    "List must have " 
                    + MAX_LIST_ITEMS 
                    + " or fewer items."
                );
                return false;
            }
            
            return true;
        }
    }
    
    public static final class MessageCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object message
        ) {
            if (message == null) {
                return false;
            }
            if (!(message instanceof String)) {
                return false;
            }
            final String messageString = (String) message;
            if (
                messageString.length() == 0 
                || messageString.length() > MAX_MESSAGE_LENGTH
            ) {
                return false;
            }
            
            return true;
        }
    }
    
    public static final class DescriptionCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object description
        ) {
            if (description == null) {
                return false;
            }
            if (!(description instanceof String)) {
                return false;
            }
            final String descriptionString = (String) description;
            if (
                descriptionString.length() == 0 
                || descriptionString.length() > MAX_DESCRIPTION_LENGTH
            ) {
                return false;
            }
            
            return true;
        }
    }
    
    public static final class PhotoCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object input
        ) {            
            if (input == null) {
                // photo is optional
                return true;
            }
            
            setMessage("Please upload a photo.");
            if (!(input instanceof Blob)) {
                System.out.println("not a blob");
                return false;
            }
            final Blob myBlob = (Blob) input;
            final File blobFile = myBlob.getFile();
            
            String mimeType;
            try {
                mimeType = detectMimeType(blobFile);
                if (mimeType == null) {
                    System.out.println("not an image: null");
                    setMessage("Not an image file.");
                    return false;
                }
                if (!mimeType.contains("image")) {
                    System.out.println("not an image: " + mimeType);
                    setMessage("Not an image file.");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            // max file size of 4 MB
            final long maxFileBytes = 1048576L * 4L;
            if (blobFile.length() > maxFileBytes) {
                setMessage("Image must be smaller than 4 MB.");
                return false;
            }
            
            return true;
        }
        
        public static String detectMimeType(
            final File file
        ) throws IOException {
            TikaInputStream tikaIS = null;
            try {
                tikaIS = TikaInputStream.get(file);

                /*
                 * You might not want to provide the file's name. 
                 * If you provide an Excel
                 * document with a .xls extension, it will 
                 * get it correct right away; but if you provide an 
                 * Excel document with .doc extension, it will guess 
                 * it to be a Word document
                 */
                final Metadata metadata = new Metadata();
                // metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());

                final Detector detector = new DefaultDetector(
                    MimeTypes.getDefaultMimeTypes()
                );
                if (detector.detect(tikaIS, metadata) == null) {
                    tikaIS.close();
                    return null;
                }
                
                final String result = 
                    detector.detect(tikaIS, metadata).toString();
                tikaIS.close();
                return result;
            } finally {
                if (tikaIS != null) {
                    tikaIS.close();
                }
            }
        }
    }
}
