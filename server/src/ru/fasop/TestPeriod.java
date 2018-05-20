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
public class TestPeriod {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<TestDay> testdays;
	public String description;

	public TestPeriod(){}
	
	public TestPeriod(String description) {
		this.description = description;
	}

	public void addTestDay(TestDay td) {
		if(null == testdays)
			testdays = new ArrayList<TestDay>();
		
		testdays.add(td);
	}

	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}

}
