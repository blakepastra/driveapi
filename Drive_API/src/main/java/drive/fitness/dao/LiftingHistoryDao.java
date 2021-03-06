package drive.fitness.dao;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import drive.fitness.models.LiftingHistory;
import drive.fitness.models.User;
import drive.fitness.models.records.LiftingRecord;

public interface LiftingHistoryDao extends CrudRepository<LiftingHistory, Integer>{ 
	@Procedure(name = "get_user_gains_total")
	public long getUserGainsTotal(@Param("user_id") int user_id);
	
	@Procedure(name = "get_user_gains_week")
	public long getUserGainsWeek(@Param("user_id") int user_id);
	
	@Procedure(name = "get_user_gains_month")
	public long getUserGainsMonth(@Param("user_id") int userId);
	
	@Procedure(name = "get_user_gains_today")
	public long getUserGainsToday(@Param("user_id") int userId);
	
	@Query("FROM LiftingHistory where userId=:userId")
	public List<LiftingHistory> getUserLiftingHistory(@Param("userId") int userId);
	
	@Query("FROM LiftingHistory where userId=:userId AND exercise.id=:id ORDER BY date")
	public List<LiftingHistory> getLiftingHistoryByExercise(@Param("userId") int userId, @Param("id") int id);
	
	@Query("FROM LiftingHistory where userId=:userId AND date>=:startTime AND date<=:endTime")
	public List<LiftingHistory> getUserLiftingHistoryBetween(@Param("userId") int userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	@Query(value="SELECT reps, weight, oneRepMax, records "
			   + "FROM get_lifting_records(:user_id,:exercise_id) AS foo(reps smallint, weight double precision, oneRepMax double precision, records bigint);", nativeQuery=true)
	public List<Object[]> getLiftingRecords(@Param("user_id") int user_id, @Param("exercise_id") int exercise_id);
}
