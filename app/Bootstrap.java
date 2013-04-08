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
        	
        	
        	ArrayList<Long>ids = new ArrayList<Long>();
        	
        	for (int i = 0; i<10; i++)
        	{
        		String name = "p" + i;
        		Person p = new Person(name);
        		p.save();
        		ids.add( p.id );
        	}
        	for (int j = 0; j<30; j++ )
        	{
        		int i1 = (int) Math.floor( ids.size() * Math.random() );
        		int i2 = (int) Math.floor( ids.size() * Math.random() );
        		if (i2 == i1) { i2 = i1 + 1; }
        		if (i2 >= ids.size() ) { i2 = 0; }
        		Person p1 = Person.findById( ids.get(i1) );
        		Person p2 = Person.findById( ids.get(i2) );
        		PersonalInspiration pi = new PersonalInspiration(p1, p2);
        		pi.save();
        	}
        	
        }
        
    }
 
}