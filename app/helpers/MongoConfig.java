package helpers;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
 
//import com.typesafe.config.Config;
//import play.Configuration;

public class MongoConfig {

  private static Datastore datastore;
  //private final Config config;
  
//  private final Config config;
//  public MongoConfig(Config config) {
//      this.config = config;
//  }
  
	public static Datastore datastore() {
	    if (datastore == null) {
	        initDatastore();
	    }
	    return datastore;
	}

    public static void initDatastore() {
    	 Morphia morphia = new Morphia();
    	  
	      datastore = morphia.createDatastore(new MongoClient(), "viewfinquanttrading");
	      
	      datastore.ensureIndexes();    	
    }  
  
//    public static void initDatastore() {
//    final Morphia morphia = new Morphia();
//
//    // Tell Morphia where to find our models
//    morphia.mapPackage("models");
//
//    MongoClient mongoClient = new MongoClient(
//        ConfigFactory.load().getString("mongodb.host"),
//        ConfigFactory.load().getInt("mongodb.port"));
//
//    	datastore = morphia.createDatastore(
//        mongoClient, ConfigFactory.load().getString("mongodb.database"));       
//    }

}
