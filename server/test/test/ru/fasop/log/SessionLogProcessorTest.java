package test.ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import ru.fasop.TestSubject;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.PageViewEventCollection;
import ru.fasop.log.Session;
import ru.fasop.log.SessionLogProcessor;
import test.ru.fasop.GertContentTestCase;

public class SessionLogProcessorTest extends GertContentTestCase {
	
	public void testConstructor() {
		SessionLogProcessor proc = new SessionLogProcessor(getExampleSessionLog());
		
		assertEquals(0, proc.eventIndex);
    	
    	assertEquals(null, proc.curEvent);
    }
	
	public void testNextEvent() {
		SessionLogProcessor proc = new SessionLogProcessor(new Session());
		
    	assertEquals(null, proc.curEvent);
    	
    	proc.nextEvent();
    	
    	assertEquals(null, proc.curEvent);
    	
    	Session session = new Session(new TestSubject(), new Long(123));
    	
    	session.events.add(new LogEvent(new Long(1),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										null));
    	
    	proc = new SessionLogProcessor(session);
		
    	assertEquals(null, proc.curEvent);
    	
    	proc.nextEvent();
    	
    	assertEquals(1, proc.curEvent.timestamp.intValue());
    }
	
	public void testStepThroughEvents() {
		SessionLogProcessor proc = new SessionLogProcessor(getExampleSessionLog());
		
    	assertEquals(null, proc.curEvent);
    	
    	proc.nextEvent();
    	
    	assertEquals(LogEvent.ACT_USER_LOGIN, proc.curEvent.act);
    	
    	proc.nextEvent();
    	
    	assertEquals(LogEvent.ACT_USER_VIEW_PAGE, proc.curEvent.act);
    }
	
	public void testBuildPageViewEventCollection() {
		List<Long> targetUttList = new ArrayList<Long>();
		targetUttList.add(new Long(46));
		
		SessionLogProcessor proc = new SessionLogProcessor(getExampleSessionLog());
		
		proc.seekLogin();
		
		assertEquals(new Long(1), proc.curEvent.timestamp);
		
		proc.seekNextPageViewEvent();
		
		assertEquals(new Long(2), proc.curEvent.timestamp);
		
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(2), eventCollection.events.get(0).timestamp);
		assertEquals(1, eventCollection.events.size());
		assertEquals(new Long(6), proc.curEvent.timestamp);
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(6), eventCollection.events.get(0).timestamp);
		assertEquals(3, eventCollection.events.size());
		assertEquals(new Long(9), proc.curEvent.timestamp);
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(9), eventCollection.events.get(0).timestamp);
		assertEquals(3, eventCollection.events.size());
		assertEquals(new Long(12), proc.curEvent.timestamp);
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(12), eventCollection.events.get(0).timestamp);
		assertEquals(3, eventCollection.events.size());
		assertEquals(null, proc.curEvent);
		
		assertEquals(null, proc.buildPageViewEventCollection());
	}
	
	public void testGetDuration() {
		SessionLogProcessor proc = new SessionLogProcessor(getExampleSessionLog());
		
		proc.seekLogin();
		proc.seekNextPageViewEvent();
		
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(2), eventCollection.events.get(0).timestamp);
		assertEquals(new Long(6), proc.curEvent.timestamp);
		assertEquals(proc.curEvent.timestamp - eventCollection.getStartTime(), eventCollection.getDuration());
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(6), eventCollection.events.get(0).timestamp);
		assertEquals(new Long(9), proc.curEvent.timestamp);
		assertEquals(proc.curEvent.timestamp - eventCollection.getStartTime(), eventCollection.getDuration());
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(9), eventCollection.events.get(0).timestamp);
		assertEquals(new Long(12), proc.curEvent.timestamp);
		assertEquals(proc.curEvent.timestamp - eventCollection.getStartTime(), eventCollection.getDuration());
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(12), eventCollection.events.get(0).timestamp);
		assertEquals(3, eventCollection.events.size());
		assertEquals(null, proc.curEvent);
		
		assertEquals(-1, eventCollection.getDuration());
		assertEquals(eventCollection.getLastPageEvent().timestamp - eventCollection.getStartTime(), eventCollection.getDurationUntilLastPageEvent());
		
	}
	
	public void testGetDurationIfLastPageViewHasNoEvents() {
		SessionLogProcessor proc = new SessionLogProcessor(getDurationSpecialCaseExampleSessionLog());
		
		proc.seekLogin();
		proc.seekNextPageViewEvent();
		
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(2), eventCollection.getPageViewEvent().timestamp);
		assertEquals(new Long(3), proc.curEvent.timestamp);
		assertEquals(proc.curEvent.timestamp - eventCollection.getStartTime(), eventCollection.getDuration());
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(new Long(3), eventCollection.getPageViewEvent().timestamp);
		assertEquals(new Long(5), proc.curEvent.timestamp);
		assertEquals(proc.curEvent.timestamp - eventCollection.getStartTime(), eventCollection.getDuration());
		
		eventCollection = proc.buildPageViewEventCollection();
		
		assertEquals(null, proc.curEvent);
		assertEquals(new Long(5), eventCollection.getPageViewEvent().timestamp);
		assertEquals(-1, eventCollection.getDuration());
	}
	
	public void testNextPageViewForTroubleLog() {
		Session sesh = (Session) conProd.querySingle("select e from Session e where e.id = 2610203");
		
		SessionLogProcessor proc = new SessionLogProcessor(sesh);
		
		proc.seekLogin();
				
		proc.seekNextPageViewEvent();
		
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		while(null != eventCollection){
			assertTrue(null != eventCollection.getPageType());
			
			eventCollection = proc.buildPageViewEventCollection();
		}
		
		assertEquals(null, proc.curEvent);
	}
	
	public void testNextPageViewForTroubleLog2() {
		Session sesh = (Session) conProd.querySingle("select e from Session e where e.id = 3855953");
		
		SessionLogProcessor proc = new SessionLogProcessor(sesh);
		
		proc.seekLogin();
				
		proc.seekNextPageViewEvent();
		
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		while(null != eventCollection){
			assertTrue(null != eventCollection.getPageType());
			
			eventCollection = proc.buildPageViewEventCollection();
		}
		
		assertEquals(null, proc.curEvent);
	}
	
    private Session getExampleSessionLog() {
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
    	Session session = new Session(subj, new Long(123));
    	
    	// login
    	List<LogEventParam> params = LogEventParam.parse("question_set_type : GJTEST;" +
    														"question_set_desc : gjt2pst;" +
    														"question_set_id : 123;" +
    														"user : joe@hotmail.com");
    	session.events.add(new LogEvent(new Long(1),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										params));
    	
    	// view inst page
    	params = LogEventParam.parse("pageId : gt_inst_page");
    	session.events.add(new LogEvent(new Long(2),
											LogEvent.ACTOR_USER,
											LogEvent.ACT_USER_VIEW_PAGE,
											params));
    	
    	// view real test question 1
    	params = LogEventParam.parse("pageId : gt_show_item_page;"
									+ "isValidUtt : false;"
									+ "question_set_id : 453;"
									+ "questionId : 46;"
									+ "questionContent : De naam vader Oscar mijn");
    	session.events.add(new LogEvent(new Long(6),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_USER_VIEW_PAGE,
								params));

    	// time expired
       	params = LogEventParam.parse("pageId : gt_show_item_page");
    	session.events.add(new LogEvent(new Long(7),
							LogEvent.ACTOR_SYSTEM,
							LogEvent.ACT_SYS_TIME_EXPIRED,
							new ArrayList<LogEventParam>()));
    	
    	// rate correctness for 1st real question
       	params = LogEventParam.parse("pageId : gt_show_item_page;"
       								+ "correctness_val : 394");
       	session.events.add(new LogEvent(new Long(8),
		   							LogEvent.ACTOR_USER,
		   							LogEvent.ACT_USER_RATE_CORRECTNESS,
		   							params));
       	
       	// view second question
    	params = LogEventParam.parse("pageId : gt_show_item_page;"
									+ "isValidUtt : false;"
									+ "question_set_id : 453;"
									+ "questionId : 46;"
									+ "questionContent : De naam vader Oscar mijn");
    	session.events.add(new LogEvent(new Long(9),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_USER_VIEW_PAGE,
								params));

    	// rate correctness second question
       	params = LogEventParam.parse("pageId : gt_show_item_page;"
       								+ "correctness_val : 394");
       	session.events.add(new LogEvent(new Long(10),
		   							LogEvent.ACTOR_USER,
		   							LogEvent.ACT_USER_RATE_CORRECTNESS,
		   							params));
    	
    	// time expired
       	params = LogEventParam.parse("pageId : gt_show_item_page");
    	session.events.add(new LogEvent(new Long(11),
							LogEvent.ACTOR_SYSTEM,
							LogEvent.ACT_SYS_TIME_EXPIRED,
							new ArrayList<LogEventParam>()));

    	// debrief page
    	params = LogEventParam.parse("pageId : gt_debrief_page");
    	session.events.add(new LogEvent(new Long(12),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_VIEW_PAGE,
							new ArrayList<LogEventParam>()));
    	
    	// user clicked event to show that last event should get assigned to last page view in
    	// the case where it's not a page view event
       	params = LogEventParam.parse("pageId : gt_debrief_page");
    	session.events.add(new LogEvent(new Long(13),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_PRESS_BUTTON,
							new ArrayList<LogEventParam>()));
    	
    	// user clicked event to show that last event should get assigned to last page view in
    	// the case where it's not a page view event
       	params = LogEventParam.parse("pageId : gt_debrief_page");
    	session.events.add(new LogEvent(new Long(14),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_PRESS_BUTTON,
							new ArrayList<LogEventParam>()));
    	
    	return session;
	}
    
    private Session getDurationSpecialCaseExampleSessionLog() {
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
    	Session session = new Session(subj, new Long(123));
    	
    	// login
    	List<LogEventParam> params = LogEventParam.parse("question_set_type : GJTEST;" +
    														"question_set_desc : gjt2pst;" +
    														"question_set_id : 123;" +
    														"user : joe@hotmail.com");
    	session.events.add(new LogEvent(new Long(1),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										params));
    	
    	// view inst page
    	params = LogEventParam.parse("pageId : gt_inst_page");
    	session.events.add(new LogEvent(new Long(2),
											LogEvent.ACTOR_USER,
											LogEvent.ACT_USER_VIEW_PAGE,
											params));
    	
    	// view real test question 1
    	params = LogEventParam.parse("pageId : gt_show_item_page;"
									+ "isValidUtt : false;"
									+ "question_set_id : 453;"
									+ "questionId : 46;"
									+ "questionContent : De naam vader Oscar mijn");
    	session.events.add(new LogEvent(new Long(3),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_USER_VIEW_PAGE,
								params));

    	// time expired
       	params = LogEventParam.parse("pageId : gt_show_item_page");
    	session.events.add(new LogEvent(new Long(4),
							LogEvent.ACTOR_SYSTEM,
							LogEvent.ACT_SYS_TIME_EXPIRED,
							new ArrayList<LogEventParam>()));

    	// debrief page
    	params = LogEventParam.parse("pageId : gt_debrief_page");
    	session.events.add(new LogEvent(new Long(5),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_VIEW_PAGE,
							new ArrayList<LogEventParam>()));
    	
    	return session;
	}
}
