package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public final class GeneralDataToken extends Model {

    private static final long serialVersionUID = -3394657858296880119L;

    @ManyToOne(targetEntity=GeneralData.class)
    public GeneralData generalData;
    
    @Required
    public String text;

    public GeneralDataToken() {
        // do nothing
    }
    
    public GeneralDataToken(final String someText) {
        this.text = someText;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GeneralDataToken [text=");
        builder.append(this.text);
        builder.append("]");
        return builder.toString();
    }
}
