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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Token [text=");
        builder.append(text);
        builder.append("]");
        return builder.toString();
    }
}
