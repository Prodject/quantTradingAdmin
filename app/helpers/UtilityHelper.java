package helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
 
import redis.clients.jedis.Jedis;

public class UtilityHelper {

	public static String md5String(String stringToHash) {		
        String generatedString = null;
        try {             
            MessageDigest md = MessageDigest.getInstance("MD5");
             
            md.update(stringToHash.getBytes());
             
            byte[] bytes = md.digest();
 
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            generatedString = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return generatedString;		
	}
	
	  /**
	   * True if there is a logged in user, false otherwise.
	   * @param ctx The context.
	   * @return True if user is logged in.
	   */
//	  public static boolean isLoggedIn() {
//		  String email = session("email");
//		  //HttpSession session = request.getSession(true);
//		  
//		  if (session.isNew()) {
//		         //title = "Welcome to my website";
//		         //session.setAttribute(userIDKey, userID);
//			  email = (String)session.getAttribute("email");
//	      } else {
//
//	    	  email = (String)session.getAttribute("email");
//	      }		  
//		  
//	    return (email != null);
//	  }
	
	/**
	 * 
	 * @param jedis
	 * @return
	 */
    public static Long chartDefinedColors(Jedis jedis)  {
    	//use sets
    	long result = 0;
    	Random random = new Random();
    	int maxCount = 150;    	
    	Long count = jedis.scard("chartDefinedColors");
    	if(count == 0) {
    		//this.jedis.del("chartDefinedColors");
        	for(int i= 1;i<=maxCount;i++) {
        		result = jedis.sadd("chartDefinedColors", String.format("#%06x", random.nextInt(256*256*256)));
        	}    		
    	}
    	return count;
    }	
	
	/**
	 * 
	 * @param jedis
	 * @return
	 */
    public static Long chartDefinedColorsList(Jedis jedis)  {
    	//use sets
    	long result = 0;
    	Random random = new Random();
    	int maxCount = 300;    	
    	Long count = jedis.llen("chartDefinedColors");
    	if(count == 0) {    		
        	for(int i= 1;i<=maxCount;i++) {
        		result = jedis.lpush("chartDefinedColors", String.format("#%06x", random.nextInt(256*256*256)));
        	}    		
    	}
    	return count;
    }
    
    public static String logContent() {
    	String content = "Introduction\n" + 
    			"NoSQL databases are often used when high writes and reads on a big amount of data is required or when the structure of data needs to be dynamic. Adopting a NoSQL database  needs careful considerations, especially regarding concurrency, isolation and consistency.\n" + 
    			"In the following section, a brief analysis of concurrency in MongoDB will be provided, to continue with an overview on consistency in different NoSQL databases.\n" + 
    			"\n" + 
    			"Discussion\n" + 
    			"This week App is based on PHP and MySQL. The application is a simple web app, which allows users to insert comments into the database. There are two PHP pages interacting with the database: form.php which after connection, selects all the rows in the comments table to display them, and comments.php, which inserts new rows.\n" + 
    			"\n" + 
    			"MySQL is a relational database system developed by Oracle Corporation. In MySQL, the tables are pre-defined and it is possible to interact with the database using the SQL language. Being a relational database, it is easy to store data in different tables related using joins. This allows avoiding data duplication.\n" + 
    			"\n" + 
    			"MongoDB is a NoSQL database that stores data in JSON-like format documents, which can have different structure. It is not a relational database and it uses dynamic schema. It is possible to change the structure of the tables and fields easily compared to RDBMS databases. The data are not normalized. MongoDB is designed for high availability and scalability.\n" + 
    			"\n" + 
    			"When choosing a database, it is important to define which the requirements for the application are. In particular, following the CAP theorem, it is important to check if consistency, availability or partition-tolerance are required. According to the CAP theorem, it is impossible to have the three at the same time: a database can be CA, AP or CP.\n" + 
    			"\n" + 
    			"In the case of our application, there might be several reasons why we would want to implement MongoDB. I am taking as example one: no rigid schema. Often a comment system of a blog does not need a rigid relational schema in place. The comments are usually always related to a post, so they can be inserted in a document type database. In addition, different fields can exist for different situations, without having to deal with NULL values in case they are not always present. For this reasons, using a NoSQL system, could help to increase performance and give more flexibility in case the structure should change.\n" + 
    			"\n" + 
    			"To convert our application to MongoDB, several steps would be required. I will consider that we are not going through a migration, but starting from scratch.\n" + 
    			"\n" + 
    			"MongoDB would need to be installed on the Ubuntu machine along with the MongoDB driver for PHP. From the coding point of view, it is not more complex how to connect to the database and perform operations. We can create the collection with the following command:";
    	return content;
    }
}
