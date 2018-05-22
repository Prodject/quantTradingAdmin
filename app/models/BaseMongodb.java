package models;

import helpers.MongoConfig;

public class BaseMongodb {
    public void save() {
    	MongoConfig.datastore().save(this);
    }
    
    
}
