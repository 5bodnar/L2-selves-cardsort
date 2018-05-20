package test.ru.fasop;

import java.util.List;

import javax.persistence.Query;

import ru.fasop.FASOPUserTaskList;
import ru.fasop.QuestionnaireItem;
import ru.fasop.TestSubject;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.Session;

public class TestSubjectTest extends GertContentTestCase {
	
	public void testSomething(){
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		Boolean isAdmin = false;
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
				
		assertEquals(name, subj.name);
		assertEquals(email, subj.email);
		assertEquals(phoneNum, subj.phoneNum);
		assertEquals(password, subj.password);
		assertEquals(isAdmin, subj.isAdmin);
		assertEquals(new Long(0), subj.currentTaskIndex);
	}
	
	public void testHasTaskList(){
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		
		subj.tasklist = new FASOPUserTaskList("Session 1 - CNTRL", FASOPUserTaskList.TYPE_SCHEDULED);
		
		assertEquals("Session 1 - CNTRL", subj.tasklist.label);
	}
	
	public void testLoadAndSave(){
		String name = "Stephen Bodnar";
		String email = "s.bodnar@let.ru.nl";
		String phoneNum = "061234567";
		String password = "test";
		
		TestSubject subj = new TestSubject(name, email, phoneNum, password);
		con.create(subj);
		
		Query query = mgr.createQuery("select e from TestSubject e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		TestSubject subjFromDB = (TestSubject) query.getResultList().get(0);
		
		assertEquals(subj.email, subjFromDB.email);
	}
	
//	public void testGetTopNPossibleSelves(){
///*		Could inform a CALL system on how to adapt to the learner
//*/		
//		String name = "Stephen Bodnar";
//		String email = "s.bodnar@let.ru.nl";
//		String phoneNum = "061234567";
//		String password = "test";
//		
//		TestSubject subj = new TestSubject(name, email, phoneNum, password);
//		
//		//Hope count : How many hopes do they have?
//		//Fear count : How many fears do they have?
//		
//		assertEquals(16, subj.getMostRecentHopedForL2SelfConcept().count());
//		assertEquals(7, subj.getMostRecentFearedL2SelfConcept().count());
//		
//		//Are they motivated? : What is their mean hope motivation level?
//		//What are their self-efficacy levels? : their mean hope self-efficacy level?
//		//What are their expectancy levels? : their mean hope expectancy levels?
//		//* repeat for fears
//		
//		subj.getMostRecentHopedForL2SelfConcept().getMeanDesiredness();
//		subj.getMostRecentHopedForL2SelfConcept().getMeanEfficacy();
//		subj.getMostRecentHopedForL2SelfConcept().getMeanLikelihood();
//		
//		subj.getMostRecentFearedL2SelfConcept().getMeanDesiredness();
//		subj.getMostRecentFearedL2SelfConcept().getMeanEfficacy();
//		subj.getMostRecentFearedL2SelfConcept().getMeanLikelihood();
//		
//		// SD for motivation, self-efficacy, expectancy (to see if they are spread, or uniform).
//		subj.getMostRecentHopedForL2SelfConcept().getDesirednessSD();
//		subj.getMostRecentHopedForL2SelfConcept().getEfficacySD();
//		subj.getMostRecentHopedForL2SelfConcept().getLikelihoodSD();
//		
//		subj.getMostRecentHopedForL2SelfConcept().getDesirednessHighestFrequencyTertile();
//		subj.getMostRecentHopedForL2SelfConcept().getEfficacyHighestFrequencyTertile();
//		subj.getMostRecentHopedForL2SelfConcept().getLikelihoodHighestFrequencyTertile();
//			
////		Where do their motivations lie? : How are they distributed? Build Top N list (with desiredness)
////		Where do their self-efficacy lie? How are they distributed? Build Top N list (with self-efficacy)
////		Where do their expectancy lie? How are they distributed? Build Top N list (with likelihood)
////		* Can repeat the same, but create top N lists of constructs
//
//		List<L2PossibleSelf> pselves = subj.getTopNPossibleSelves(10, L2SelfConceptInstance.DIM_DESIREDNESS);
//		
//		assertEquals(10, pselves.size());
//	}

}
