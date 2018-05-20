package test.ru.fasop.log;

import java.util.List;

import ru.fasop.TestSubject;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.PostQuestionnaireSession;
import ru.fasop.log.Session;
import test.ru.fasop.GertContentTestCase;

public class PostQuestionnaireSessionTest extends GertContentTestCase {
    
    public void testGetQuestionsAnswered() {
    	PostQuestionnaireSession sesh = new PostQuestionnaireSession(getLogSession());
    	
		assertEquals(2, sesh.getQuestionsAnswered().size());
    }
    
    public void testGetQuestionsAnsweredByType() {
    	PostQuestionnaireSession sesh = new PostQuestionnaireSession(getLogSession());
    	
		assertEquals(2, sesh.getQuestionsAnswered("likert").size());
    }
    
    public void testGetLatestAnswerForQuestion() {
    	PostQuestionnaireSession sesh = new PostQuestionnaireSession(getLogSession());
    	
    	assertEquals("-1", sesh.getLatestAnswerForQuestion("99999999"));
		assertEquals("1", sesh.getLatestAnswerForQuestion("111"));
		
		assertEquals("5", sesh.getLatestAnswerForQuestion("222"));
    }
    
    private Session getLogSession() {
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
    	Session session = new Session(subj, new Long(123));
    	
    	// login
    	List<LogEventParam> params = LogEventParam.parse("question_set_type : QNNAIRE; " +
    														"question_set_desc : Pre-questionnaire; " +
    														"question_set_id : 123; " +
    														"user : joe@hotmail.com");
    	    	
    	session.events.add(new LogEvent(new Long(1),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										params));
    	
    	// view inst page
    	params = LogEventParam.parse("pageId : questionnaire_inst_page");
    	session.events.add(new LogEvent(new Long(2),
											LogEvent.ACTOR_USER,
											LogEvent.ACT_USER_VIEW_PAGE,
											params));
    	
    	// view example test show_item page
    	params = LogEventParam.parse("pageId : questionnaire_show_category_page;"
									+ "questionSetId : 454;"
									+ "category : personal");
    	
    	session.events.add(new LogEvent(new Long(3),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_VIEW_PAGE,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454;"
									+ "questionId : 111;"
    								+ "category : personal;"
    								+ "question_type : open_short;"
    								+ "content : Naam;"
    								+ "value : 1");
    	session.events.add(new LogEvent(new Long(4),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454;"
    								+ "questionId : 222;"
    								+ "category : personal;"
    								+ "question_type : open_short;"
    								+ "content : E-mail;"
    								+ "value : 2");
    	session.events.add(new LogEvent(new Long(5),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// answer a question - out of order recording
    	params = LogEventParam.parse("questionSetId : 454;"
    								+ "questionId : 222;"
    								+ "category : personal;"
    								+ "question_type : open_short;"
    								+ "content : E-mail;"
    								+ "value : 5");
    	session.events.add(new LogEvent(new Long(7),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454;"
    								+ "questionId : 222;"
    								+ "category : personal;"
    								+ "question_type : open_short;"
    								+ "content : E-mail;"
    								+ "value : 3");
    	session.events.add(new LogEvent(new Long(6),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// debrief page
    	params = LogEventParam.parse("pageId : questionnaire_debrief_page");
    	session.events.add(new LogEvent(new Long(12),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_VIEW_PAGE,
							params));
    	
    	return session;
	}
}
