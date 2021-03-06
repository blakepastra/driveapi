package drive.fitness.controller;

import org.springframework.web.bind.annotation.RestController;


import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import drive.fitness.dao.*;
import drive.fitness.models.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import drive.fitness.s3.S3Wrapper;

@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class UserController {
	private static final Logger logger = LogManager.getLogger(HistoryController.class);
	
	@Autowired
	private S3Wrapper s3Wrapper;

	@Autowired
	private UserDao userDao;
	
	@PersistenceContext
    private EntityManager em;
	
	@CrossOrigin
	@RequestMapping(value = "/checkAuthentication", method= RequestMethod.GET)
    public String checkAuthentication() {
    	return "SUCCESS";
    }

    @RequestMapping(value = "/getAllUsers", method= RequestMethod.GET)
    @CrossOrigin
    public List<User> getAllUsers() {
    	List<User> users = (List<User>) userDao.findAll();
    	for (User user : users) {
    		try {
				user.setProfilePic("data:image/jpeg;base64," + this.getUserProfilePicString(user.getUsername()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        return users;
    }
    
    @RequestMapping(value = "/getFilteredUsersSearch", method= RequestMethod.GET)
    @CrossOrigin
    public List<User> getFilteredUsersSearch(@RequestParam(value = "searchTerm", defaultValue = "test") String searchTerm) {
    	List<User> users = (List<User>) userDao.getFilteredUsersSearch(searchTerm);
    	for (User user : users) {
    		try {
				user.setProfilePic("data:image/jpeg;base64," + this.getUserProfilePicString(user.getUsername()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        return users;
    }
    
    @CrossOrigin
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUsers", method= RequestMethod.GET)
    public List<User> getUsers() {
    	StoredProcedureQuery findByYearProcedure =
                em.createNamedStoredProcedureQuery("getUsers");
    	return findByYearProcedure.getResultList();
    }
    
    @GetMapping(value = "/getUserByEmail")
    @CrossOrigin
    public User getUserByEmail(@RequestParam(value = "email", defaultValue = "test") String email) {
    	User user = userDao.findByEmailIgnoreCase(email);
    	if (user == null) {
    		user = new User();
    		user.setEmail("doesnt_exist");
    	}
        return user;
    }
    
    @SuppressWarnings("unchecked")
    @CrossOrigin
	@RequestMapping(value = "/getUserCompeting", method= RequestMethod.GET)
    public List<User> getUserCompeting(@RequestParam(value = "userId", defaultValue = "test") int userId) {
    	StoredProcedureQuery query =
                em.createNamedStoredProcedureQuery("getUserCompeting");
    	query.setParameter("user_id", userId);
    	return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @CrossOrigin
	@RequestMapping(value = "/getUserCompetitors", method= RequestMethod.GET)
    public List<User> getUserCompetitors(@RequestParam(value = "userId", defaultValue = "test") int userId) {
    	StoredProcedureQuery query =
                em.createNamedStoredProcedureQuery("getUserCompetitors");
    	query.setParameter("user_id", userId);
    	return query.getResultList();
    }
    
    @RequestMapping(value = "/getUser", method= RequestMethod.GET)
    @CrossOrigin
    public User getUser(@RequestParam(value = "username", defaultValue = "test") String username) {
    	return userDao.getUser(username);
    }
    
    @RequestMapping(value = "/getUserId", method= RequestMethod.GET)
    @CrossOrigin
    public int getUserId(@RequestParam(value = "username", defaultValue = "test") String username) {
    	return userDao.getUserId(username);
    }

    @RequestMapping(value = "/getUserByUsername", method= RequestMethod.GET)
    @CrossOrigin
    public User getUserByUsername(@RequestParam(value = "username", defaultValue = "test") String username) {
    	User user = userDao.findByUsername(username);
    	if (user == null) {
    		user = new User();
    		user.setUsername("alreadyexists");
    	}
        return user;
    }
    
    @PostMapping(value = "/createUser")
    @CrossOrigin
    public User createUser(@RequestBody User user) {
        User newUser = userDao.save(user);
        return newUser;
    }
    
    @RequestMapping(value = "/getUserGainsTotal", method= RequestMethod.GET)
    @CrossOrigin
    public BigInteger getUserGainsTotal(@RequestParam(value = "userId", defaultValue = "test") int userId) {
    	StoredProcedureQuery query =
                em.createNamedStoredProcedureQuery("getUserGainsTotal");
    	query.setParameter("user_id", userId);
    	BigInteger result = (BigInteger)query.getSingleResult();
    	if(result == null) {
    		result = BigInteger.ZERO;
    	}
    	return result;
    }
    
    @RequestMapping(value = "/getUserGainsWeek", method= RequestMethod.GET)
    @CrossOrigin
    public BigInteger getUserGainsWeek(@RequestParam(value = "userId", defaultValue = "test") int userId) {
    	StoredProcedureQuery query =
                em.createNamedStoredProcedureQuery("getUserGainsWeek");
    	query.setParameter("user_id", userId);
    	return (BigInteger)query.getSingleResult();
    }
    
    @RequestMapping(value = "/getUserGainsMonth", method= RequestMethod.GET)
    @CrossOrigin
    public BigInteger getUserGainsMonth(@RequestParam(value = "userId", defaultValue = "test") int userId) {    	
    	StoredProcedureQuery query =
                em.createNamedStoredProcedureQuery("getUserGainsMonth");
    	query.setParameter("user_id", userId);
    	return (BigInteger)query.getSingleResult();
    }
    
    @RequestMapping(value = "/getUserGainsToday", method= RequestMethod.GET)
    public BigInteger getUserGainsToday(@RequestParam(value = "userId", defaultValue = "test") int userId) {    	
    	StoredProcedureQuery query =
                em.createNamedStoredProcedureQuery("getUserGainsToday");
    	query.setParameter("user_id", userId);
    	return (BigInteger)query.getSingleResult();
    }
    
    @RequestMapping(value = "/getLeaderboardData", method= RequestMethod.GET)
    @CrossOrigin
    public Iterable<User> getLeaderboardData(@RequestParam(value = "userId", defaultValue = "test") int userId) {    	
    	Iterable<User> competingUsers = new ArrayList<User>();
    	competingUsers = this.getUserCompeting(userId);
    	competingUsers.forEach((user) -> {
    		BigInteger gainsToday = this.getUserGainsToday(user.getId());
    		gainsToday = gainsToday != null ? gainsToday : BigInteger.ZERO;
    		BigInteger gainsWeek = this.getUserGainsWeek(user.getId());
    		gainsWeek = gainsWeek != null ? gainsWeek : BigInteger.ZERO;
    		BigInteger gainsMonth = this.getUserGainsMonth(user.getId());
    		gainsMonth = gainsMonth != null ? gainsMonth : BigInteger.ZERO;
    		BigInteger gainsTotal = this.getUserGainsTotal(user.getId());
    		gainsTotal = gainsTotal != null ? gainsTotal : BigInteger.ZERO;
    		user.setGainsToday(gainsToday);
    		user.setGainsWeek(gainsWeek);
    		user.setGainsMonth(gainsMonth);
    		user.setGainsTotal(gainsTotal);
    	});
    	return competingUsers;
    }
    
    @RequestMapping(value = "/getUserProfilePic", method= RequestMethod.GET)
    @CrossOrigin
    public @ResponseBody ResponseEntity<String> getUserProfilePic(@RequestParam(value = "username", defaultValue = "test") String username) throws IOException {
    	return s3Wrapper.download("profile_pics/" + username);
    }
    
    private String getUserProfilePicString(String username) throws IOException {
    	return s3Wrapper.download("profile_pics/" + username).getBody();
    }
    
    @RequestMapping(value = "/uploadUserProfilePic", method= RequestMethod.POST)
    @CrossOrigin
    public void uploadUserProfilePic(@RequestParam(value = "username", defaultValue = "test") String username, @RequestBody String pic) throws IOException {
    	s3Wrapper.uploadImage(pic, "profile_pics/" + username);
    }
}