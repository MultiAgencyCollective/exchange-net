package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class GeneralData extends Model {

    private static final long serialVersionUID = -1043840899011594414L;

    @OneToMany(cascade = CascadeType.ALL)
    public Map<String, Number> ipToSubmissionCount;
    
    @OneToMany(cascade = CascadeType.ALL)
    public Set<Token> sentEmailAddresses;
    
    public GeneralData() {
        this.ipToSubmissionCount = new HashMap<String, Number>();
        this.sentEmailAddresses = new HashSet<Token>();
    }
}
