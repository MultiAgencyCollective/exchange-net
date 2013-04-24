package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public final class Number extends Model {

    private static final long serialVersionUID = -2729092743856602060L;
   
    @Required
    public int value;

    public Number() {
        // do nothing
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Number [value=");
        builder.append(this.value);
        builder.append("]");
        return builder.toString();
    }
}
