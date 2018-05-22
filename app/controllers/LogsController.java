package controllers;

import play.data.Form;

import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import repository.UserRepository;
import models.*;

//import javax.inject.Inject;
//import javax.persistence.PersistenceException;
import java.util.*;

import io.ebean.*;

import java.io.IOException;
import java.text.*;

import helpers.HoldingHelper;
import redis.clients.jedis.Jedis;
import static play.libs.Json.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import repository.LogRepository;

 
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;


import helpers.MongoConfig;
import helpers.UtilityHelper;
import helpers.HoldingHelper;

/**
 * Manage a database of logs
 */
public class LogsController extends Controller {
          
	public Result index() {		  
		  String[] currencies = new String[2];
		  currencies[0]="BTC";
		  currencies[1]="ETH";
		  
		  Jedis jedis = new Jedis("localhost");
		  jedis.auth("viewfin333");
		  jedis.set("currency", currencies[0]);
		  String currency = jedis.get("currency");
		  
		  System.out.println("currency="+currency);
		  System.out.println("user name="+session("userName"));
		  		  
	    return ok(toJson(currencies));
	}
	
	public Result logRedis() {		  
		  String[] currencies = new String[2];
		  currencies[0]="BTC";
		  currencies[1]="ETH";
		  
		  Jedis jedis = new Jedis("localhost");
		  jedis.auth("viewfin333");
		  
		  jedis.set("currency", currencies[0]);
		  String currency = jedis.get("currency");
		  
		  System.out.println("currency="+currency);
		  		  		  
		  //String userId = jedis.hget("user_id", "kevin");
		  Map<String, String> userIds = jedis.hgetAll("user_id");
		  
		  Set<String> references = jedis.keys("*_reference");
		  Set<String> statusKeys = jedis.keys("*_status");
		  for(String reference : references) {
              System.out.println(reference);         
          }
		  
		  for(String status : statusKeys) {
              System.out.println(status);         
          }		  
		  
		  Set<String> redisKeys = new HashSet<String>();
		    
		  redisKeys.add("australia");
		  redisKeys.add("canada");
		    		  
	    return ok(toJson(redisKeys));
	}
	
	public Result logJson() throws JsonParseException, JsonMappingException, IOException {		  
		  		    	  		  
		  ObjectMapper mapper = new ObjectMapper(); 
		  String robotsJson = "{\"robot_1\": \"strategy_maker1\", \"robot_2\": \"strategy_maker2\", \"robot_3\": \"strategy_maker3\"}";
		    		  
		  Map<String, String> robots = mapper.readValue(robotsJson, Map.class);
		  
		  for (Map.Entry<String, String> robot : robots.entrySet()) {
				System.out.println("Key : " + robot.getKey() + " Value : " + robot.getValue());
		  }		  
		  
		  System.out.println(robots);		  
		  System.out.println("end");		  
		  
	    return ok(toJson(robots));
	}	
	
	public Result logMongodb() {		  
		
		  Date today = new Date();
		  
	        Log log = new Log();
	         
	        log.user_name="huanghe";
	        log.robot_name="robotOne";
	        log.log_level="ERROR";
	        log.content= UtilityHelper.logContent();
	        log.created_at=today;

	        
	        log.save();
	        
	        log.user_name="kevin";
	        log.robot_name="robotTwo";
	        log.log_level="WARNING";
	        log.content= UtilityHelper.logContent();
	        log.created_at=today;
		        
	        log.save();	        
	        
	        List<Log> logs = Log.findAll();
	        //List<Log> logs = Log.find("huanghe","robotOne");
	        
	        
	    return ok(toJson(logs));
	}	
}
