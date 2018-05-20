package ru.fasop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FASOPUserTask {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String label;
	public String initialMessage;
	public String instructionsHTML;
	public String debriefHTML;
	public String controller;
	
	public Integer timeLimitInMinutes;
	
	public List<TestPeriod> testPeriods;
	
	public Boolean skipHomeScreen = false;
	public Boolean showInstructions = true;
	public Boolean showDebriefScreen = true;
	
	public FASOPUserTask(){}
	
	public FASOPUserTask(String label, String initialMessage, String controller, Integer timeLimitInMinutes) {
		this.label = label;
		this.initialMessage = initialMessage;
		this.controller = controller;
		this.timeLimitInMinutes = timeLimitInMinutes;
		this.testPeriods = new ArrayList<TestPeriod>();
	}
	
	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}
	
	public FASOPUserTask clone(){
		FASOPUserTask clone = new FASOPUserTask(label, initialMessage, controller, timeLimitInMinutes);
		
		clone.instructionsHTML = instructionsHTML;
		clone.debriefHTML = debriefHTML;
		clone.testPeriods = testPeriods;
		clone.skipHomeScreen = skipHomeScreen;
		clone.showInstructions = showInstructions;
		clone.showDebriefScreen = showDebriefScreen;
		
		return clone;
	}
}
