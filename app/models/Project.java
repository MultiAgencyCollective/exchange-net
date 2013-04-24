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
    @CheckWith(MyChecks.NameCheck.class)
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
    
    @OneToMany(mappedBy="id", cascade=CascadeType.ALL)
    public Set<Token> tagSet;
    
    @OneToMany(mappedBy="id", cascade=CascadeType.ALL)
    public Set<Token> livingInspirationSet;
    
    @OneToMany(mappedBy="id", cascade=CascadeType.ALL)
    public Set<Token> pastInspirationSet;
    
    @OneToMany(mappedBy="id", cascade=CascadeType.ALL)
    public Set<Token> nonArtistInspirationSet;
        
    @OneToMany(mappedBy="id", cascade=CascadeType.ALL)
    public Set<Token> emailSet;
    
    @Lob
    public String message;
    
    public Project() {
        this.tagSet = new HashSet<Token>();
        this.livingInspirationSet = new HashSet<Token>();
        this.pastInspirationSet = new HashSet<Token>();
        this.nonArtistInspirationSet = new HashSet<Token>();
        this.emailSet = new HashSet<Token>();
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
        this.message = message;
        
        this.tagSet = new HashSet<Token>();
        this.livingInspirationSet = new HashSet<Token>();
        this.pastInspirationSet = new HashSet<Token>();
        this.nonArtistInspirationSet = new HashSet<Token>();
        this.emailSet = new HashSet<Token>();
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
        }
    }
    
    private static void initializeSet(
        final Set<Token> targetSet,
        final String source
    ) {
        assert targetSet != null && source != null;
        targetSet.clear();
        for (final String commaToken: source.split(",")) {
            if (isValidToken(commaToken)) {
                final Token newToken = new Token();
                newToken.text = commaToken;
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
        builder.append(", message=");
        builder.append(this.message);
        builder.append("]");
        return builder.toString();
    }
}
