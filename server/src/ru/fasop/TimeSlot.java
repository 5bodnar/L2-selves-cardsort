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
import javax.persistence.Table;

@Entity
public class TimeSlot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String startTime;
	public String endTime;
	public int maxSpots;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<TestSpot> spots;
	
	public TimeSlot(){}
	
	public TimeSlot(String startTime, String endTime, int maxSpots) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.maxSpots = maxSpots;
		
		this.spots = new ArrayList<TestSpot>();
		for(int i = 0; i < maxSpots; i++)
			this.spots.add(new TestSpot());
	}

	public TestSpot getSpot(int i) {
		if(i < this.spots.size())
			return this.spots.get(i);
		else
			throw new IllegalArgumentException("Sorry pal, you tried to get a spot that wasn't there.  Index: " + i);
	}

	public boolean isFull() {
		for(int i = 0; i < this.spots.size(); i++)
			if(! this.spots.get(i).isTaken())
				return false;
		return true;
	}
	
	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}

}
