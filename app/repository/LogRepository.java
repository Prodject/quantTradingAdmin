package repository;

import models.*;

import java.util.*;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class LogRepository {

	//@Inject
    private Morphia morphia;

    public static Map<String,Map> filters() {
    	List<Log> logs = Log.findAll();
    	
    	Map<String,Map>  logFilters= new HashMap<String,Map>();
    	
    	Map<String, String> userNames = new HashMap<String, String>();
    	Map<String, String> robotNames = new HashMap<String, String>();
    	Map<String, String> logLevelNames = new HashMap<String, String>();
    	
    	for(Log log : logs) {
    		userNames.put(log.user_name, log.user_name);
    		robotNames.put(log.robot_name, log.robot_name);
    		logLevelNames.put(log.log_level, log.log_level);
    	}
    	logFilters.put("userNames", userNames);
    	logFilters.put("robotNames", robotNames);
    	logFilters.put("logLevelNames", logLevelNames);
    	
    	return logFilters;
    }
}