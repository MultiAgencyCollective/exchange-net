package controllers;

import play.*;
import play.mvc.*;

import java.io.File;
import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void index2() {
    	String orgname = "The MultiAgency Collective";
        render(orgname);
    }
    
    
    public static void error( String message )
    {
    	flash.error("ERROR in processing uploaded file");
    	render(message);
    }

    public static void testSubmit( File imageupload, String ProjectTitle, String ProjectDescription, String aboutexchange, 
    		String alsoabout1, String alsoabout2, String alsoabout3, String notabout1, String notabout2, String notabout3,
    		String StartDate, String ProjectLocation, String YourName, String CollectivesName, String ContactEmail,
    		String AboutMyself, String AboutAnother, 
    		String DeadOne, String DeadTwo, String CanTellDeadOne, String CanTellDeadTwo, String LiveOne, String LiveTwo,
    		String CanTellLiveOne, String CanTellLiveTwo, String NonArtistOne, String NonArtistTwo, 
    		String CanTellNonOne, String CanTellNonTwo, String AnotherName, String AnotherProject, String AnotherEmail) {
    	
    	
    	if (imageupload != null && imageupload.canRead() )
    	{
    		System.err.println("Can read image file");
    	}
    	else
    	{
    		if (imageupload == null )
    		{
    			System.err.println("No Image Attachment");
        		error("No Image Attachment");
    		}
    		else
    		{
	    		System.err.println("Can't read image file");
	    		error("Unable to read image attachment");	
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