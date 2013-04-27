package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import controllers.MyChecks;

@Entity
public final class Project extends Model {
    
    private static final long serialVersionUID = 1963079018764143458L;

    static final String PLEASE_ENTER_A = "Please enter a";
    
    @Required(message = PLEASE_ENTER_A + " title.")
    @CheckWith(MyChecks.ProjectTitleCheck.class)
    public String projectTitle;
    
    @Required(message = PLEASE_ENTER_A + "n artist.")
    @CheckWith(MyChecks.NameCheck.class)
    public String artist;
    
    @Required(message = "Please upload a photo.")
    @CheckWith(MyChecks.PhotoCheck.class)
    public Blob myImage;
    
    @Required(message = PLEASE_ENTER_A + " description.")
    @CheckWith(MyChecks.DescriptionCheck.class)
    @Lob 
    public String description;
    
    @Required(message = PLEASE_ENTER_A + " tag.")
    @CheckWith(MyChecks.NameCheck.class)
    public String tags;
    
    @Required(message = PLEASE_ENTER_A + " living artist.")
    @CheckWith(MyChecks.NameCheck.class)
    public String livingInspirations;
    
    @Required(message = PLEASE_ENTER_A + " past artist.")
    @CheckWith(MyChecks.NameCheck.class)
    public String pastInspirations;
    
    @Required(message = PLEASE_ENTER_A + "n inspiration.")
    @CheckWith(MyChecks.NameCheck.class)
    public String nonArtistInspirations;
    
    public String emails;
    
    public String sender;
    
    @Lob
    public String message;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> tagSet;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> livingInspirationSet;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> pastInspirationSet;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> nonArtistInspirationSet;
        
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> emailSet;

    public Project() {
        this.tagSet = new HashSet<ProjectToken>();
        this.livingInspirationSet = new HashSet<ProjectToken>();
        this.pastInspirationSet = new HashSet<ProjectToken>();
        this.nonArtistInspirationSet = new HashSet<ProjectToken>();
        this.emailSet = new HashSet<ProjectToken>();
    }
    
    public Project(
        final String projectTitle,
        final String artist,
        final Blob myImage,
        final String description,
        final String tags,
        final String livingInspirations,
        final String pastInspirations,
        final String nonArtistInspirations,
        final String emails,
        final String sender,
        final String message 
    ) {
        this.projectTitle = projectTitle;
        this.artist = artist;
        this.myImage = myImage;
        this.description = description;
        this.tags = tags;
        this.livingInspirations = livingInspirations;
        this.pastInspirations = pastInspirations;
        this.nonArtistInspirations = nonArtistInspirations;
        this.emails = emails;
        this.sender = sender;
        this.message = message;
        
        this.tagSet = new HashSet<ProjectToken>();
        this.livingInspirationSet = new HashSet<ProjectToken>();
        this.pastInspirationSet = new HashSet<ProjectToken>();
        this.nonArtistInspirationSet = new HashSet<ProjectToken>();
        this.emailSet = new HashSet<ProjectToken>();
    }
    
    public void initializeSets() {
        if (!(
            this.tagSet.isEmpty() 
            && this.livingInspirationSet.isEmpty() 
            && this.pastInspirationSet.isEmpty() 
            && this.nonArtistInspirationSet.isEmpty() 
            && this.emailSet.isEmpty()
        )) {
            // already initialized 
            return;  
        }
        
        initializeSet(this.tagSet, this.tags);
        initializeSet(this.livingInspirationSet, this.livingInspirations);
        initializeSet(this.pastInspirationSet, this.pastInspirations);
        initializeSet(this.nonArtistInspirationSet, this.nonArtistInspirations);
        if (this.emails != null && this.emails.length() != 0) {
            initializeSet(this.emailSet, this.emails);
        } else {
            this.emailSet.clear();
        }
    }
    
    private static void initializeSet(
        final Set<ProjectToken> targetSet,
        final String source
    ) {
        assert targetSet != null && source != null;
        targetSet.clear();
        for (final String commaToken: source.split(",")) {
            if (isValidToken(commaToken)) {
                if (commaToken == null || commaToken.length() == 0) {
                    System.out.println("ERROR: invalid token");
                }
                final ProjectToken newToken = new ProjectToken(commaToken);
                targetSet.add(newToken);
            }
        }
    }
    
    private static boolean isValidToken(final String token) {
        // token must contain a word character: letter or digit
        return token.matches(".*\\w.*");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Project [ProjectTitle=");
        builder.append(this.projectTitle);
        builder.append(", artist=");
        builder.append(this.artist);
        builder.append(", myImage=");
        builder.append(this.myImage);
        builder.append(", description=");
        builder.append(this.description);
        builder.append(", tagSet=");
        builder.append(this.tagSet);
        builder.append(", livingInspirationSet=");
        builder.append(this.livingInspirationSet);
        builder.append(", pastInspirationSet=");
        builder.append(this.pastInspirationSet);
        builder.append(", nonArtistInspirationSet=");
        builder.append(this.nonArtistInspirationSet);
        builder.append(", emailSet=");
        builder.append(this.emailSet);
        builder.append(", sender=");
        builder.append(this.sender);
        builder.append(", message=");
        builder.append(this.message);
        builder.append("]");
        return builder.toString();
    }
}
