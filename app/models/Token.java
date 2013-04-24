package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public final class Token extends Model {

    private static final long serialVersionUID = -2007799939305725772L;

    @Required
    public String text;

    public Token() {
        // do nothing
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Token [text=");
        builder.append(this.text);
        builder.append("]");
        return builder.toString();
    }
}
