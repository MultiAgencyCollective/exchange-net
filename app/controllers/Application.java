package controllers;

import play.*;
import play.mvc.*;

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
    
    

    public static void testSubmit( String ProjectTitle, String ProjectDescription, boolean exchangeyes, boolean exchangeno, 
    		String alsoabout1, String alsoabout2, String alsoabout3, String notabout1, String notabout2, String notabout3,
    		String StartDate, String ProjectLocation, String YourName, String CollectivesName, String ContactEmail,
    		boolean AboutMyself, boolean NotAboutMyself, boolean AboutAnother, boolean NotAboutAnother, 
    		String DeadOne, String DeadTwo, String CanTellDeadOne, String CanTellDeadTwo, String LiveOne, String LiveTwo,
    		String CanTellLiveOne, String CanTellLiveTwo, String NonArtistOne, String NonArtistTwo, 
    		String CanTellNonOne, String CanTellNonTwo, String AnotherName, String AnotherProject, String AnotherEmail) {
    	
    	
    	System.err.println("01:" + ProjectTitle);
    	System.err.println("02:" + ProjectDescription);
    	System.err.println("03:" + exchangeyes + " and " + exchangeno);
    	
    	System.err.println("04: about " + alsoabout1 + " and " + alsoabout2 + " and " + alsoabout3);
    	System.err.println("05: not about " + notabout1 + " nor " + notabout2 + " nor " + notabout3);
    	
    	System.err.println("06:" + StartDate);
    	System.err.println("07:" + ProjectLocation);
    	System.err.println("08:" + YourName);
    	System.err.println("09:" + CollectivesName);
    	System.err.println("10:" + ContactEmail);
    	
    	System.err.println("11:" + AboutMyself + "|" + NotAboutMyself + "|" + AboutAnother + "|"+ NotAboutAnother);
    	
    	System.err.println("12:" + DeadOne );
    	System.err.println("13:" + DeadTwo);
    	System.err.println("14:" + CanTellDeadOne + "|" + CanTellDeadTwo );
    	
    	System.err.println("15:" + LiveOne );
    	System.err.println("16:" + LiveTwo);
    	System.err.println("17:" + CanTellLiveOne + "|" + CanTellLiveTwo );
    	
    	System.err.println("18:" + NonArtistOne );
    	System.err.println("19:" + NonArtistTwo);
    	System.err.println("20:" + CanTellNonOne + "|" + CanTellNonTwo );
    	
    	System.err.println("21:" + AnotherName);
    	System.err.println("22:" + AnotherProject);
    	System.err.println("23:" + AnotherEmail);
    	
    	renderJSON("GOT IT");
    	
    }
}