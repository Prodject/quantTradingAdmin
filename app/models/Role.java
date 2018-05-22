package models;

import play.data.format.Formats;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

import java.util.*;

import io.ebean.*;


/**
 * Computer entity managed by Ebean
 */
@Entity 
public class Role extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String role_short_name;
    public String role_display_name;
    public String info;
    
    public static final Finder<Long, Role> find = new Finder<>(Role.class);

}