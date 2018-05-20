package test.ru.fasop;

import javax.persistence.Query;

import ru.fasop.TestSpot;
import ru.fasop.TestSubject;

public class TestSpotTest extends GertContentTestCase {
	
	public void testTestSpot(){
		TestSubject subj = new TestSubject("name", "email", "phone num", "password");
		
		TestSpot testSpot = new TestSpot();
		
		assertTrue(! testSpot.isTaken());
		
		testSpot.subj = subj;
		
		assertTrue(testSpot.isTaken());
		
		assertTrue(! testSpot.isCompleted);
	}
	
	public void testLoadAndSave(){
		TestSubject subj = new TestSubject("name", "email", "phone num", "password");
		con.create(subj);
		
		TestSpot testSpot = new TestSpot();
		testSpot.subj = subj;
		
		con.create(testSpot);
		
		Query query = mgr.createQuery("select e from TestSpot e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		TestSpot spotFromDB = (TestSpot) query.getResultList().get(0);
		
		assertEquals(subj.email, spotFromDB.subj.email);
	}

}
