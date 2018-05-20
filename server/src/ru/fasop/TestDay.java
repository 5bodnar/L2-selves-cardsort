package ru.fasop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class TestDay {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String date;
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<TimeSlot> timeslots;

	public TestDay(){}
	
	public TestDay(String testDay) {
		this.date = testDay;
	}

	public void addTimeslot(TimeSlot timeSlot) {
		if(null == this.timeslots)
			this.timeslots = new ArrayList<TimeSlot>();

		this.timeslots.add(timeSlot);
	}

	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}

}
