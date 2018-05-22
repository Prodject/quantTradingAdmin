package models;

import play.data.format.Formats;

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
public class CryptoCurrencies extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;
    public String color;
    
    public static final Finder<Long, CryptoCurrencies> find = new Finder<>(CryptoCurrencies.class);

    //@OneToMany
    //public Holding holdings;
}