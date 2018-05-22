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
 * Manage a database of computers
 */
//@With(SecuredAction.class)
@Security.Authenticated(Secured.class)
public class HoldingsController extends Controller {

    private final UserRepository userRepository;    
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final Jedis jedis;
    private final Config config;
    
    @Inject
    public HoldingsController(FormFactory formFactory,UserRepository userRepository, HttpExecutionContext httpExecutionContext, Config config) {
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
    public Result index() {

    	String category = "整体账户分析";
    	
    	return ok(views.html.robots.indexbyoverall.render(category)); 	        
    }    
        
    public Result indexbyrobot() {
    	String category = "分账户分析";
    	     	
    	Set<Holding> robots = Ebean.find(Holding.class)
    	  .select("robot_name")    	  
    	  .setDistinct(true)
    	  .where().isNotNull("robot_name")
    	  .findSet();    	
    	
        return ok(views.html.robots.indexbyrobot.render(category,robots));
    } 
    
    public Result indexbyexchange() {
    	String category = "分交易所分析";
    	//List<Exchange> exchanges = Exchange.find.all();  
    	Set<Holding> exchanges = Ebean.find(Holding.class)
    	    	  .select("exchange_name")    	  
    	    	  .setDistinct(true)
    	    	  .where().isNotNull("exchange_name")
    	    	  .findSet();
    	
        return ok(views.html.robots.indexbyexchange.render(category, exchanges));
    } 
        
    public Result indexbycurrency() {
    	String category = "分货币对分析";    	
    	
    	Set<Holding> cryptoCurrencies = Ebean.find(Holding.class)
  	    	  .select("symbol")    	  
  	    	  .setDistinct(true)
  	    	  .where().isNotNull("symbol")
  	    	  .findSet();
    	
        return ok(views.html.robots.indexbycurrency.render(category,cryptoCurrencies));
    } 
     
    public Result indexbystrategy() {
    	String category = "分策略分析";
        return ok(views.html.robots.indexbystrategy.render(category));
    }    
    
    /** 
    ** SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
    **/
    public Result getHoldings(String holdingType,String field, String categoryContent) {        
        Date today = new Date();
        
        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat formatMD = new SimpleDateFormat ("MM/dd");
        DecimalFormat decimalFormat = new DecimalFormat("#.########");
        Random random = new Random();
        //--------------------------------------------------------
        Map<String, String> cryptoCurrenciesName = new HashMap<String, String>();
        Map<String, String> cryptoCurrenciesColor = new HashMap<String, String>();
        //--------------------------------------------------------
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
        List<Holding> holdings;
        if(field.equals("all")) {
            holdings = Holding.find.query().where()
                    .between("created_at", HoldingHelper.getDateTimePlusDay(-30), format.format(today))
                    .eq("user_name", session("userName"))
                    .orderBy("symbol,created_at  Asc")
                    //.fetch("robot")
                    .findList();        	
        } else {
            holdings = Holding.find.query().where()
                    .between("created_at", HoldingHelper.getDateTimePlusDay(-30), format.format(today))
                    .eq(field, categoryContent)
                    .eq("user_name", session("userName"))
                    .orderBy(field+",created_at  Asc")
                    //.fetch("robot")
                    .findList();
        }
      //-----------------from database ----------------------------
        Map<String, String> cryptoCurrenciesQty = new HashMap<String, String>();
        Map<String, String> cryptoCurrenciesRate = new HashMap<String, String>();
        String type = "";
        int k = 1;
        int lindex = 1;
        Double previousQty= 0.0;
        Double currentQty= 0.0;
        for(Holding holding : holdings) {
        	
        	type = holding.symbol;
        	if (holdingType.equals("robot")) {
        		type = holding.robot_name;
        		if (cryptoCurrenciesColor.containsKey(type) == false && type != null) {
		        	cryptoCurrenciesName.put(type, type);
		        	cryptoCurrenciesColor.put(type, this.jedis.lindex("chartDefinedColors", lindex));
		        	lindex++;
        		}
    		} else if (holdingType.equals("exchange")) {
    			type = holding.exchange_name;
    			if (cryptoCurrenciesColor.containsKey(type) == false && type != null) {
		        	cryptoCurrenciesName.put(type, type);
		        	cryptoCurrenciesColor.put(type, this.jedis.lindex("chartDefinedColors", lindex));
		        	lindex++;
    			}
    		} else {
    			if (cryptoCurrenciesColor.containsKey(holding.symbol) == false && type != null) {
    	        	cryptoCurrenciesName.put(holding.symbol, holding.symbol);
    	        	cryptoCurrenciesColor.put(holding.symbol, this.jedis.lindex("chartDefinedColors", lindex));
    	        	lindex++;
    	        }
    		}
        	
        	 cryptoCurrenciesQty.put(type+"_"+formatMD.format(holding.created_at), decimalFormat.format(holding.qty));
        	 //growthRate Growth rate curve
        	 previousQty=currentQty;
        	 currentQty= holding.qty;
        	 if (k>1) {
        		 if(currentQty >0 && previousQty> 0) {        			 
        			         			  
        			 cryptoCurrenciesRate.put(type+"_"+formatMD.format(holding.created_at), decimalFormat.format(currentQty-previousQty));
        		   } 
        		}        	 
        	 k++;
    	}
        
        holdingsJson.put("cryptoCurrenciesReal", cryptoCurrenciesQty);
        holdingsJson.put("holdingGrowthRate", cryptoCurrenciesRate);
                        
//    	if (holdingType.equals("robot")) {
//    		List<Robot> cryptoCurrencies = Robot.find.all();
//	        for(Robot cryptoCurrency : cryptoCurrencies) {  
//	        	cryptoCurrenciesName.put(cryptoCurrency.name, cryptoCurrency.name);
//	        	cryptoCurrenciesColor.put(cryptoCurrency.name, cryptoCurrency.color);
//	        }    		
//		} else if (holdingType.equals("exchange")) {
//			List<Exchange> cryptoCurrencies = Exchange.find.all();
//	        for(Exchange cryptoCurrency : cryptoCurrencies) {  
//	        	cryptoCurrenciesName.put(cryptoCurrency.name, cryptoCurrency.name);
//	        	cryptoCurrenciesColor.put(cryptoCurrency.name, cryptoCurrency.color);
//	        }
		//} //else {
//			List<CryptoCurrencies> cryptoCurrencies = CryptoCurrencies.find.all();
//	        for(CryptoCurrencies cryptoCurrency : cryptoCurrencies) {  
//	        	cryptoCurrenciesName.put(cryptoCurrency.name, cryptoCurrency.name);
//	        	cryptoCurrenciesColor.put(cryptoCurrency.name, cryptoCurrency.color);
//	        }
		//}  
    	
        holdingsJson.put("cryptoCurrenciesName", cryptoCurrenciesName);
        holdingsJson.put("cryptoCurrenciesColor", cryptoCurrenciesColor);
        
      return ok(toJson(holdingsJson));        
    }    
    
    /** BTC":"red","ETH":"black","ETC":"orange","BCH":"green","LTC":"yellow"
    **/
    public Result getHoldingsManually() {
        int k=1;
         ArrayList daysArray = new ArrayList();
           k=1;
          while(k<=31) {
            daysArray.add(k);
            k++;
          }
        Map<String, ArrayList> mapDays = new HashMap<String, ArrayList>();
        mapDays.put("days", daysArray);

        //-------------------------------------------------
        ArrayList cryptoCurrencyBtc = new ArrayList();
           k=1;
          while(k<=31) {
            cryptoCurrencyBtc.add(User.getRandom());
            k++;
          } 
    
        Map<String, ArrayList> btcChilds = new HashMap<String, ArrayList>();
        btcChilds.put("quantity", cryptoCurrencyBtc);

        ArrayList cryptoCurrencyEth = new ArrayList();
           k=1;
          while(k<=31) {
            cryptoCurrencyEth.add(User.getRandom());
            k++;
          } 
                      
        Map<String, ArrayList> ethChilds = new HashMap<String, ArrayList>();
        ethChilds.put("quantity", cryptoCurrencyEth);

        Map<String, Map> map = new HashMap<String, Map>();
        map.put("BTC", btcChilds);
        map.put("ETH", ethChilds);
        map.put("ETC", btcChilds);
        map.put("BCH", ethChilds);
        map.put("LTC", btcChilds);
        //-----------------------------------------------
        Map<String, Map> holdings = new HashMap<String, Map>();
        holdings.put("cryptoCurrencies", map);
        holdings.put("days", mapDays);

      return ok(toJson(holdings));  
    }

    public Result getHoldingsManuallyOne() {
       
        ArrayList cryptoCurrencyTwo = new ArrayList();
           
          cryptoCurrencyTwo.add(User.getRandom());
          cryptoCurrencyTwo.add(User.getRandom());
          cryptoCurrencyTwo.add(User.getRandom());
             
        Map<String, ArrayList> childs = new HashMap<String, ArrayList>();
        childs.put("qty", cryptoCurrencyTwo);

        Map<String, Map> map = new HashMap<String, Map>();
        map.put("BTC", childs);
        map.put("ETH", childs);
        map.put("ETC", childs);

      return ok(toJson(map));        
    }  

    /**
     * Display the paginated list of computers.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
//    public CompletionStage<Result> list(int page, String sortBy, String order, String filter) {
//    	//logger.info("I am kevin-----------------");
//    	//System.out.println("list of data");
//    	//User user = User.find.byId(1L);
//    	//System.out.println(user.name);
//    	//------------------------------
//    	// Find all tasks
//    	//List<User> users = User.find.all();  
//    	
//    	//System.out.println(user.id);	
// 	  	   
//        // Run a db operation in another thread (using DatabaseExecutionContext)
//        return userRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(list -> {
//            // This is the HTTP rendering thread context
//        	//sub folders
//            return ok(views.html.robots.userlist.render(list));
//        }, httpExecutionContext.current());
//    }
    
	  /**
	   * True if there is a logged in user, false otherwise.
	   * @param ctx The context.
	   * @return True if user is logged in.
	   */
	  public static boolean isLoggedIn() {
		  String email = session("email");

	    return (email != null);
	  }	
}
