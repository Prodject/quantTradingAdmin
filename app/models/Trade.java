package models;

import play.data.format.Formats;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import java.util.Date;

import java.util.*;

import io.ebean.*;

/**
 * traes entity managed by Ebean
 * price is single price
 * amount is qyt * price
 */
@Entity 
public class Trade extends BaseModel {
    private static final long serialVersionUID = 1L;
       
    public String side;
    
    public Double quantity;
    
    @Constraints.Required
    public String trade_id;
    
    @Column(nullable=true)
    //@Constraints.Required
    public long robot_id;
    
    @Column(length=30)
    public String robot_name;
    
    @Column(nullable=true)
    public long user_id;
    public String user_name;
    
    @Column(nullable=true)
    //@Constraints.Required
    public long exchange_id;
    
    @Column(length=30)
    public String exchange_name;
    
    @Constraints.Required
    public Double price;//single price special
    @Constraints.Required    
    public String pair;
    
    public Double price_btc;
    public Double price_eth;
    public Double price_usdt;
    
    @Column(precision=15,scale=8,nullable=false)
    public Double amount;
    
    @Column(precision=15,scale=8)
    public Double amount_btc;
    
    @Column(precision=15,scale=8)
    public Double amount_eth;
    
    @Column(precision=15,scale=8)
    public Double amount_usdt;
    
    public Double market_price;//market price
    public Double market_amount;//market amount
    
    @Column(precision=15,scale=8,nullable=false)
    public Double fee;
    
    @Column(precision=15,scale=8)
    public Double fee_btc;
    
    @Column(precision=15,scale=8)
    public Double fee_eth;
    
    @Column(precision=15,scale=8)
    public Double fee_usdt;
    //public Double fee_currency;
    
    public String strategy;
    public long created_at_timestamp;
    
    @Formats.DateTime(pattern="yyyy-MM-dd hh:mm:ss")
    public Date created_at;
    
    @Formats.DateTime(pattern="yyyy-MM-dd hh:mm:ss")
    public Date updated_at;
    
//    @ManyToOne
//    public Robot robot;
//    @ManyToOne
//    public Exchange exchange;
//    @ManyToOne
//    public User user;
    
    public static final Finder<Long, Trade> find = new Finder<>(Trade.class);

}