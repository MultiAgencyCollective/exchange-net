package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public final class ProjectToken extends Model {

    private static final long serialVersionUID = -3394657858296880119L;

    @ManyToOne(targetEntity=Project.class)
    public Project project;
    
    @Required
    public String text;

    public ProjectToken() {
        // do nothing
    }
    
    public ProjectToken(final String someText) {
        this.text = someText;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProjectToken [text=");
        builder.append(this.text);
        builder.append("]");
        return builder.toString();
    }
}
