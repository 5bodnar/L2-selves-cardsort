package ru.fasop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;

@Entity
public class FASOPUserGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String name;
	
	@OneToMany(cascade=CascadeType.REFRESH)
	@OrderBy("id")
	public List<TestSubject> userList;
	
	public FASOPUserGroup(){}
	
	public FASOPUserGroup(String name) {
		this.name = name;
		this.userList = new ArrayList<TestSubject>();
	}

	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}

	public boolean containsUser(TestSubject subj) {
		for(int i = 0; i < userList.size(); i++){
			TestSubject tempSubj = userList.get(i);
			
			if(subj.id == tempSubj.id
					&& subj.email.equals(tempSubj.email)){
				return true;
			}
		}
		
		return false;
	}
}
