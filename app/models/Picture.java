package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.data.validation.CheckWith;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import controllers.MyChecks;

@Entity
public class Picture extends Model {
 
    @CheckWith(MyChecks.PhotoCheck.class)
    @Lob
	public Blob image;
	
    
    public Picture() {
        // do nothing
    }
}
