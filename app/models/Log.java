package models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.query.Query;

import helpers.MongoConfig;

import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import org.bson.types.ObjectId;
import java.util.Date;
import play.data.format.Formats;
import repository.DatabaseExecutionContext;

@Entity(value = "logs", noClassnameStored = true)
public class Log extends BaseMongodb {
	@Id
	//public Long id;	
	public ObjectId _id;    
	public String user_name;
	public String robot_name;
	public String log_level;
	public String content;
	
    @Formats.DateTime(pattern="yyyy-MM-dd hh:mm:ss")
    public Date created_at;
        
    public static Log findById(String id) {
    	Log log = MongoConfig
    			.datastore()
    			.find(Log.class)
                .field("_id")
                .equal(new ObjectId(id))
                .get();
        
        return log;
    }    
       
    public static List<Log> findAll() {
    	
    	return MongoConfig
    			.datastore()
    			.find(Log.class)
    			.asList();
    }
    
    public static List<Log> findAllByCreateQuery() {
    	
    	return MongoConfig
    			.datastore()
    			.createQuery(Log.class)
    			.asList();
    }   
    
    public static List<Log> find(int page, int pageSize, String sortBy, String order, String userName, String robotName,String logLevel) {
    	Query<Log> query;
    	List<Log> logs;
    	
    	if(order.equals("asc")) {
   	    	order = "";
   	    } else {
   	    	order = "-";
   	    }
    	   	    
   	    query = MongoConfig
		 			.datastore()
		 			.find(Log.class);
   	       	    
   	     if(userName != null && !userName.isEmpty()) { 
   	    	query =  query.field("user_name").equal(userName);     		   	 
   	     } else if(robotName != null && !robotName.isEmpty()) {
   	    	query = query.field("robot_name").equal(robotName);    	    	 
   	     } else if(logLevel != null && !logLevel.isEmpty()) {
 	    	query = query.field("log_level").equal(logLevel);    	    	 
 	     }    	     
   	     
   	     query.offset(pageSize * (page - 1)).limit(pageSize);
   	        	     
	   	 logs =  query.order(order+sortBy)
	 			.asList();   
	   	 
    	return logs;
    }
    
}
