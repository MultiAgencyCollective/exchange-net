package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ProjectInspiration extends Model {
 
	@Required
	@ManyToOne
	public Project inspired;
	
	@Required
	@OneToOne
	public Person inspiration;
	
	public ProjectInspiration( Project inspd, Person inspiration )
	{
		this.inspired = inspd;
		this.inspiration = inspiration;
	}
	
	@Override
	public String toString() {
		return  inspired.title + " was inspired by " + inspiration.name;
	}
}
