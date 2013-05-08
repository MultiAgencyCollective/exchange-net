package models;

import java.util.Set;

public class ProjectForJson {

    public String projectTitle;
    
    public String artist;
            
    public String[] tags;
    
    public String[] livingInspirations;
    
    public String[] pastInspirations;
    
    public String[] nonArtistInspirations;
            
    public ProjectForJson(Project project) {
        project.initializeSets();
        this.projectTitle = project.projectTitle;
        this.artist = project.artist;
        this.tags = getStringArray(project.tagSet);
        this.livingInspirations = getStringArray(project.livingInspirationSet);
        this.pastInspirations = getStringArray(project.pastInspirationSet);
        this.nonArtistInspirations = 
            getStringArray(project.nonArtistInspirationSet);
    }
    
    private static String[] getStringArray(final Set<ProjectToken> tokenSet) {
        String[] result = new String[tokenSet.size()];
        int i = 0;
        for (ProjectToken token: tokenSet) {
            result[i] = token.text;
            i++;
        }
        
        return result;
    }
}
