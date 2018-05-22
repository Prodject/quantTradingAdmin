package models;

import play.data.format.Formats;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

import java.util.*;

import io.ebean.*;


/**
 * Robot entity managed by Ebean
 */
@Entity 
public class Robot extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String uid;
    public String name;
    public String color;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date created_at;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date updated_at;    
    
    public static final Finder<Long, Robot> find = new Finder<>(Robot.class);

}