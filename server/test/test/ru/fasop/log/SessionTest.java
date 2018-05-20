package test.ru.fasop.log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import ru.fasop.TestSubject;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.Session;
import test.ru.fasop.GertContentTestCase;

public class SessionTest extends GertContentTestCase {

    public void testConstructors() {
    	String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
    	Session session = new Session(subj, new Long(123));
    	
    	session.events.add(new LogEvent(new Long(12345),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										new ArrayList<LogEventParam>()));
		
		assertEquals(new Long(123), session.timestamp);
		assertEquals(1, session.events.size());
		assertEquals(LogEvent.ACTOR_USER, session.events.get(0).actor);
    }
    
    public void testGetEarliestAndLatestUserEvents(){
		Session session = new Session();
		
		assertEquals(0, session.getDurationInMillis());
		
    	session.events.add(new LogEvent(new Long(1),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYS_TIME_EXPIRED,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(2),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGIN,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(3),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(4),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(5),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGOUT,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(6),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_REQUEST_RECOG,
				new ArrayList<LogEventParam>()));
    	
    	
    	assertEquals(new Long(2), session.getEarliestUserEvent().timestamp);
    	assertEquals(new Long(5), session.getLatestUserEvent().timestamp);
    }
        
    public void testIsActiveSession(){
    	Session session = new Session();
		
    	session.events.add(new LogEvent(new Long(1),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYS_TIME_EXPIRED,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(2),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGIN,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(3),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(4),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(Calendar.getInstance().getTimeInMillis(),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGOUT,
				new ArrayList<LogEventParam>()));
    	
    	assertEquals(true, session.isActive(60 * 5));
    	
    	session.events.add(new LogEvent(Calendar.getInstance().getTimeInMillis() - (1000 * 60 * 5),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGOUT,
				new ArrayList<LogEventParam>()));
    	
    	assertEquals(true, session.isActive(60 * 5));
    	
    	session.events.add(new LogEvent(Calendar.getInstance().getTimeInMillis() - (1000 * 60 * 6),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGOUT,
				new ArrayList<LogEventParam>()));
    	
    	assertEquals(false, session.isActive(60 * 5));
    }
    
    public void testGetDuration(){
		Session session = new Session();
		
		assertEquals(0, session.getDurationInMillis());
		
    	session.events.add(new LogEvent(new Long(1),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYS_TIME_EXPIRED,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(2),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGIN,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(3),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(4),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(5),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGOUT,
				new ArrayList<LogEventParam>()));
    	
    	session.events.add(new LogEvent(new Long(6),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_REQUEST_RECOG,
				new ArrayList<LogEventParam>()));

    	assertEquals(5 - 3, session.getDurationInMillis());
    }
    
	public void testLastPageViewed(){
		Session session = getExp1StyleLogSession();
		
		assertEquals("greet_training_spoken_video_page", session.getLastNonTimeExpiredPageViewed());
		
		List<LogEventParam> params = LogEventParam.parse("pageId : debrief_page ,"
														+ "questionId : 952," 
														+ "questionSetId : 952");
		session.events.add(new LogEvent(new Long(2),
					LogEvent.ACTOR_USER,
					LogEvent.ACT_USER_VIEW_PAGE,
					params));
				
		assertEquals("debrief_page", session.getLastNonTimeExpiredPageViewed());
	}
	
	private Session getExp1StyleLogSession() {
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		con.create(subj);
		
    	Session session = new Session(subj, new Long(123));
    	
    	// login
    	List<LogEventParam> params = LogEventParam.parse("question_set_type : TREAT," +
    														"question_set_desc : Melvin registers his new address. arrives at new place. Mo greets him," +
    														"question_set_id : 951," +
    														"user : joe@hotmail.com");
    	session.events.add(new LogEvent(new Long(1),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										params));
    	    	
    	
    	// view page 1 
    	params = LogEventParam.parse("pageId : inst_page ,"
									+ "questionId : 952," 
									+ "questionSetId : 952");
    	session.events.add(new LogEvent(new Long(2),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_USER_VIEW_PAGE,
								params));
    	
    	// view page 1 
    	params = LogEventParam.parse("pageId : greet_training_spoken_question_page ,"
									+ "questionId : 952," 
									+ "questionSetId : 952");
    	session.events.add(new LogEvent(new Long(2),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_USER_VIEW_PAGE,
								params));
    	
    	// saved answer
    	params = LogEventParam.parse("pageId : greet_training_spoken_question_page,"
									+ "questionId : 952," 
									+ "questionSetId : 952");
    	session.events.add(new LogEvent(new Long(2),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_SYS_SAVE_AUDIO,
								params));
    	
    	// view page 2
    	params = LogEventParam.parse("pageId : greet_training_spoken_question_page ,"
									+ "questionId : 953," 
									+ "questionSetId : 952");
    	session.events.add(new LogEvent(new Long(2),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_USER_VIEW_PAGE,
								params));
    	
    	// saved answer
    	params = LogEventParam.parse("pageId : greet_training_spoken_question_page,"
									+ "questionId : 953," 
									+ "questionSetId : 952");
    	session.events.add(new LogEvent(new Long(2),
								LogEvent.ACTOR_SYSTEM,
								LogEvent.ACT_SYS_SAVE_AUDIO,
								params));
    	
    	// view page 3
    	params = LogEventParam.parse("pageId : greet_training_spoken_video_page,"
									+ "questionId : 954," 
									+ "questionSetId : 952");
    	session.events.add(new LogEvent(new Long(2),
								LogEvent.ACTOR_USER,
								LogEvent.ACT_USER_VIEW_PAGE,
								params));
    	
    	

//    		[2263724, system, save_audio, 6:50:20 AM]
//
//    		    testfasop@gmail.com = questionSetId
//    		    951 = question_id
//    		    952 = questionContent
//    		    Wat gaat Melvin doen? = reqBlocks
//    		    [gaat| verhuizen| Melvin] = filename 
    	
    	con.create(session);
    	
    	return session;
	}
	
    public void testLoadAndSave(){
    	String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		con.create(subj);
		
    	Session session = new Session(subj, new Long(123));
    	
    	session.events.add(new LogEvent(new Long(12345),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										new ArrayList<LogEventParam>()));
		
		session.save(super.mgr);
		
		Query query = mgr.createQuery("select e from Session e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		Session sessionFromDB = (Session) query.getResultList().get(0);
		
		assertEquals(new Long(123), sessionFromDB.timestamp);
		assertEquals(1, sessionFromDB.events.size());
		assertEquals(email, session.subj.email);
	}
}
