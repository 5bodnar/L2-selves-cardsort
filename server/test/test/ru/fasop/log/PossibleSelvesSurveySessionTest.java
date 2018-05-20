package test.ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import ru.fasop.TestSubject;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.PossibleSelvesSurveySession;
import ru.fasop.log.Session;
import ru.fasop.motivation.L2PossibleSelf;
import test.ru.fasop.GertContentTestCase;

public class PossibleSelvesSurveySessionTest extends GertContentTestCase {

    public void testConstructors() {
    	String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
		PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(subj, new Long(123));
    	
    	pselvesSesh.events.add(new LogEvent(new Long(12345),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										new ArrayList<LogEventParam>()));
		
		assertEquals(new Long(123), pselvesSesh.timestamp);
		assertEquals(1, pselvesSesh.events.size());
		assertEquals(LogEvent.ACTOR_USER, pselvesSesh.events.get(0).actor);
    }
        
//    public void testBehaviourMeasures() {
//    	Session session = (Session) conProd.querySingle("select e from Session e where e.id = 3856253");
//		
//		assertTrue(null != session);
//		
//		PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(session);
//    	
//		// assertEquals(48, pselvesSesh.countCardsBrowsed());
//		
//		assertEquals(4, pselvesSesh.countForwardsBrowsingInstances());
//    	assertEquals(3, pselvesSesh.countBackwardsBrowsingInstances());
//		
//    	assertEquals(11, pselvesSesh.countCardPicks());
//    	assertEquals(1, pselvesSesh.countCardUnpicks());
//    	
//    	List<L2PossibleSelf> originalPossibleSelves =
//    		(List) conProd.queryList("SELECT e FROM L2PossibleSelf e where (e.isOriginal = true and e.isCustom = false)");
//    	
//    	assertEquals(1, pselvesSesh.countCardMispicks(originalPossibleSelves));
//    }
//    
//    public void testGetHitsCounts() {
//    	Session session = (Session) conProd.querySingle("select e from Session e where e.id = 3856412");
//		
//		assertTrue(null != session);
//		
//		PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(session);
//    	
//		System.out.println(pselvesSesh.getDesirednessDataPoints(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS));
//		
//		int thresholdAsPercentage = 50;
//		assertEquals(2, pselvesSesh.countDesiredHits(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS, thresholdAsPercentage));
//		
//		System.out.println(pselvesSesh.getDesirednessDataPoints(PossibleSelvesSurveySession.PAGE_ID_DES_UNDESIREDNESS));
//		
//		assertEquals(730, pselvesSesh.getSortingBoxHeight(PossibleSelvesSurveySession.PAGE_ID_DES_UNDESIREDNESS).intValue());
//		assertEquals(2, pselvesSesh.countDesiredHits(PossibleSelvesSurveySession.PAGE_ID_DES_UNDESIREDNESS, thresholdAsPercentage));
//    }
//    
//    public void testGetRangeCharacteristics() {
//    	Session session = (Session) conProd.querySingle("select e from Session e where e.id = 3856412");
//		
//		assertTrue(null != session);
//		
//		PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(session);
//    	    	    	
//		assertEquals(730.0, pselvesSesh.getSortingBoxHeight(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS));
//    	
//		assertEquals(156, pselvesSesh.getLowerSortLimit(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS).intValue());
//		
//		assertEquals(573, pselvesSesh.getUpperSortLimit(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS).intValue());
//
//		assertEquals(404, pselvesSesh.getRangeCentre(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS).intValue());
//		
//		assertEquals(140, pselvesSesh.getDesirednessSD(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS).intValue());
//    }
//    
//    public void testGetTimeOnPage() {
//    	Session session = (Session) conProd.querySingle("select e from Session e where e.id = 3856253");
//		
//		assertTrue(null != session);
//		
//		PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(session);
//    	    	
//    	assertEquals(78913433 , pselvesSesh.getTimeOnPage("pselves_select_pselves"));
//    }
//    
//    public void testReshuffles() {
//    	Session session = (Session) conProd.querySingle("select e from Session e where e.id = 3856412");
//		
//		assertTrue(null != session);
//		
//		PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(session);
//    	
//		assertEquals(4, pselvesSesh.countCardReshuffles());
//    	assertEquals(3, pselvesSesh.countDesiredCardReshuffles());
//    	assertEquals(1, pselvesSesh.countUndesiredCardReshuffles());
//    }
    
    public void testGetsErrorDialogMustChooseThreeUnwantedSelves(){
    	Session session = (Session) conProd.querySingle("select e from Session e where e.id = 5777082");
		
		assertTrue(null != session);
		
		PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(session);
		
		assertEquals(1, pselvesSesh.countDialogViewsForMustChooseThreeUnwantedSelves());
    }
}
