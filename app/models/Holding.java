package models;

import play.data.format.Formats;

import play.data.validation.Constraints;

import java.util.Date;
import java.text.DecimalFormat;
import java.util.*;

import io.ebean.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;

/**
 * Holding entity managed by Ebean
 */
@Entity 
public class Holding extends BaseModel {

    private static final long serialVersionUID = 1L;
        
    @Column(nullable=true)
    //@Constraints.Required
    public long robot_id;
    
    @Column(length=30)
    public String robot_name;
    
    @Column(nullable=true)
    //@Constraints.Required
    public long exchange_id;  
    
    @Column(length=30)
    public String exchange_name;
    
    @Column(nullable=true)
    public long user_id;
    public String user_name;
    
    @Column(length=30)
    public String symbol;
    
    @Column(precision=15,scale=8,nullable=false)
    public Double qty;
        
    public Double price;
    public Double price_btc;
    public Double price_eth;
    public Double price_usdt;
    public Double amount_btc;
    public Double amount_eth;
    public Double amount_usdt;
    public Double amount;
    public Double price_actual;
    public Double amount_actual;
        
    public String strategy;
    public long created_at_timestamp;    
     
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date created_at;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date updated_at;
    
    public static final Finder<Long, Holding> find = new Finder<>(Holding.class);

    //@ManyToOne
    //public CryptoCurrencies cryptoCurrency;
    //@ManyToOne(optional=false)
    
//    @ManyToOne
//    public Robot robot;
//    @ManyToOne
//    public Exchange exchange;    
//    @ManyToOne
//    public User user;
    //@ManyToOne
    //public Exchange exchange;    
}