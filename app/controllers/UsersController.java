package controllers;

import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import play.mvc.Http.Session;

import repository.UserRepository;
import models.User;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import helpers.UtilityHelper;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.*;

/**
 * Manage a database of UsersController
 */
public class UsersController extends Controller {
	
	private final UserRepository userRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    
    @Inject
    public UsersController(UserRepository userRepository, FormFactory formFactory,HttpExecutionContext httpExecutionContext) {            	
    	this.userRepository = userRepository;
    	this.formFactory = formFactory;        
        this.httpExecutionContext = httpExecutionContext;
    }
        
    public  Result login() {
    	     	 
        return ok(views.html.users.login.render("login"));
    }    
    
    public Result authenticate() {
    	
    	Form<User> loginForm = formFactory.form(User.class).bindFromRequest();
    	    	    	
    	if (loginForm.hasErrors()) {
    	    return badRequest(views.html.users.login.render("login"));
    	} else {
    	    User user = loginForm.get();
    	        	    
    	    if(this.userRepository.isValid(user.email, user.password)) {
    	    	User loginedUser = User.find.query().where()
    					.eq("email", user.email)
    					.eq("password", UtilityHelper.md5String(user.password))
    					.findOne();	
    	    	
    	    	session("userId", loginedUser.id.toString());
    	    	session("email", loginedUser.email);
    	    	session("userName", loginedUser.name);
    	    	if(loginedUser.role != null && !loginedUser.role.isEmpty()) {
    	    		session("role", loginedUser.role);	
    	    	} else {
    	    		session("role", "user");
    	    	}
    	    	    	         
    	        return redirect(routes.HoldingsController.index());
    	    } else {
    	    	return ok("failed login ");
    	    }    	    
    	}
   }    
        
    /**
     * Logs out (only for authenticated users) and returns them to the Index page. 
     * @return A redirect to the Index page. 
     */
    @Security.Authenticated(Secured.class)
    public Result logout() {
      session().clear();
      return redirect(routes.UsersController.login());
    }   
 
    public Result index() {
        return Results.redirect(routes.UsersController.list(0, "id", "DESC", ""));
    }

    public CompletionStage<Result> list(int page, String sortBy, String order, String filter) {
        
        return userRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(list -> {
            
            return ok(views.html.users.list.render(list, sortBy, order, filter));
        }, httpExecutionContext.current());
    }
    
    /**
     * Display the 'new form'.
     */
    public Result create() {
    	
        Form<User> userForm = formFactory.form(User.class);
        
        
    	return ok(views.html.users.create.render(userForm));
    }

    /**
     * Handle the 'new form' submission
     */
    public Result save() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if (userForm.hasErrors()) {             
            return badRequest(views.html.users.create.render(userForm));
        }

        User userData = userForm.get();
        User user = new User(); 
        
        user.name = userData.name;
        user.email = userData.email;
        user.password = UtilityHelper.md5String(userData.password);
        user.role = userData.role;
        
        userRepository.save(user);
        
        flash("success", "User " + user.name + " has been created");
        
        return index();            
    }
    
    /**
     * Handle user deletion
     */
    public CompletionStage<Result> delete(Long id) {
        // Run delete db operation, then redirect
        return userRepository.delete(id).thenApplyAsync(v -> {
            
            flash("success", "User has been deleted");
            return index();
        }, httpExecutionContext.current());
    }
    
    /**
     * Display the 'edit form'.
     *
     * @param id Id of the user to edit
     */
    public CompletionStage<Result> edit(Long id) {
    	
        return userRepository.lookup(id).thenApplyAsync(user -> {      
        	                       
            Form<User> userForm = formFactory.form(User.class).fill(user.get());
            
            return ok(views.html.users.edit.render(id, userForm));
            
        }, httpExecutionContext.current());
    }
    
    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the user to edit
     */
    public CompletionStage<Result> update(Long id) throws PersistenceException {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if (userForm.hasErrors()) {             
            return userRepository.lookup(id).thenApplyAsync(companies -> {
                 
                return badRequest(views.html.users.edit.render(id, userForm));
                
            }, httpExecutionContext.current());
        } else {
        	User userData = userForm.get();
             
            return userRepository.update(id, userData).thenApplyAsync(data -> {
                 
                flash("success", "User " + userData.name + " has been updated");
                return index();
            }, httpExecutionContext.current());
        }
    }    
}
