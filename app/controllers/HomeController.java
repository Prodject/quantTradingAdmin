package controllers;

//import play.api.Logger;
//import java.sql.*;  
//import play.db.*;

import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Manage a database of home
 */
public class HomeController extends Controller {     
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public HomeController(FormFactory formFactory,
                          HttpExecutionContext httpExecutionContext) {         
        this.formFactory = formFactory;         
        this.httpExecutionContext = httpExecutionContext;
    }

//    /**
//     * This result directly redirect to application home.
//     */
//    private Result GO_HOME = Results.redirect(
//            routes.HomeController.list(0, "name", "asc", "")
//    );

    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return Results.redirect(routes.HoldingsController.index());
    }    
}
