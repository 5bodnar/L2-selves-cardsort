package ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import ru.fasop.QuestionnaireItem;
import ru.fasop.TestSubject;

public class PostQuestionnaireSession extends Session {
	List<PageViewEventCollection> pViewCollections = new ArrayList<PageViewEventCollection>();
	
	public PostQuestionnaireSession(){}
	
	public PostQuestionnaireSession(Session session){
		this.id = session.id;
		this.subj = session.subj;
		this.timestamp = session.timestamp;
		this.events = session.events;
		
		SessionLogProcessor proc = new SessionLogProcessor(session);
		proc.seekLogin();
		
		proc.seekNextPageViewEvent();
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		while(null != eventCollection){
			pViewCollections.add(eventCollection);
			
			eventCollection =  proc.buildPageViewEventCollection();
		}
	}
	
	public PostQuestionnaireSession(TestSubject subj, Long timestamp) {
		super(subj, timestamp);
	}
	
	public List<Long> getQuestionsAnswered(){
		List<Long> ids = new ArrayList<Long>();
			
		for(PageViewEventCollection pvc : pViewCollections){
			for(LogEvent e : pvc.events){
				if(LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM.equals(e.act)){
					String id = e.getParam("questionId");
							
					if(null == id){
						id = e.getParam("question_id");
					}
					
					if(! ids.contains(new Long(id))){
						ids.add(new Long(id));
					}
				}
			}
		}
		
		return ids;
	}
	
	public List<Long> getQuestionsAnswered(String type){
		List<Long> ids = new ArrayList<Long>();
			
		for(PageViewEventCollection pvc : pViewCollections){
			for(LogEvent e : pvc.events){
				if(LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM.equals(e.act)
						&& type.equals(e.getParam("question_type"))){
					String id = e.getParam("questionId");
							
					if(null == id){
						id = e.getParam("question_id");
					}
					
					if(! ids.contains(new Long(id))){
						ids.add(new Long(id));
					}
				}
			}
		}
		
		return ids;
	}
	
	public String getLatestAnswerForQuestion(String qid){
		long latestTimestamp = 0;
		
		String value = "-1";
		
		for(PageViewEventCollection pvc : pViewCollections){
			for(LogEvent e : pvc.events){
				if(LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM.equals(e.act)
					&& latestTimestamp < e.timestamp.longValue()){

					String id = e.getParam("questionId");
							
					if(null == id){
						id = e.getParam("question_id");
					}
					
					if(qid.equals(id)){
						latestTimestamp = e.timestamp;
						value  = getQuestionnaireValue(e);
					}
				}
			}
		}
		
		return value;
	}
	
	public String getQuestionnaireValue(LogEvent e){
		if("likert".equals(e.getParam("question_type"))){
			if("Zeer mee eens".equals(e.getParam("value"))){
				return "1";
			}
			else if("Mee eens".equals(e.getParam("value"))){
				return "2";
			}
			else if("Neutraal".equals(e.getParam("value"))){
				return "3";
			}		
			else if("Oneens".equals(e.getParam("value"))){
				return "4";
			}
			else if("Zeer oneens".equals(e.getParam("value"))){
				return "5";
			}
			else {
				return e.getParam("value");
			}
		}
		else {
			return e.getParam("value");
		}	
	}
}
