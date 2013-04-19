package controllers;

import models.Picture;
import play.data.validation.Check;

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
            if (photo == null) {
                return false;
            }
            if (!(photo instanceof Picture)) {
                return false;
            }
            Picture photoPicture = (Picture) photo;
            if (photoPicture.image == null) {
                return false;
            }
            
            return true;
        }
    }
}
