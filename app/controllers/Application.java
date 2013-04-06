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
    	System.err.println("ARGUS has value ProjectTitle = " + argus.get("ProjectTitle") );
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
    		String DeadOne, String DeadTwo, String CanTellDeadOne, String CanTellDeadTwo, String LiveOne, String LiveTwo,
    		String CanTellLiveOne, String CanTellLiveTwo, String NonArtistOne, String NonArtistTwo, 
    		String CanTellNonOne, String CanTellNonTwo, String AnotherName, String AnotherProject, String AnotherEmail) {
    	
    	
    	if (imageupload != null && imageupload.image != null )
    	{
    		System.err.println("Was able to read image file");
    		imageupload.save();
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
    	
    	renderJSON("GOT IT");
    	
    }
}