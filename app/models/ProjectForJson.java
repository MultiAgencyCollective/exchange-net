package models;

import java.util.Set;

public class ProjectForJson {

    public String projectTitle;
    
    public String[] artists;
            
    public String[] tags;
    
    public String[] peers;
    
    public String[] otherInspirations;
    
    public String date;
            
    public ProjectForJson(final Project project) {
        project.initializeSets();
        this.projectTitle = project.projectTitle;
        this.artists = getStringArray(project.artistSet);
        this.tags = getStringArray(project.tagSet);
        this.peers = getStringArray(project.peerSet);
        this.otherInspirations = getStringArray(project.otherInspirationSet);
        this.date = "" + project.date;
    }
    
    private String[] getStringArray(final Set<ProjectToken> tokenSet) {
        String[] result = new String[tokenSet.size()];
        int i = 0;
        for (ProjectToken token: tokenSet) {
            result[i] = token.text;
            i++;
        }
        
        return result;
    }
}
