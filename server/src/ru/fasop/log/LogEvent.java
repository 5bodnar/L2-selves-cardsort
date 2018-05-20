package ru.fasop.log;

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
public class LogEvent implements Comparable<LogEvent> {
	public final static String ACTOR_UNKNOWN = "unknown";
	public final static String ACTOR_USER = "user";
	public final static String ACTOR_SYSTEM = "system";
	
	public final static String ACT_USER_VIEW_PAGE = "user_view_page";
	public final static String ACT_USER_LOGIN = "user_login";
	public final static String ACT_USER_LOGOUT = "user_logout";
	public final static String ACT_USER_RATE_CORRECTNESS = "rate_correctness";
	public final static String ACT_USER_ANSWER_QNNAIRE_ITEM = "answer_questionnaire_item";
	public final static String ACT_SYS_TIME_EXPIRED = "time_expired";
	public final static String ACT_USER_START_RECORD = "start_record";
	public final static String ACT_USER_STOP_RECORD = "stop_record";
	public final static String ACT_SYS_SAVE_AUDIO = "save_audio";
	public final static String ACT_USER_PRESS_BUTTON = "press_button";
	public final static String ACT_USER_SKIP_QUESTION = "skip_question";
	public final static String ACT_SYSTEM_REQUEST_RECOG = "request_recognition";
	public final static String ACT_SYSTEM_GIVE_CF = "give_cf";
	public final static String ACT_SYSTEM_LOG_ERROR = "log_error";
	
	public final static String PARAM_USER = "user";
	public final static String PARAM_PAGE_ID = "pageId";
	public final static String PARAM_USER_ID = "user_id";
	public final static String PARAM_QUESTION_SET_ID = "question_set_id";
	public final static String PARAM_QUESTION_ID = "question_id";
	public final static String PARAM_QUESTION_CONTENTS = "questionContent";
	public final static String PARAM_CORRECTNESS_VAL = "correctness_val";
	public final static String PARAM_FILENAME = "filename";
	public static final String PARAM_FEEDBACK_TYPE = "type_of_feedback";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public Long timestamp;
	public String actor;
	public String act;
	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("id")
	public List<LogEventParam> params;

	public LogEvent(){}
	
	public LogEvent(long timestamp, String actor, String act, List<LogEventParam> params) {
		this.timestamp = new Long(timestamp);
		this.actor = actor;
		this.act = act;
		this.params = params;
	}
	
	public LogEvent clone(){
		LogEvent clone = new LogEvent(timestamp, actor, act, null);
		
		clone.params = new ArrayList<LogEventParam>();
		
		for(LogEventParam p : params){
			clone.params.add(p.clone());
		}
		
		return clone;
	}
	
	public String getParam(String name){
		for(LogEventParam p : params){
			if(name.equals(p.name))
				return p.value;
		}
		
		return null;
	}

	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}
	
	public String toString(){
		StringBuffer strb = new StringBuffer();
		
		strb.append("[" + id + "," + actor + "," + act + "," + timestamp + ", ");
		
		strb.append("(");
		
		for(LogEventParam p : params){
			strb.append(p.name + "=" + p.value + ",");
		}
		
		strb.append(")");
		
		strb.append("]");
		return strb.toString();
	}
	
	public boolean equals(Object other){
		if(null == other){
			return false;
		}
		else if(! (other instanceof LogEvent)){
			return false;
		}
		
		LogEvent otherEvent = (LogEvent) other;
		
		if(this.id.equals(otherEvent.id)){
			return this.toString().equals(otherEvent.toString());
		}
		else{
			return false;
		}
	}

	@Override
	public int compareTo(LogEvent other) {
		if (this == other) return 0;
		
		if(id < other.id){
			return -1;
		}
		else {
			return 1;
		}
	}
}
