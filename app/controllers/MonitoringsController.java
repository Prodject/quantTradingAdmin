package controllers;

import play.data.Form;

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
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;
import static play.libs.Json.*;
import io.ebean.*;
import java.text.*;

import helpers.HoldingHelper;
import helpers.UtilityHelper;
import play.mvc.Security;
import com.typesafe.config.Config;
import redis.clients.jedis.Jedis;
import helpers.LogForm;
import repository.LogRepository;
/**
 * Manage a database of MonitoringsController
 */
@Security.Authenticated(Secured.class)
public class MonitoringsController extends Controller {
	
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    
    @Inject
    public MonitoringsController(FormFactory formFactory,HttpExecutionContext httpExecutionContext) {            	    	
    	this.formFactory = formFactory;        
        this.httpExecutionContext = httpExecutionContext;
    }
           
    public Result index() {
        return Results.redirect(routes.MonitoringsController.list(0, "user_name", "DESC", null, null, null));
    }
    
    /**
     * 
     * @param page
     * @param sortBy
     * @param order
     * @param filter
     * @param userName
     * @param robotName
     * @param logLevel
     * @return
     */
    public Result list(int page, String sortBy, String order, String userName, String robotName, String logLevel) {
	    	
    	int pageSize = 6;
    	List<Log> logs = Log.find(page, pageSize, sortBy, order, userName, robotName, logLevel);
    	    	
    	LogForm logFormData = new LogForm();
    	logFormData.userName = userName;
    	logFormData.robotName = robotName;
    	logFormData.logLevel = logLevel;
    	
    	Form<LogForm> logForm = formFactory.form(LogForm.class).fill(logFormData);
    	    	
    	Map<String, Map> logFilters = LogRepository.filters();
    	
        return ok(views.html.monitorings.list.render(page, logs, sortBy, order, userName, robotName, logLevel, logForm, logFilters.get("userNames"), logFilters.get("robotNames"), logFilters.get("logLevelNames")));
        
    }
    
    public Result show(String id) {
    	Log log = Log.findById(id);

        return ok(views.html.monitorings.show.render(log));
    }    
}

