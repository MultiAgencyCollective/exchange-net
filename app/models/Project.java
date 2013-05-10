package models;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.apache.commons.io.IOUtils;

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
    public String artists;
    
    @Required(message = "Please upload a photo.")
    @CheckWith(MyChecks.PhotoCheck.class)
    public Blob myImage;
    
    @Required(message = PLEASE_ENTER_A + " description.")
    @CheckWith(MyChecks.DescriptionCheck.class)
    @Lob 
    public String description;
    
    public String briefDescription;
    
    @Required(message = PLEASE_ENTER_A + " tag.")
    @CheckWith(MyChecks.NameCheck.class)
    public String tags;
    
    @Required(message = PLEASE_ENTER_A + " peer.")
    @CheckWith(MyChecks.NameCheck.class)
    public String peers;
    
    @Required(message = PLEASE_ENTER_A + "n inspiration.")
    @CheckWith(MyChecks.NameCheck.class)
    public String otherInspirations;
    
    @Required(message = PLEASE_ENTER_A + " year.")
    @CheckWith(MyChecks.YearCheck.class)
    public int startYear;

    @CheckWith(MyChecks.URLCheck.class)
    public String url;
    
    public String emails;
    
    public String sender;
    
    @Lob
    public String message;
    
    @Lob
    public byte[] imageBytes;

    public String imageMimeType;
    public String imageFileName;  
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> artistSet;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> tagSet;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> peerSet;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> otherInspirationSet;
        
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectToken> emailSet;
    
    public boolean isTest;

    public long date;
    
    public Project() {
        this.artistSet = new HashSet<ProjectToken>();
        this.tagSet = new HashSet<ProjectToken>();
        this.peerSet = new HashSet<ProjectToken>();
        this.otherInspirationSet = new HashSet<ProjectToken>();
        this.emailSet = new HashSet<ProjectToken>();
        
        this.isTest = false;
        this.date = new Date().getTime();
    }
    
    public Project(
        final String projectTitle,
        final String artists,
        final Blob myImage,
        final String description,
        final int startYear,
        final String tags,
        final String peers,
        final String otherInspirations,
        final String url,
        final String emails,
        final String sender,
        final String message 
    ) {
        this.projectTitle = projectTitle;
        this.artists = artists;
        this.myImage = myImage;
        this.description = description;
        this.startYear = startYear;
        this.tags = tags;
        this.peers = peers;
        this.otherInspirations = otherInspirations;
        this.url = url;
        this.emails = emails;
        this.sender = sender;
        this.message = message;
        
        this.imageBytes = null;
        
        this.artistSet = new HashSet<ProjectToken>();
        this.tagSet = new HashSet<ProjectToken>();
        this.peerSet = new HashSet<ProjectToken>();
        this.otherInspirationSet = new HashSet<ProjectToken>();
        this.emailSet = new HashSet<ProjectToken>();
        
        this.isTest = false;
        this.date = new Date().getTime();
    }
    
    public void initializeUrl() {
        final String prefix = "http://";
        final String securePrefix = "https://";
        if (this.url != null && this.url.length() != 0) {
           if (this.url.indexOf(prefix) != 0 && this.url.indexOf(securePrefix) != 0) {
               this.url = prefix + this.url;
           }
        }
    }
    
    public void initializeImage() {
        if (!(
            this.imageBytes == null
            && this.imageMimeType == null
            && this.imageFileName == null
        )) {
            return;
        }
        
        try {
            this.imageBytes = IOUtils.toByteArray(this.myImage.get());
            this.imageMimeType = 
                MyChecks.PhotoCheck.detectMimeType(this.myImage.getFile());
            this.imageFileName = this.myImage.getFile().getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void initializeSets() {
        if (!(
            this.artistSet.isEmpty()
            && this.tagSet.isEmpty() 
            && this.peerSet.isEmpty() 
            && this.otherInspirationSet.isEmpty()
            && this.emailSet.isEmpty()
        )) {
            // already initialized 
            return;  
        }
        
        initializeSet(this.artistSet, this.artists);
        initializeSet(this.tagSet, this.tags);
        initializeSet(this.peerSet, this.peers);
        initializeSet(this.otherInspirationSet, this.otherInspirations);
        if (this.emails != null && this.emails.length() != 0) {
            initializeSet(this.emailSet, this.emails);
        } else {
            this.emailSet.clear();
        }
        
        final int maxCharsForBriefDescription = 200;
        if (this.description.length() >= maxCharsForBriefDescription) {
            this.briefDescription = this.description.substring(0, maxCharsForBriefDescription) + "...";
        } else {
            this.briefDescription = this.description;
        }
    }
    
    private static void initializeSet(
        final Set<ProjectToken> targetSet,
        final String source
    ) {
        assert targetSet != null && source != null;
        targetSet.clear();
        
        final int maxTokens = 10;
        int tokenCount = 0;
        
        for (final String commaToken: source.split(",")) {
            if (isValidToken(commaToken)) {
                if (commaToken == null || commaToken.length() == 0) {
                    System.out.println("ERROR: invalid token");
                }
                final ProjectToken newToken = new ProjectToken(commaToken);
                targetSet.add(newToken);
                tokenCount++;
                if (tokenCount >= maxTokens) {
                    return;
                }
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
        builder.append(", artists=");
        builder.append(this.artists);
        builder.append(", myImage=");
        builder.append(this.myImage);
        builder.append(", description=");
        builder.append(this.description);
        builder.append(", startYear=");
        builder.append(this.startYear);
        builder.append(", artistSet=");
        builder.append(this.artistSet);
        builder.append(", tagSet=");
        builder.append(this.tagSet);
        builder.append(", peerSet=");
        builder.append(this.peerSet);
        builder.append(", otherInspirationSet=");
        builder.append(this.otherInspirationSet);
        builder.append(", url=");
        builder.append(this.url);
        builder.append(", emailSet=");
        builder.append(this.emailSet);
        builder.append(", sender=");
        builder.append(this.sender);
        builder.append(", message=");
        builder.append(this.message);
        builder.append(", isTest=");
        builder.append(this.isTest);
        builder.append(", date=");
        builder.append(this.date);
        builder.append("]");
        return builder.toString();
    }
}
