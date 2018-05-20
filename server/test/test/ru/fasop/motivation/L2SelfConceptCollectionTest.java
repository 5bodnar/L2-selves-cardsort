package test.ru.fasop.motivation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.fasop.TestSubject;
import ru.fasop.log.PossibleSelvesSurveySession;
import ru.fasop.log.Session;
import ru.fasop.motivation.L2PossibleSelf;
import ru.fasop.motivation.L2SelfConceptCollection;
import test.ru.fasop.GertContentTestCase;

public class L2SelfConceptCollectionTest extends GertContentTestCase {
	
	public void testCalculateZScore(){
		List<L2PossibleSelf> originalSelves = 
			(List) conProd.queryList("SELECT e FROM L2PossibleSelf e where (e.isOriginal = true and e.isCustom = false)");
		
		List<TestSubject> subjects = new ArrayList<TestSubject>();
		
		List<TestSubject> subs = (List<TestSubject>) conProd.queryList("select e from TestSubject e");
		
		Long[] subjectIds = new Long[] {new Long(2283202), new Long(2283203), new Long(2283204), new Long(2283205), new Long(2283275), new Long(2283363), new Long(2283499), new Long(2285101)};
		List<Long> ids = Arrays.asList(subjectIds);
		
		for(TestSubject sub : subs){
			if(ids.contains(sub.id)){
				subjects.add(sub);
			}
		}
		
		L2SelfConceptCollection instCol = new L2SelfConceptCollection();
		
		for(TestSubject sub : subjects){
			List<Session> sessions = Session.getPossibleSelvesSessions(conProd, sub);
			
			for(Session session : sessions){
				PossibleSelvesSurveySession pselvesSesh = new PossibleSelvesSurveySession(session);
				
				if(0 < pselvesSesh.getTimeOnPage(PossibleSelvesSurveySession.PAGE_ID_SELECT_PSELVES)
						&& 0 < pselvesSesh.getTimeOnPage(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS)){
				
					instCol.addPossibleSelvesSurveySession(pselvesSesh);
				}
			}
		}
		
		Session mostRecentSesh = Session.getPossibleSelvesSessions(conProd, subjects.get(0)).get(0);
		PossibleSelvesSurveySession sesh = new PossibleSelvesSurveySession(mostRecentSesh);
		
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getPickScore(sesh, L2SelfConceptCollection.TYPE_ZSCORE)));
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getUnpickScore(sesh, L2SelfConceptCollection.TYPE_ZSCORE)));
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getMispickScore(sesh, originalSelves, L2SelfConceptCollection.TYPE_ZSCORE)));
		
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getTimeScore(sesh, PossibleSelvesSurveySession.PAGE_ID_SELECT_PSELVES, L2SelfConceptCollection.TYPE_ZSCORE)));
		
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getRangeScore(sesh, PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS, L2SelfConceptCollection.TYPE_ZSCORE)));
		
		//assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getRangeCentreScore(sesh, PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS, L2SelfConceptInstanceCollection.TYPE_ZSCORE)));
		
		//assertEquals(730.0, pselvesSesh.getSortingBoxHeight(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS));
    	
		//assertEquals(156, pselvesSesh.getLowerSortLimit(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS).intValue());
		
		//assertEquals(573, pselvesSesh.getUpperSortLimit(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS).intValue());
				
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getDesirednessDispersionScore(sesh, PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS, L2SelfConceptCollection.TYPE_ZSCORE)));
		
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getReshufflesScore(sesh, L2SelfConceptCollection.TYPE_ZSCORE)));
		
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getForwardBrowsesScore(sesh, L2SelfConceptCollection.TYPE_ZSCORE)));
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getBackwardsBrowsesScore(sesh, L2SelfConceptCollection.TYPE_ZSCORE)));
		
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getHitsScore(sesh, PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS, 75, L2SelfConceptCollection.TYPE_ZSCORE)));
	}
	
	public void testGetQuartile(){
		List<L2PossibleSelf> originalSelves = 
			(List) conProd.queryList("SELECT e FROM L2PossibleSelf e where (e.isOriginal = true and e.isCustom = false)");
		
		List<TestSubject> subjects = new ArrayList<TestSubject>();
		
		List<TestSubject> subs = (List<TestSubject>) conProd.queryList("select e from TestSubject e");
		
		Long[] subjectIds = new Long[] {new Long(2283202), new Long(2283203), new Long(2283204), new Long(2283205), new Long(2283275), new Long(2283363), new Long(2283499), new Long(2285101)};
		List<Long> ids = Arrays.asList(subjectIds);
		
		for(TestSubject sub : subs){
			if(ids.contains(sub.id)){
				subjects.add(sub);
			}
		}
		
		L2SelfConceptCollection instCol = new L2SelfConceptCollection();
		
		for(TestSubject sub : subjects){
			List<Session> sessions = Session.getPossibleSelvesSessions(conProd, sub);
			
			for(Session session : sessions){
				instCol.addPossibleSelvesSurveySession(new PossibleSelvesSurveySession(session));
			}
		}
		
		Session mostRecentSesh = Session.getPossibleSelvesSessions(conProd, subjects.get(0)).get(0);
		PossibleSelvesSurveySession sesh = new PossibleSelvesSurveySession(mostRecentSesh);
		
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getPickScore(sesh, L2SelfConceptCollection.TYPE_PERCENTILE)));
		assertTrue(isBetweenNegativeFiveAndPositiveFive(instCol.getUnpickScore(sesh, L2SelfConceptCollection.TYPE_PERCENTILE)));
	}
	
	boolean isBetweenNegativeFiveAndPositiveFive(double val){
		if(! ( val >= -5 && val <= 5)){
			System.out.println(val);
		}
		
		return val >= -5 && val <= 5;
	}
}
