package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class ProjectAffiliation extends Model {
 
	public HashSet<Project> affiliates;
	
	public ProjectAffiliation( Project p1, Project p2, Project p3 ) {
		affiliates = new HashSet<Project>();
		affiliates.add( p1 );
		affiliates.add( p2 );
		affiliates.add( p3 );
	}
}
