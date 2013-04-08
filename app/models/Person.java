package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Person extends Model {
    
	@Required
	public String name;
		
	@ManyToMany(cascade=CascadeType.PERSIST)
	public List<Person>aliases;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public List<Project>projects;

	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<PersonalAffiliation>affiliations;
	
	@Required
	@OneToMany (mappedBy="inspired", cascade=CascadeType.ALL)
	public List<PersonalInspiration>inspirations;
	
	public boolean isalive = true;
	
	public boolean isartist = true;
	
	public String collective = "";
	
	public String email = "";
	
	
	public Person( String aname )
	{
		name = aname;
		aliases = new ArrayList<Person>();
		projects = new ArrayList<Project>();
		inspirations = new ArrayList<PersonalInspiration>();
		affiliations = new HashSet<PersonalAffiliation>();
		isalive = true;
		isartist = true;
	}
	
	public String toString() {
		String desc = "";
		desc += "Name: " + name + ".\n";
		
		if ( aliases.size() > 0 )
		{
			desc += "Possibly also known as: " + aliases.get(0).name;
			for (int j = 1; j<aliases.size(); j++)
				desc += " and " + aliases.get(j).name;
			desc += ".\n";
		}
		if ( projects.size() > 0 )
		{
			desc += "Associated with Projects: " + projects.get(0).title;
			for (int j = 1; j<projects.size(); j++)
				desc += " and " + projects.get(j).title;
			desc += ".\n";
		}
		
		if ( inspirations.size() > 0 )
		{
			desc += "Inspired by: " + inspirations.get(0).inspiration.name;
			for (int j = 1; j<inspirations.size(); j++)
				desc += " and " + inspirations.get(j).inspiration.name;
			desc += ".\n";
		}
		
		if (collective.length() > 0)
		{
			desc += "Affiliated with " + collective  + ".\n";
		}
		if (email.length() > 0)
		{
			desc += "Contact email: " + email  + ".\n";
		}
		
		return desc;
	}
	
}
