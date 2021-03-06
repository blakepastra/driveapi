package drive.fitness.models;


import java.sql.ResultSet;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import org.hibernate.mapping.Array;
import org.hibernate.mapping.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="workout")
@NamedStoredProcedureQueries({
@NamedStoredProcedureQuery(name = "getCompetingWorkouts", 
                           procedureName = "get_competing_workouts",
                           resultClasses=Workout.class,
                           parameters = {
                               @StoredProcedureParameter(mode = ParameterMode.IN, name = "user_id", type = int.class),
                               @StoredProcedureParameter(mode = ParameterMode.IN, name = "start_index", type = int.class),
                               @StoredProcedureParameter(mode = ParameterMode.IN, name = "end_index", type = int.class),
                           }),
@NamedStoredProcedureQuery(name = "getTimechartData", 
						   procedureName = "get_timechart_data",
						   parameters = {
							   @StoredProcedureParameter(mode = ParameterMode.IN, name = "work_id", type = int.class),
						   })
})
public class Workout {
	
	@Id
	@Column(name="id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="users_id")
	@JsonProperty("user")
	private User user;
	
	@Column(name="start_time")
	private Date startTime;
	
	@Column(name="end_time")
	private Date endTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUserId() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}