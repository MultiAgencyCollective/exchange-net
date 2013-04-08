package controllers;

import play.*;
import play.db.jpa.Blob;
import play.mvc.*;

import java.io.File;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.*;

public class Application extends Controller {

    public static void index( HashMap<String,Object>argus) {
        render(argus);
    }
    
    public static void index2() {
    	String orgname = "The MultiAgency Collective";
        render(orgname);
    }
    
    public static void setupTest() {
    	render();
    }
    
    public static void testJSON( String echo ) {

    	HashMap<String, String> hm = new HashMap<String, String>();
    	hm.put("key1", "value1");
    	hm.put("key2", "value2");
    	
    	com.google.gson.Gson gson = new Gson();
    	String json = gson.toJson(hm);
    	renderJSON( json );
    }
    
    public static void testUpload( Picture pic ) {
    	if (pic.image == null)
    	{
    		flash.error("Oops, you need to select an image file!");
            showIt();
    	}
    	else
    	{
	    	pic.save();
	    	showIt();
    	}
    }
    
    public static void showIt() {
    	render();
    }
    
    
    
    public static void getPicture(long id) {
        Picture picture = Picture.findById(id);
        if (picture == null) {
        	System.err.println( "There was no picture found with id = " + id); 
        }
        //TODO:  create an "image not found" image to serve in case there's none found.
        response.setContentTypeIfNotSet(picture.image.type());
        renderBinary(picture.image.get());
    
    }
    
    public static void error( String message )
    {
    	flash.error("ERROR in processing uploaded file");
    	render(message);
    }

    public static void testSubmit(  Picture imageupload, String ProjectTitle, String ProjectDescription, String aboutexchange, 
    		String alsoabout1, String alsoabout2, String alsoabout3, String notabout1, String notabout2, String notabout3,
    		String StartDate, String ProjectLocation, String YourName, String CollectivesName, String ContactEmail,
    		String AboutMyself, String AboutAnother, 
    		String DeadOne, String DeadTwo, String LiveOne, String LiveTwo,
    		String CanTellLiveOne, String CanTellLiveTwo, String NonArtistOne, String NonArtistTwo, 
    		String CanTellNonOne, String CanTellNonTwo, String AnotherName, String AnotherProject, String AnotherEmail) {
    	
    	
    	//Long pictureid = new Long(-1);
    	
    	if (imageupload != null && imageupload.image != null )
    	{
    		System.err.println("Was able to read image file");
    		imageupload.save();
    		//pictureid = imageupload.getId();
    	}
    	else
    	{
    		if (imageupload == null )
    		{
    			System.err.println("No Image Attachment");
    			flash.error("Oops, you need to select an image file!");
                index( new HashMap<String,Object>() );
    		}
    		else
    		{
	    		System.err.println("Can't read image file");
	    		flash.error("Oops, you need to select an image file!");
	    		HashMap<String, Object>argus = new HashMap<String,Object>();
	    		
	    		argus.put("ProjectTitle", ProjectTitle );
	    		argus.put("ProjectDescription", ProjectDescription );
	    		argus.put("alsoabout1", alsoabout1 );
	    		argus.put("alsoabout2", alsoabout2 );
	    		argus.put("alsoabout3", alsoabout3 );
	    		argus.put("notabout1", notabout1 );
	    		argus.put("notabout2", notabout2 );
	    		argus.put("notabout3", notabout3 );
	    		argus.put("StartDate", StartDate );
	    		argus.put("ProjectLocation", ProjectLocation );
	    		argus.put("YourName", YourName );
	    		argus.put("CollectivesName", CollectivesName );
	    		argus.put("ContactEmail", ContactEmail );
	    		argus.put("DeadOne", DeadOne );
	    		argus.put("DeadTwo", DeadTwo );
	    		argus.put("LiveOne", LiveOne );
	    		argus.put("LiveTwo", LiveTwo );
	    		argus.put("CanTellLiveOne", CanTellLiveOne );
	    		argus.put("CanTellLiveTwo", CanTellLiveTwo );
	    		argus.put("NonArtistOne", NonArtistOne );
	    		argus.put("NonArtistTwo", NonArtistTwo );
	    		argus.put("CanTellNonOne", CanTellNonOne );
	    		argus.put("CanTellNonTwo", CanTellNonTwo );
	    		argus.put("AnotherName", AnotherName );
	    		argus.put("AnotherProject", AnotherProject );
	    		argus.put("AnotherEmail", AnotherEmail );
	    		
	    		
	    		System.err.println( "argus = " + argus.toString()) ;
                render("Application/index.html", argus );
    		}
    	}
    	
    	//now we have a valid pictureid in Long pictureid
    	
    	System.err.println("01:" + ProjectTitle);
    	System.err.println("02:" + ProjectDescription);
    	System.err.println("03:" + aboutexchange );
    	
    	System.err.println("04: about " + alsoabout1 + " and " + alsoabout2 + " and " + alsoabout3);
    	System.err.println("05: not about " + notabout1 + " nor " + notabout2 + " nor " + notabout3);
    	
    	System.err.println("06:" + StartDate);
    	System.err.println("07:" + ProjectLocation);
    	System.err.println("08:" + YourName);
    	System.err.println("09:" + CollectivesName);
    	System.err.println("10:" + ContactEmail);
    	
    	System.err.println("11:" + AboutMyself +  "|" + AboutAnother );
    	
    	System.err.println("12:" + DeadOne );
    	System.err.println("13:" + DeadTwo);
    	
    	System.err.println("14:" + LiveOne );
    	System.err.println("15:" + LiveTwo);
    	System.err.println("16:" + CanTellLiveOne + "|" + CanTellLiveTwo );
    	
    	System.err.println("17:" + NonArtistOne );
    	System.err.println("18:" + NonArtistTwo);
    	System.err.println("19:" + CanTellNonOne + "|" + CanTellNonTwo );
    	
    	System.err.println("20:" + AnotherName);
    	System.err.println("21:" + AnotherProject);
    	System.err.println("22:" + AnotherEmail);
    	
    	//save the record of the entry, first.
    	Entry entry = new Entry(   ProjectTitle,  ProjectDescription,  aboutexchange, 
    		 alsoabout1,  alsoabout2,  alsoabout3,  notabout1,  notabout2,  notabout3,
    		 StartDate,  ProjectLocation,  YourName,  CollectivesName,  ContactEmail,
    		 AboutMyself,  AboutAnother, 
    		 DeadOne,  DeadTwo,   LiveOne,  LiveTwo,
    		 CanTellLiveOne,  CanTellLiveTwo,  NonArtistOne,  NonArtistTwo, 
    		 CanTellNonOne,  CanTellNonTwo,  AnotherName,  AnotherProject,  AnotherEmail);
    	
    	entry.picture = imageupload;
    	entry.save();
    	
    	//now create the database representation of the project.
    	Project proj = new Project(ProjectTitle);
    	proj.picture = imageupload;
    	proj.description = ProjectDescription;
    	if (alsoabout1 != null && alsoabout1.length() > 0)
    		proj.descriptors.add(alsoabout1);
    	if (alsoabout2 != null && alsoabout2.length() > 0)
    		proj.descriptors.add(alsoabout2);
    	if (alsoabout3 != null && alsoabout3.length() > 0)
    		proj.descriptors.add(alsoabout3);
    	
    	if (notabout1 != null && notabout1.length() > 0)
    		proj.nondescriptors.add(notabout1);
    	if (notabout2 != null && notabout2.length() > 0)
    		proj.nondescriptors.add(notabout2);
    	if (notabout3 != null && notabout3.length() > 0)
    		proj.nondescriptors.add(notabout3);
    	
    	proj.startdate = StartDate;
    	proj.location = ProjectLocation;
    	if ( aboutexchange.equalsIgnoreCase("no") )
    		proj.aboutexchange = false;
    	
    	//make a person entry for the person who filled out the form.
    	Person sub = new Person( YourName );
    	sub.email = ContactEmail;
    	sub.collective = CollectivesName;
    	if ( AboutMyself.equalsIgnoreCase("yes") )
    		sub.projects.add( proj );
    	sub.save();
    	
    	//that person is the contact for the project
    	proj.contact = sub;
    	proj.save();
    	
    	//add influences -- doing it both for project and for the subject, above.
    	if (LiveOne.length() > 0)
    	{
	    	Person alive1 = new Person(LiveOne);
	    	alive1.email = CanTellLiveOne;
	    	alive1.save();
	    	ProjectInspiration ipa1 = new ProjectInspiration(proj, alive1);
	    	ipa1.save();
	    	PersonalInspiration isa1 = new PersonalInspiration(sub, alive1);
	    	isa1.save();
	    	proj.inspirations.add(ipa1);
	    	sub.inspirations.add(isa1);
    	}
    	
    	if (LiveTwo.length() > 0)
    	{
	    	Person alive2 = new Person(LiveTwo);
	    	alive2.email = CanTellLiveTwo;
	    	alive2.save();
	    	ProjectInspiration ipa2 = new ProjectInspiration(proj, alive2);
	    	ipa2.save();
	    	PersonalInspiration isa2 = new PersonalInspiration(sub, alive2);
	    	isa2.save();
	    	proj.inspirations.add(ipa2);
	    	sub.inspirations.add(isa2);
    	}
    	
    	if (DeadOne.length() > 0 )
    	{
	    	Person dead1 = new Person(DeadOne);
	    	dead1.save();
	    	ProjectInspiration ipd1 = new ProjectInspiration(proj, dead1);
	    	ipd1.save();
	    	PersonalInspiration isd1 = new PersonalInspiration(sub, dead1);
	    	isd1.save();
	    	proj.inspirations.add(ipd1);
	    	sub.inspirations.add(isd1);
    	}
    	
    	if (DeadTwo.length() > 0 )
    	{
	    	Person dead2 = new Person(DeadTwo);
	    	dead2.save();
	    	ProjectInspiration ipd2 = new ProjectInspiration(proj, dead2);
	    	ipd2.save();
	    	PersonalInspiration isd2 = new PersonalInspiration(sub, dead2);
	    	isd2.save();
	    	proj.inspirations.add(ipd2);
	    	sub.inspirations.add(isd2);
    	}
    	
    	if (NonArtistOne.length() > 0)
    	{
	    	Person nona1 = new Person(NonArtistOne);
	    	nona1.email = CanTellNonOne;
	    	nona1.save();
	    	ProjectInspiration ipn1 = new ProjectInspiration(proj, nona1);
	    	ipn1.save();
	    	PersonalInspiration isn1 = new PersonalInspiration(sub, nona1);
	    	isn1.save();
	    	proj.inspirations.add(ipn1);
	    	sub.inspirations.add(isn1);
    	}
    	
    	if (NonArtistTwo.length() > 0 )
    	{
	    	Person nona2 = new Person(NonArtistTwo);
	    	nona2.email = CanTellNonTwo;
	    	nona2.save();
	    	ProjectInspiration ipn2 = new ProjectInspiration(proj, nona2);
	    	ipn2.save();
	    	PersonalInspiration isn2 = new PersonalInspiration(sub, nona2);
	    	isn2.save();
	    	proj.inspirations.add(ipn2);
	    	sub.inspirations.add(isn2);
    	}
    	//commit the project to the database.
    	proj.save();
    	sub.save();
    	
    	renderJSON("GOT IT");
    	
    }
}