package models;

import play.data.format.Formats;
import play.data.validation.ValidationError;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

import java.util.*;

import io.ebean.*;

 
/**
 *  table entity managed by Ebean
 */
@Entity 
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String email;
    public String name;
    public String password;
    public String remember_token;
    public String role;
    public boolean user_disabled;
        
    public static final Finder<Long, User> find = new Finder<>(User.class);

      
    /**
     * Returns true if email and password are valid credentials.
     * @param email The email. 
     * @param password The password. 
     * @return True if email is a valid user email and password is valid for that email.
     */
//    public static boolean isValid(String email, String password) {
//    	System.out.println("email="+email);
//      return ((email != null) 
//              &&
//              (password != null));
//    }    
    
    public static int getRandom() {
    	Random rand = new Random();

		return rand.nextInt(1000) + 1;
    }
}