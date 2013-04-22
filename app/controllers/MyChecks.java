package controllers;

import javax.activation.MimetypesFileTypeMap;

import play.data.validation.Check;
import play.db.jpa.Blob;

public class MyChecks {

    public class NameCheck extends Check {
        
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
            if (nameString.length() == 0) {
                return false;
            }
            
            return true;
        }
    }
    
    public class PhotoCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object photo
        ) {
            setMessage("Please upload a photo.");
            
            if (photo == null) {
                return false;
            }
            if (!(photo instanceof Blob)) {
                return false;
            }
            final Blob photoBlob = (Blob) photo;
            if (photoBlob.length() == 0l) {
                return false;
            }
            
            final String mimeType = 
                new MimetypesFileTypeMap().getContentType(photoBlob.getFile());
            return  mimeType.contains("image");
        }
    }
}
