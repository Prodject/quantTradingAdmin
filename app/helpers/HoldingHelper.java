package helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HoldingHelper {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat dateFormatMD = new SimpleDateFormat("MM/dd");

    public static String getDateTimePlusDay(int days) {
        Date currentDate = new Date();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.add(Calendar.DATE, days); 
        
        Date currentDatePlusDay = calendar.getTime();
        
        return dateFormat.format(currentDatePlusDay);       
    }

    public static String getDateTimePlusDayMD(int days) {
        Date currentDate = new Date();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.add(Calendar.DATE, days); 
        
        Date currentDatePlusDay = calendar.getTime();
        
        return dateFormatMD.format(currentDatePlusDay);       
    }    
    
	    
    
    // public Result getHoldings() {
    //   List<Holding> holdings = Holding.find.all();

    //   Holding holdings = Ebean.find(Holding.class, 1);

    //   //return ok(toJson(holdings));        
    // } 

  // public Result getHoldingsManually() {
  //       int k=1;
  //        ArrayList daysArray = new ArrayList();
  //          k=1;
  //         while(k<=31) {
  //           daysArray.add(k);
  //           k++;
  //         }
  //       Map<String, ArrayList> mapDays = new HashMap<String, ArrayList>();
  //       mapDays.put("days", daysArray);

  //       //-------------------------------------------------
  //       ArrayList cryptoCurrencyBtc = new ArrayList();
  //          k=1;
  //         while(k<=31) {
  //           cryptoCurrencyBtc.add(User.getRandom());
  //           k++;
  //         } 
    
  //       Map<String, ArrayList> btcChilds = new HashMap<String, ArrayList>();
  //       btcChilds.put("quantity", cryptoCurrencyBtc);

  //       ArrayList cryptoCurrencyEth = new ArrayList();
  //          k=1;
  //         while(k<=31) {
  //           cryptoCurrencyEth.add(User.getRandom());
  //           k++;
  //         } 
                      
  //       Map<String, ArrayList> ethChilds = new HashMap<String, ArrayList>();
  //       ethChilds.put("quantity", cryptoCurrencyEth);

  //       Map<String, Map> map = new HashMap<String, Map>();
  //       map.put("BTC", btcChilds);
  //       map.put("ETH", ethChilds);
  //       map.put("ETC", btcChilds);
  //       map.put("BCH", ethChilds);
  //       map.put("LTC", btcChilds);
  //       //-----------------------------------------------
  //       Map<String, Map> holdings = new HashMap<String, Map>();
  //       holdings.put("cryptoCurrencies", map);
  //       holdings.put("days", mapDays);

  //     return ok(toJson(holdings));  
  //   }  
}