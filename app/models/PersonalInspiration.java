package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class PersonalInspiration extends Model {
	
	@Required
	@ManyToOne
	public Person inspired;
	
	@Required
	@OneToOne
	public Person inspiration;
	
	public PersonalInspiration( Person inspd, Person inspiration )
	{
		this.inspired = inspd;
		this.inspiration = inspiration;
	}
    
	@Override
	public String toString() {
		return  inspired.name + " was inspired by " + inspiration.name;
	}
}
