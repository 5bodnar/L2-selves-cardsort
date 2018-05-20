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
import javax.persistence.OrderColumn;

@Entity
public class FASOPUserTaskList {
	public static final Integer TYPE_SCHEDULED = 0;
	public static final Integer TYPE_ANYTIME = 1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String label;
	public Integer type;
	
	public String tasklistPresentationHTML;
	
	@ManyToMany(cascade=CascadeType.REFRESH)
	@OrderColumn
	public List<FASOPUserTask> tasks;

	public FASOPUserTaskList(){}
	
	public FASOPUserTaskList(String label, int type) {
		this.label = label;
		this.type = type;
		this.tasks = new ArrayList<FASOPUserTask>();
	}
	
	public FASOPUserTaskList clone(){
		FASOPUserTaskList list = new FASOPUserTaskList(label, type);
		list.tasklistPresentationHTML = tasklistPresentationHTML;
		list.tasks = tasks;
		
		return list;
	}
}
