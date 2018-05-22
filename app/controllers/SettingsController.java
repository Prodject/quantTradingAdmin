package controllers;

import play.data.Form;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import repository.UserRepository;
import models.*;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.*;

import static play.libs.Json.toJson;
import static play.libs.Json.*;
import io.ebean.*;

import java.io.IOException;
import java.text.*;

import helpers.HoldingHelper;
import helpers.UtilityHelper;

import redis.clients.jedis.Jedis;
import java.util.concurrent.CompletionStage; 
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.typesafe.config.Config;
/**
 * Manage a database of settings
 */
public class SettingsController extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final Jedis jedis;
    private final Config config;
    
    @Inject
    public SettingsController(FormFactory formFactory, HttpExecutionContext httpExecutionContext, Config config) {
    	this.config = config;
        this.formFactory = formFactory;        
        this.httpExecutionContext = httpExecutionContext;
	    this.jedis = new Jedis(this.config.getString("redis.default.host"));
	    this.jedis.auth(this.config.getString("redis.default.password"));	
	    	        	   
    }
    
    /**
     * index list
     */
    public Result index(String key) throws JsonParseException, JsonMappingException, IOException {
    	
    	String userName = session("userName");//huanghe kevin
    	String category = "参数设置页面"; 
    	Set<String> redisKeys = new HashSet<String>();
    	redisKeys.add("user_id");
    	Map<String, String> redisValues = new HashMap<String, String>();
    	
	     if (key== null) {
	    	 key = "user_id";
	     }
	     String type = this.jedis.type(key);
	     	     
	     if(type.equals("string")) {	    	 	    	 
	    	 redisValues.put(key,this.jedis.get(key));
	     } else if (type.equals("hash"))  {
	    	  
	    	 redisValues = this.jedis.hgetAll(key);	
	     }
	     
		  Set<String> references = this.jedis.keys("*_reference");
		  Set<String> statusKeys = this.jedis.keys("*_status");
		  for(String reference : references) {              

              redisKeys.add(reference);
          }
		  
		  for(String status : statusKeys) {
                
              redisKeys.add(status);
          }
		  		  		  
		  ObjectMapper mapper = new ObjectMapper(); 
		  mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		  
		  String robotsJson = jedis.hget("user_id", userName);
		    		  
		  Map<String, String> robots = mapper.readValue(robotsJson, Map.class);
		  		  
        return ok(views.html.settings.index.render(category, redisKeys, redisValues, key, userName, robots));
    }    
    
    /**
     * Display the 'edit form' of a existing settings.
     *
     * @param id Id of the computer to edit
     */
    public Result edit(String key,String field) {
    	String category = "参数设置页面"; 
    	String type = this.jedis.type(key);
    	Map<String, String> redisValues = new HashMap<String, String>();
    	String value = null;
	     if(type.equals("string")) {	    	 	    	 
	    	 value = this.jedis.get(key);
	     } else if (type.equals("hash"))  {
	    	  
	    	 value = this.jedis.hget(key,field);	
	     } else if (type.equals("none"))  {
	    	 return ok(toJson(key+" doesn't exist,the type is none"));
	     } 
	     
        return ok(views.html.settings.edit.render(category, key,field, value));
    }

    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the settings to edit
     */
    public Result update(String key,String field)  {
    	DynamicForm requestData = formFactory.form().bindFromRequest();
        String redisValue = requestData.get("redisValue");
        String type = this.jedis.type(key);
        
	     if(type.equals("string")) {	    	 	    	 
	    	 this.jedis.set(key+"_set", redisValue);
	     } else if (type.equals("hash"))  {	    	  
	    	 this.jedis.hset(key+"_set", field, redisValue);	
	     } else if (type.equals("none"))  {
	    	 return ok(toJson(key+" doesn't exist,the type is none"));
	     } 
	     
	    return redirect("/settings/index");     	
    }    
    
    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the settings to edit
     */
//    public CompletionStage<Result> update(Long id) throws PersistenceException {
//        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest();
//        if (computerForm.hasErrors()) {
//            // Run companies db operation and then render the failure case
//            return companyRepository.options().thenApplyAsync(companies -> {
//                // This is the HTTP rendering thread context
//                return badRequest(views.html.editForm.render(id, computerForm, companies));
//            }, httpExecutionContext.current());
//        } else {
//            Computer newComputerData = computerForm.get();
//            // Run update operation and then flash and then redirect
//            return computerRepository.update(id, newComputerData).thenApplyAsync(data -> {
//                // This is the HTTP rendering thread context
//                flash("success", "Computer " + newComputerData.name + " has been updated");
//                return GO_HOME;
//            }, httpExecutionContext.current());
//        }
//    }
    
    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the settings to edit
     */
    public Result chartDefinedColors()  {

    	Long count = UtilityHelper.chartDefinedColorsList(this.jedis);
    	
    	//List<String> chartDefinedColors = jedis.lrange("chartDefinedColors",0,-1);    	  
    	//value = jedis.lrange("chartDefinedColors",0,-1);
    	//String one = jedis.lindex("chartDefinedColors",1);
    	//System.out.println(jedis.lindex("chartDefinedColors",0));
    	//System.out.println(jedis.lindex("chartDefinedColors",1));
    				    	
    	return redirect("/users");
    }          
}
