package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Entry extends Model {
    
	//public Long pictureid;
	@OneToOne
	public Picture picture;
	
	public String ProjectTitle = "";
	public String ProjectDescription = "";
	public boolean aboutexchange; 
	public String alsoabout1 = "",  alsoabout2 = "",  alsoabout3="",  notabout1="",  notabout2="",  notabout3="";
	
	public String StartDate=""; 
	public String ProjectLocation=""; 
	public String YourName="";
	public String CollectivesName=""; 
	public String ContactEmail="";
	
	public boolean AboutMyself=true;
	public boolean AboutAnother=false; 
	
	public String DeadOne="",  DeadTwo=""; 
	public String LiveOne="",  LiveTwo="";
	public String CanTellLiveOne="",  CanTellLiveTwo="";
	public String NonArtistOne="",  NonArtistTwo=""; 
	public String CanTellNonOne="",  CanTellNonTwo="";
	
	public String AnotherName="";
	public String AnotherProject=""; 
	public String AnotherEmail="";
	
	
	public Entry(  String ProjectTitle, String ProjectDescription, String aboutexchange, 
    		String alsoabout1, String alsoabout2, String alsoabout3, String notabout1, String notabout2, String notabout3,
    		String StartDate, String ProjectLocation, String YourName, String CollectivesName, String ContactEmail,
    		String AboutMyself, String AboutAnother, 
    		String DeadOne, String DeadTwo, String LiveOne, String LiveTwo,
    		String CanTellLiveOne, String CanTellLiveTwo, String NonArtistOne, String NonArtistTwo, 
    		String CanTellNonOne, String CanTellNonTwo, String AnotherName, String AnotherProject, String AnotherEmail)
	{
		//this.pictureid = pictureid;
		this.ProjectTitle =  ProjectTitle;
		this.ProjectDescription = ProjectDescription;
		if (aboutexchange.equalsIgnoreCase("no"))
			this.aboutexchange = false;
		this.alsoabout1 = alsoabout1;
		this.alsoabout2 = alsoabout2;
		this.alsoabout3 = alsoabout3;
		this.notabout1 = notabout1;
		this.notabout2 = notabout2; 
		this.notabout3 = notabout3;
		this.StartDate = StartDate;
		this.ProjectLocation = ProjectLocation;
		this.YourName = YourName;
		this.CollectivesName = CollectivesName;
		this.ContactEmail = ContactEmail;
		if (AboutMyself.equalsIgnoreCase("no") )
			this.AboutMyself = false;
		if (AboutAnother.equalsIgnoreCase("yes") )
			this.AboutAnother = true; 
		this.DeadOne = DeadOne;
		this.DeadTwo = DeadTwo;
		this.LiveOne = LiveOne;
		this.LiveTwo = LiveTwo;
		this.CanTellLiveOne = CanTellLiveOne;
		this.CanTellLiveTwo = CanTellLiveTwo;
		this.NonArtistOne = NonArtistOne;
		this.NonArtistTwo = NonArtistTwo; 
		this.CanTellNonOne = CanTellNonOne;
		this.CanTellNonTwo = CanTellNonTwo;
		this.AnotherName = AnotherName;
		this.AnotherProject = AnotherProject;
		this.AnotherEmail = AnotherEmail;
		
	}
}
