package ru.fasop;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

@Entity
public class TestSpot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	@ManyToOne(cascade=CascadeType.REFRESH)
	@OrderBy("id")
	public TestSubject subj;
	
	public Boolean isCompleted = false;
	
	public boolean isTaken() {
		return null != subj;
	}

	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}

}
