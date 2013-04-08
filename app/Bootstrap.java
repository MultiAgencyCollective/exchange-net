import java.util.ArrayList;

import controllers.Application;
import play.*;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
	public void doJob() {
        //Check if the database is empty
        if(Picture.count() == 0) {
        	System.err.println("REMOVING ALL DATABASE ENTRIES....");
        	Fixtures.deleteDatabase();
            //Fixtures.loadModels("seeddata.yml");
        	System.err.println("About to create and add new seed data to database");
        }
        
    }
 
}