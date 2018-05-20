package test.ru.fasop.log;

import java.util.List;

import ru.fasop.TestSubject;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.QuestionnaireResult;
import ru.fasop.log.QuestionnaireSessionProcessor;
import ru.fasop.log.Session;
import test.ru.fasop.GertContentTestCase;

public class QuestionnaireSessionProcessorTest extends GertContentTestCase {
	
	public void testGetResultType() {
		List<LogEventParam> params = LogEventParam.parse("question_set_type : QNNAIRE, " +
				"question_set_desc : Pre-questionnaire, " +
				"question_set_id : 123, " +
				"user : joe@hotmail.com");

		LogEvent e = new LogEvent(new Long(1),
									LogEvent.ACTOR_USER,
									LogEvent.ACT_USER_LOGIN,
									params);
		    	
    	assertEquals("pre", QuestionnaireSessionProcessor.getResType(e));
    	
    	params = LogEventParam.parse("question_set_type : QNNAIRE, " +
				"question_set_desc : Post-questionnaire, " +
				"question_set_id : 123, " +
				"user : joe@hotmail.com");
    	
    	e = new LogEvent(new Long(1),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGIN,
				params);

    	assertEquals("post", QuestionnaireSessionProcessor.getResType(e));
    	
    	params = LogEventParam.parse("question_set_type : QNNAIRE, " +
				"question_set_desc : Motivational Indicators - Periodic - Set 1, " +
				"question_set_id : 123, " +
				"user : joe@hotmail.com");
    	
    	e = new LogEvent(new Long(1),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGIN,
				params);

    	assertEquals("mini_motivation", QuestionnaireSessionProcessor.getResType(e));
    }
	
	public void testStepThroughEvents() {
		QuestionnaireSessionProcessor proc = new QuestionnaireSessionProcessor(getLogSession());
		    	
    	assertEquals(null, proc.prev2Event);
    	assertEquals(null, proc.prevEvent);
    	assertEquals(null, proc.curEvent);
    	
    	proc.nextEvent();
    	
    	assertEquals(null, proc.prev2Event);
    	assertEquals(null, proc.prevEvent);
    	assertEquals(LogEvent.ACT_USER_LOGIN, proc.curEvent.act);
    	
    	proc.nextEvent();
    	
    	assertEquals(null, proc.prev2Event);
    	assertEquals(LogEvent.ACT_USER_LOGIN, proc.prevEvent.act);
    	assertEquals(LogEvent.ACT_USER_VIEW_PAGE, proc.curEvent.act);
    }
			
	public void testDidReachDebriefPage() {
		Session sesh = getLogSession();
		QuestionnaireSessionProcessor proc = new QuestionnaireSessionProcessor(sesh);
    	
    	assertEquals(false, proc.didReachDebriefPage);
    	    	
    	while(proc.eventIndex < sesh.events.size()) {
    		proc.nextEvent();
    	}
    	
    	assertEquals(new Long(12), proc.curEvent.timestamp);
    	assertEquals(true, proc.didReachDebriefPage);
    }
	
    public void testProcess() {
    	QuestionnaireSessionProcessor proc = new QuestionnaireSessionProcessor(getLogSession());
    	
    	QuestionnaireResult res = proc.process();
    	    	
    	assertEquals(3, res.events.size());
    }
    
    public void testProcessIncludeLastAnswerToQuestionOnly() {
    	QuestionnaireSessionProcessor proc = new QuestionnaireSessionProcessor(getLogSession());
    	
    	boolean includeLastAnswerOnly = true;
    	
    	QuestionnaireResult res = proc.process(includeLastAnswerOnly);
    	    	
    	assertEquals(2, res.events.size());
    }
    
    public void testProcessNoDebriefPage() {
    	QuestionnaireSessionProcessor proc = new QuestionnaireSessionProcessor(getLogSessionNoDebriefPage());
    	
    	QuestionnaireResult res = proc.process();
    	    	
    	assertEquals(3, res.events.size());
    }
    
	private Session getLogSession() {
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
    	Session session = new Session(subj, new Long(123));
    	
    	// login
    	List<LogEventParam> params = LogEventParam.parse("question_set_type : QNNAIRE, " +
    														"question_set_desc : Pre-questionnaire, " +
    														"question_set_id : 123, " +
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
    	params = LogEventParam.parse("pageId : questionnaire_show_category_page,"
									+ "questionSetId : 454,"
									+ "category : personal");
    	
    	session.events.add(new LogEvent(new Long(3),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_VIEW_PAGE,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454,"
									+ "questionId : 111,"
    								+ "category : personal,"
    								+ "question_type : open_short,"
    								+ "content : Naam,"
    								+ "value : Semra");
    	session.events.add(new LogEvent(new Long(4),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454,"
    								+ "questionId : 222,"
    								+ "category : personal,"
    								+ "question_type : open_short,"
    								+ "content : E-mail,"
    								+ "value : joe@test.com");
    	session.events.add(new LogEvent(new Long(5),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454,"
    								+ "questionId : 222,"
    								+ "category : personal,"
    								+ "question_type : open_short,"
    								+ "content : E-mail,"
    								+ "value : joe@test.com");
    	session.events.add(new LogEvent(new Long(5),
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
	
	private Session getLogSessionNoDebriefPage() {
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
    	Session session = new Session(subj, new Long(123));
    	
    	// login
    	List<LogEventParam> params = LogEventParam.parse("question_set_type : QNNAIRE, " +
    														"question_set_desc : Pre-questionnaire, " +
    														"question_set_id : 123, " +
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
    	params = LogEventParam.parse("pageId : questionnaire_show_category_page,"
									+ "questionSetId : 454,"
									+ "category : personal");
    	
    	session.events.add(new LogEvent(new Long(3),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_VIEW_PAGE,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454,"
    								+ "category : personal,"
    								+ "question_type : open_short,"
    								+ "content : Naam,"
    								+ "value : Semra");
    	session.events.add(new LogEvent(new Long(4),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454,"
    								+ "category : personal,"
    								+ "question_type : open_short,"
    								+ "content : E-mail,"
    								+ "value : joe@test.com");
    	session.events.add(new LogEvent(new Long(5),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454,"
    								+ "category : personal,"
    								+ "question_type : open_short,"
    								+ "content : E-mail,"
    								+ "value : joe@test.com");
    	session.events.add(new LogEvent(new Long(5),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_ANSWER_QNNAIRE_ITEM,
							params));
    	
    	return session;
	}
}
