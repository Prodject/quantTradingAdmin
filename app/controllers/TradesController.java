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

import static play.libs.Json.toJson;
import static play.libs.Json.*;
import io.ebean.*;
import java.text.*;

import helpers.HoldingHelper;
import helpers.UtilityHelper;
import play.mvc.Security;
import com.typesafe.config.Config;
import redis.clients.jedis.Jedis;
/**
 * Manage a database of TradesController
 */
@Security.Authenticated(Secured.class)
public class TradesController extends Controller {

    private final UserRepository userRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final Jedis jedis;
    private final Config config;
    
    @Inject
    public TradesController(FormFactory formFactory,UserRepository userRepository, HttpExecutionContext httpExecutionContext, Config config) {
    	this.config = config;
    	this.userRepository = userRepository;
        this.formFactory = formFactory;        
        this.httpExecutionContext = httpExecutionContext;
	    this.jedis = new Jedis(this.config.getString("redis.default.host"));
	    this.jedis.auth(this.config.getString("redis.default.password"));
	    Long count = UtilityHelper.chartDefinedColorsList(this.jedis);        
    }
    
    /**
     * Handle default path requests, redirect to index list
     */
    public Result indexbyoverall() {
    	String category = "整体账户分析";    	 
        return ok(views.html.trades.indexbyoverall.render(category));
    }    
        
    public Result indexbyrobot() {
    	String category = "分账户分析";
    	    	
    	Set<Trade> robots = Ebean.find(Trade.class)
  	    	  .select("robot_name")    	  
  	    	  .setDistinct(true)
  	    	  .where().isNotNull("robot_name")
  	    	  .findSet();
    	
        return ok(views.html.trades.indexbyrobot.render(category,robots));
    } 
    
    public Result indexbyexchange() {
    	String category = "分交易所分析";
    	
    	Set<Trade> exchanges = Ebean.find(Trade.class)
  	    	  .select("exchange_name")    	  
  	    	  .setDistinct(true)
  	    	  .where().isNotNull("exchange_name")
  	    	  .findSet();
    	
        return ok(views.html.trades.indexbyexchange.render(category, exchanges));
    } 
    
    public Result indexbycurrency() {
    	String category = "分货币对分析";    	
    	
    	Set<Trade> cryptoCurrencies = Ebean.find(Trade.class)
    	    	  .select("pair")    	  
    	    	  .setDistinct(true)
    	    	  .where().isNotNull("pair")
    	    	  .findSet();
    	
        return ok(views.html.trades.indexbycurrency.render(category,cryptoCurrencies));
    } 
                 
	/** 
	** SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	**/
	public Result getTrades(String holdingType,String field, String categoryContent) {        

        Date today = new Date();

        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat formatMD = new SimpleDateFormat ("MM/dd");
        Random random = new Random();
        //--------------------------------------------------------
        Map<String, String> cryptoCurrenciesName = new HashMap<String, String>();
        Map<String, String> cryptoCurrenciesColor = new HashMap<String, String>();
        Map<String, ArrayList> dayJson = new HashMap<String, ArrayList>();
        
        ArrayList days = new ArrayList();
        ArrayList cryptoCurrencyBtc = new ArrayList();
        for (int i = 31; i > -1; i--) {                   	
        	days.add(HoldingHelper.getDateTimePlusDayMD(-i));
        	
        	cryptoCurrencyBtc.add(i);
        }       
        dayJson.put("days", days);
                
        //---------------------------------------------------        
        Map<String, Map> mapCurrencies = new HashMap<String, Map>();                
        Map<String, Map> holdingsJson = new HashMap<String, Map>();
        holdingsJson.put("days", dayJson);
        
        //-----------------from database ----------------------------
        List<SqlRow> trades;
        String sql;
        if(field.equals("all")) {
	        //currency	
	  		sql = "SELECT sum(amount) as amount,sum(fee) as fee,pair,DATE_FORMAT(created_at,\"%Y-%m-%d\") as created_at_modify FROM `trade` WHERE `created_at`>= :created_at and user_name=:user_name group by pair,created_at_modify ";
	     	
	    	if (holdingType.equals("robot")) {
	    		sql = "SELECT sum(amount) as amount,sum(fee) as fee,robot_id,DATE_FORMAT(created_at,\"%Y-%m-%d\") as created_at_modify FROM `trade` WHERE `created_at`>= :created_at and user_name=:user_name group by robot_id,created_at_modify ";
			} else if (holdingType.equals("exchange")) {
				sql = "SELECT sum(amount) as amount,sum(fee) as fee,exchange_id,DATE_FORMAT(created_at,\"%Y-%m-%d\") as created_at_modify FROM `trade` WHERE `created_at`>= :created_at and user_name=:user_name group by exchange_id,created_at_modify ";
			}
  		  
  		  SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
  		  sqlQuery.setParameter("created_at", HoldingHelper.getDateTimePlusDay(-30));
  		  sqlQuery.setParameter("user_name", session("userName"));
  		
  		  trades = sqlQuery.findList();
  		  
        } else {
        	String colums = holdingType;
        	 
        	if (holdingType.equals("currency")) {
        		colums = "pair";        		 
        	} else if (holdingType.equals("exchange")) {
        		colums = "exchange_id";
        	} else if (holdingType.equals("robot")) {
        		colums = "robot_id";
        	}
        	
	  		sql = "SELECT sum(amount) as amount,sum(fee) as fee,"+colums+",DATE_FORMAT(created_at,\"%Y-%m-%d\") as created_at_modify FROM `trade` WHERE `created_at`>= :created_at and "+field+"=:"+field+" and user_name=:user_name group by "+colums+",created_at_modify ";
	     	  		  		
	  		  SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	  		  sqlQuery.setParameter("created_at", HoldingHelper.getDateTimePlusDay(-30));
	  		  sqlQuery.setParameter(field, categoryContent);
	  		  sqlQuery.setParameter("user_name", session("userName"));
	  		   
      		  trades = sqlQuery.findList();
        }
        
        Map<String, String> cryptoCurrenciesQty = new HashMap<String, String>();
        Map<String, String> cryptoCurrenciesRate = new HashMap<String, String>();
        String type = "";
        int k = 1;
        Double previousQty= 0.0;
        Double currentQty= 0.0;
        for(SqlRow trade : trades) {        	
        	type = trade.getString("pair");
        	if (holdingType.equals("robot")) {        		
        		Robot robot = Robot.find.byId(trade.getLong("robot_id"));
        		type = robot.name;
	        	cryptoCurrenciesName.put(type, type);
	        	cryptoCurrenciesColor.put(type, this.jedis.lindex("chartDefinedColors", k));        		
    		} else if (holdingType.equals("exchange")) {    			
    			Exchange exchange = Exchange.find.byId(trade.getLong("exchange_id"));
        		type = exchange.name;  
	        	cryptoCurrenciesName.put(type, type);
	        	cryptoCurrenciesColor.put(type, this.jedis.lindex("chartDefinedColors", k));        		
    		} else {
	        	cryptoCurrenciesName.put(trade.getString("pair"), trade.getString("pair"));
	        	cryptoCurrenciesColor.put(trade.getString("pair"), this.jedis.lindex("chartDefinedColors", k));
    		}
        	
        	cryptoCurrenciesQty.put(type+"_"+formatMD.format(trade.getDate("created_at_modify")), Double.toString(trade.getDouble("amount")));
        	cryptoCurrenciesRate.put(type+"_"+formatMD.format(trade.getDate("created_at_modify")), Double.toString(trade.getDouble("fee")));
        	 
        	 k++;
        	 //System.out.println(k);
    	}
        
        holdingsJson.put("cryptoCurrenciesReal", cryptoCurrenciesQty);
        holdingsJson.put("holdingGrowthRate", cryptoCurrenciesRate);

        holdingsJson.put("cryptoCurrenciesName", cryptoCurrenciesName);
        holdingsJson.put("cryptoCurrenciesColor", cryptoCurrenciesColor);
 
      return ok(toJson(holdingsJson));          
	}  
	
	public Result queryByCreatedAt() {
		  String sql = "SELECT sum(amount) as amount,robot_id,DATE_FORMAT(created_at,\"%Y-%m-%d\") as created_at_modify FROM `trade` WHERE `created_at`>= :created_at group by `robot_id`,created_at_modify ";

		  SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
		  sqlQuery.setParameter("created_at", HoldingHelper.getDateTimePlusDay(-30));
 		  
		  List<SqlRow> trades = sqlQuery.findList();

		  Map<String, String> cryptoCurrenciesName = new HashMap<String, String>();
		  if (trades != null) {
			  for (SqlRow trade : trades) {

				  cryptoCurrenciesName.put("amount",trade.getString("amount"));
				  cryptoCurrenciesName.put("robot_id",trade.getString("robot_id"));
				  	
				  DateFormat dateFormat = new SimpleDateFormat("Y-mm-dd");  
				  String strDate = dateFormat.format(trade.getDate("created_at_modify"));
				  
				  cryptoCurrenciesName.put("created_at_modify",strDate);	
			  }
		  }
	    return ok(toJson(cryptoCurrenciesName));
	}
}
