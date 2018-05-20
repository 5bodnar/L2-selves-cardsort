package ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireResult {
	
	public static final String TEST_TYPE_PRE = "PRE";
	public static final String TEST_TYPE_POST = "POST";
	public static final String TEST_TYPE_MINI_MOTIVATION = "MINI_MOTIVATION";
	
	public String type;
	public String description;
	public Long questionSetId;
	
	public List<LogEvent> events = new ArrayList<LogEvent>();

	public QuestionnaireResult(String testType, String description, Long id) {
		this.questionSetId = id;
		this.type = testType;
		this.description = description;
	}

	public List<LogEvent> getQuestionnaireAnswerEventsByQuestionIDs(List<Long> questionIds) {
		List<LogEvent> answerEvents = new ArrayList<LogEvent>();
		
		for(LogEvent e : events){
			if(questionIds.contains(new Long(e.getParam("questionId")))){
				answerEvents.add(e);
			}
		}
		
		return answerEvents;
	}
	
	public List<LogEvent> getQuestionnaireAnswerEventsByQuestionType(String questionType) {
		List<LogEvent> answerEvents = new ArrayList<LogEvent>();
		
		for(LogEvent e : events){
			if(questionType.equals(e.getParam("question_type"))){
				answerEvents.add(e);
			}
		}
		
		return answerEvents;
	}
	
	public String getValueByQuestionId(Long id, boolean useLastAnswerOnly){
		String answer = "";
		
		for(int i = 0; i < events.size(); i++){
			LogEvent e = events.get(i);

			if("answer_questionnaire_item".equals(e.act) && id.equals(new Long(e.getParam("questionId")))){
				if(useLastAnswerOnly){
					answer = e.getParam("value");
				}
				else {
					answer += e.getParam("value");
					
					if(i < events.size() -1){
						answer += ", ";
					}
				}
			}
		}
		
		return answer;
	}
	
	public String toString(){
		StringBuffer strb = new StringBuffer("[\n");
		
		for(LogEvent e : events){
			strb.append(e + "\n");
		}
		
		strb.append("\n]\n");
		
		return strb.toString();
	}
	
}
