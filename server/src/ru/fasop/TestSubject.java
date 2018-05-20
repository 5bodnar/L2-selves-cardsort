package ru.fasop;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import ru.fasop.motivation.L2SelfConceptInstance;

@Entity
public class TestSubject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String name;
	public String email;
	public String phoneNum;
	public String password;
	public String testgroup;
	public Boolean isAdmin = false;
	public Boolean isLoggedIn = false;
	
	public Long currentTaskIndex = new Long(0);
		
	@ManyToOne(cascade={CascadeType.REFRESH})
	public FASOPUserGroup group;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<L2SelfConceptInstance> hopedForSelfConceptInstances;	
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<L2SelfConceptInstance> fearedSelfConceptInstances;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<L2SelfConceptInstance> unimportantSelfConceptInstances;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@OrderBy("id")
	public FASOPUserTaskList tasklist;

	public TestSubject(){}
	
	public TestSubject(String name, String email, String phoneNum, String password) {
		this.name = name;
		this.email = email;
		this.phoneNum = phoneNum;
		this.password = password;
	}
	
	public L2SelfConceptInstance getLatestHopesInstance(){
		if(0 < hopedForSelfConceptInstances.size()){
			return hopedForSelfConceptInstances.get(hopedForSelfConceptInstances.size() - 1);
		}
		else {
			return null;
		}
	}
	
	public L2SelfConceptInstance getLatestFearsInstance(){
		if(0 < fearedSelfConceptInstances.size()){
			return fearedSelfConceptInstances.get(fearedSelfConceptInstances.size() - 1);
		}
		else {
			return null;
		}
	}
	
	public boolean equals(Object other){
		if(null == other){
			return false;
		}
		else if(! (other instanceof TestSubject)){
			return false;
		}
		
		TestSubject otherOne = (TestSubject) other;
		
		return id.equals(otherOne.id);
	}
}
