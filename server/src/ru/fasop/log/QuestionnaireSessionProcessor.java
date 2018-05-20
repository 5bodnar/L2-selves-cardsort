package ru.fasop.log;

import java.util.HashMap;
import java.util.Map;

public class QuestionnaireSessionProcessor {
	public LogEvent prev2Event = null;
	public LogEvent prevEvent = null;
	public LogEvent curEvent = null;
	
	public int eventIndex = 0;
	public Session session = null;
	
	public boolean didReachDebriefPage = false;
	
	public QuestionnaireSessionProcessor(Session session) {
		this.session = session;
	}

	public void nextEvent() {
		prev2Event = prevEvent;
		prevEvent = curEvent;
		curEvent = eventIndex < session.events.size() ? session.events.get(eventIndex): null;
		eventIndex++;
		
		if(null != curEvent && ! didReachDebriefPage){
			didReachDebriefPage = "user_view_page".equals(curEvent.act) 
									&& "questionnaire_debrief_page".equals(curEvent.getParam("pageId"));
		}
	}

	public QuestionnaireResult process() {
		QuestionnaireResult res = null;
		
		try{
			nextEvent();
						
			res = new QuestionnaireResult(getResType(session.getLoginEvent()),
											session.getLoginEvent().getParam("question_set_desc"),
											Long.parseLong(session.getLoginEvent().getParam("question_set_id")));
			
			while(! didReachDebriefPage && eventIndex <= session.events.size()){
				if(LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM.equals(curEvent.act)){
					res.events.add(curEvent);
				}
								
				nextEvent();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	public QuestionnaireResult process(boolean includeLastAnswerOnly) {
		QuestionnaireResult res = null;
		
		Map<Long, LogEvent> eventMap = new HashMap<Long, LogEvent>();
		
		try{
			nextEvent();
						
			res = new QuestionnaireResult(getResType(curEvent),
											curEvent.getParam("question_set_desc"),
											Long.parseLong(curEvent.getParam("question_set_id")));
			
			while(! didReachDebriefPage && eventIndex <= session.events.size()){
				if(LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM.equals(curEvent.act)){
					eventMap.put(new Long(curEvent.getParam("questionId")), curEvent);
				}
								
				nextEvent();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		for(Long key : eventMap.keySet()){
			res.events.add(eventMap.get(key));
		}		
		
		return res;
	}

	public static String getResType(LogEvent e) {
		String testType = null;
		
		if(-1 != e.getParam("question_set_desc").toLowerCase().indexOf("pre")){
			testType = "pre";
		}
		else if(-1 != e.getParam("question_set_desc").toLowerCase().indexOf("post")){
			testType = "post";
		}
		else if(-1 != e.getParam("question_set_desc").toLowerCase().indexOf("motivational indicators")){
			testType = "mini_motivation";
		}
		else {
			testType = "unknown";
		}
		return testType;
	}
}
