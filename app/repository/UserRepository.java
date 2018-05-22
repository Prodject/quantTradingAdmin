package repository;

import io.ebean.*;

import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import models.User;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import helpers.UtilityHelper;
/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class UserRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public UserRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Return a paged list of user
     *
     * @param page     Page to display
     * @param pageSize Number of computers per page
     * @param sortBy   Computer property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     */
    public CompletionStage<PagedList<User>> page(int page, int pageSize, String sortBy, String order, String filter) {
        return supplyAsync(() ->
                ebeanServer.find(User.class).where()
                    .ilike("name", "%" + filter + "%")
                    .orderBy(sortBy + " " + order)
                    .setFirstRow(page * pageSize)
                    .setMaxRows(pageSize)
                    .findPagedList(), executionContext);
    }
    
    public CompletionStage<Optional<User>> lookup(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(User.class).setId(id).findOne()), executionContext);
    }

    public void save(User user) {
                                    
         ebeanServer.insert(user);
         
    }    
    
    public CompletionStage<Optional<Long>>  delete(Long id) {
        return supplyAsync(() -> {
            try {
                final Optional<User> userOptional = Optional.ofNullable(ebeanServer.find(User.class).setId(id).findOne());
                userOptional.ifPresent(Model::delete);
                return userOptional.map(c -> c.id);
            } catch (Exception e) {
                return Optional.empty();
            }
        }, executionContext);
    }    
    
	public boolean isValid(String email, String password) {
		
		User user = ebeanServer.find(User.class)
				.where()
				.eq("email", email)
				.eq("password", UtilityHelper.md5String(password))
				.findOne();
		
	  return ((email != null) 
	          &&
	          (password != null) 
	          && (user != null));
	}

    public CompletionStage<Optional<Long>> update(Long id, User userData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Long> value = Optional.empty();
            try {
                User user = ebeanServer.find(User.class).setId(id).findOne();
                if (user != null) {
                	
                	user.email = userData.email;
                	user.name = userData.name;
                	user.role = userData.role;
                	
                	user.update();
                    txn.commit();
                    value = Optional.of(id);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }
	   
}
