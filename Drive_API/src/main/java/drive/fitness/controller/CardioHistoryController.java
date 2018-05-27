package drive.fitness.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import drive.fitness.dao.CardioHistoryDao;
import drive.fitness.dao.CompetingDao;
import drive.fitness.models.CardioHistory;
import drive.fitness.models.Competing;
import drive.fitness.models.LiftingHistory;

@RestController
public class CardioHistoryController {
	@Autowired
	private CardioHistoryDao cardioHistoryDao;

    @RequestMapping(value = "/getCardioHistoryByExercise", method= RequestMethod.GET)
    public List<CardioHistory> getCardioHistoryByExercise(@RequestParam(value = "userId", defaultValue = "test") int userId, @RequestParam(value = "exerciseName", defaultValue = "test") String exerciseName) {
        return cardioHistoryDao.getCardioHistoryByExercise(userId, exerciseName);
    }
    
    @RequestMapping(value = "/addCardioHistory", method= RequestMethod.POST)
    public void addCardioHistory(CardioHistory ch) {    	
    	cardioHistoryDao.save(ch);
    }
    
    @RequestMapping(value = "/deleteCardioHistory", method= RequestMethod.POST)
    public void deleteCardioHistory(CardioHistory ch) {    	
    	cardioHistoryDao.delete(ch);
    }
}
