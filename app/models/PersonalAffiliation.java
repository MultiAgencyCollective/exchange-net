package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class PersonalAffiliation extends Model {
	
	public HashSet<Person> affiliates;
	
	public PersonalAffiliation( Person p1, Person p2, Person p3 ) {
		affiliates = new HashSet<Person>();
		affiliates.add( p1 );
		affiliates.add( p2 );
		affiliates.add( p3 );
	}
    
}
