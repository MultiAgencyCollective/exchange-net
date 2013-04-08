package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Project extends Model {
    
	@Required
	public String title;
	
	public String description;
	
	@OneToOne
	public Person contact;
	
	public String location;
	
	public String startdate;
	
	@OneToOne
	public Picture picture;
	
	public boolean aboutexchange = false;
	
	public ArrayList<String>descriptors;
	
	public ArrayList<String>nondescriptors;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<ProjectAffiliation>affiliations;
	
	@Required
	@OneToMany (mappedBy="inspired", cascade=CascadeType.ALL)
	public List<ProjectInspiration>inspirations;
	
	
	
	public Project(String atitle ) 
	{
		title = atitle;
		descriptors = new ArrayList<String>();
		nondescriptors = new ArrayList<String>();
		inspirations = new ArrayList<ProjectInspiration>();
		affiliations = new HashSet<ProjectAffiliation>();
		description = "";
		location = "";
		startdate = "";
	}
	
	public String toString() {
		String toreturn = title;
		if (contact != null )
			toreturn += ", by " + contact.name;
		if (description.length() > 0)
			toreturn += ".  Description: " + description ;
		if (location.length() > 0)
			toreturn += ".  Located at: " + location;
		if (startdate.length() > 0)
			toreturn += ".  Start date " + startdate;
		toreturn += ". This work";
		if (aboutexchange)
		{
			toreturn += " is about exchange";
		}
		else
		{
			toreturn += " is not about exchange";
		}
		if (descriptors.size() > 0)
		{
			toreturn += ".  It has been described as being about " + descriptors.get(0);
			for (int j = 1 ; j<descriptors.size(); j++)
				toreturn += " and " + descriptors.get(j);
			
		}
		if (nondescriptors.size() > 0)
		{
			toreturn += ".  It has been described as NOT being about " + nondescriptors.get(0);
			for (int j = 1 ; j<nondescriptors.size(); j++)
				toreturn += " nor about " + nondescriptors.get(j);
			
		}
		if ( inspirations.size() > 0 )
		{
			toreturn += ".  It was inspired by: " + inspirations.get(0).inspiration.name;
			for (int j = 1; j<inspirations.size(); j++)
				toreturn += " and " + inspirations.get(j).inspiration.name;
			
		}
		return toreturn;
	}

}
