package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import controllers.MyChecks;

@Entity
public class Project extends Model {
    
    final String PLEASE_ENTER_A = "Please enter a";
    
    @Required(message = PLEASE_ENTER_A + " title.")
    @CheckWith(MyChecks.NameCheck.class)
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
    
    @Lob
    public String message;
    
    public Project() {
        // do nothing
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
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Project [PLEASE_ENTER_A=");
        builder.append(PLEASE_ENTER_A);
        builder.append(", projectTitle=");
        builder.append(projectTitle);
        builder.append(", artist=");
        builder.append(artist);
        builder.append(", myImage=");
        builder.append(myImage);
        builder.append(", description=");
        builder.append(description);
        builder.append(", tags=");
        builder.append(tags);
        builder.append(", livingInspirations=");
        builder.append(livingInspirations);
        builder.append(", pastInspirations=");
        builder.append(pastInspirations);
        builder.append(", nonArtistInspirations=");
        builder.append(nonArtistInspirations);
        builder.append(", emails=");
        builder.append(emails);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }
}
