package test.ru.fasop.log;

import java.util.List;

import ru.fasop.TestSubject;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.QuestionnaireResult;
import ru.fasop.log.Session;
import test.ru.fasop.GertContentTestCase;

public class QuestionnaireResultTest extends GertContentTestCase {

	public void testTGJResult(){
		QuestionnaireResult res = new QuestionnaireResult(QuestionnaireResult.TEST_TYPE_PRE,
															"some description",
															new Long(12345));
		
		assertEquals(QuestionnaireResult.TEST_TYPE_PRE, res.type);
		assertEquals("some description", res.description);
		assertEquals(new Long(12345), res.questionSetId);	
	}
	
	public void testGetAnswerByQuestionType(){
		QuestionnaireResult res = new QuestionnaireResult(QuestionnaireResult.TEST_TYPE_PRE,
															"some description",
															new Long(12345));
		
		res.events = getLogSessionEvents();
		
		
		List<LogEvent> events = res.getQuestionnaireAnswerEventsByQuestionType("open_short");
		
		assertEquals(3, events.size());
		
		events = res.getQuestionnaireAnswerEventsByQuestionType("likert");
		
		assertEquals(1, events.size());
	}
	
	private List<LogEvent> getLogSessionEvents() {
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
    	Session session = new Session(subj, new Long(123));
    	
    	// login
    	List<LogEventParam> params = LogEventParam.parse("question_set_type : QNNAIRE, " +
    														"question_set_desc : Pre-questionnaire, " +
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
    	
    	
    	// answer a question
    	params = LogEventParam.parse("questionSetId : 454,"
    								+ "category : personal,"
    								+ "question_type : likert,"
    								+ "content : this sentence is long,"
    								+ "value : sagree");
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
    	
    	return session.events;
	}
}
