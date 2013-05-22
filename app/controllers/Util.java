package controllers;

import models.ProjectToken;


public abstract class Util {

    public static boolean containsIgnoreCase(
        final Iterable<ProjectToken> source, 
        final String target
    ) {
        if (source == null || target == null || target.length() == 0) {
            return false;
        }
        
        for (ProjectToken token: source) {
            if (token.text.equalsIgnoreCase(target)) {
                return true;
            }
        }
        
        return false;
    }

}
