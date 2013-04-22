package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Token extends Model {

    @Required
    public String text;

    public Token() {
        // do nothing
    }
}
