package test.ru.fasop;

import javax.persistence.Query;

import ru.fasop.TestSubject;
import ru.fasop.TimeSlot;

public class TimeSlotTest extends GertContentTestCase {
	
	public void testTimeSlot(){
		int maxSpots = 3;
		TimeSlot ts = new TimeSlot("09:00", "10:00", maxSpots);
		
		TestSubject subj1 = new TestSubject("name1", "email1", "phone num", "password");
		TestSubject subj2 = new TestSubject("name2", "email2", "phone num", "password");
		TestSubject subj3 = new TestSubject("name3", "email3", "phone num", "password");
		
		ts.getSpot(0).subj = subj1;
		
		assertTrue(! ts.isFull());
		
		ts.getSpot(1).subj = subj2;
		ts.getSpot(2).subj = subj3;
		
		assertTrue(ts.isFull());
	}
	
	public void testLoadAndSave(){
		TestSubject subj = new TestSubject("name1", "email1", "phone num", "password");
		con.create(subj);
		
		int maxSpots = 2;
		TimeSlot ts = new TimeSlot("09:00", "10:00", maxSpots);
		
		ts.getSpot(0).subj = subj;
						
		con.create(ts);
		
		Query query = mgr.createQuery("select e from TimeSlot e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		TimeSlot tsFromDB = (TimeSlot) query.getResultList().get(0);
		
		assertEquals(ts.maxSpots, tsFromDB.maxSpots);
		assertTrue(ts.getSpot(0).isTaken());
		assertTrue(! ts.getSpot(1).isTaken());
		
		assertEquals("email1", ts.getSpot(0).subj.email);
	}
	
//	public void testTestDay(){
//		def td = new TestDay("20110201")
//		
//		td.addTimeslot(new TimeSlot("09:00", "10:00", maxSpots))
//		td.addTimeslot(new TimeSlot("10:00", "11:00", maxSpots))
//		
//		assertEquals(2, td.timeslots.size())
//	}
//	
//	public void testTestPeriod(){
//		def description = "some description"
//		def tp = new TestPeriod(description)
//		
//		tp.addTestDay(new TestDay("20110201"))
//		tp.addTestDay(new TestDay("20110202"))
//		tp.addTestDay(new TestDay("20110203"))
//		
//		assertEquals(3, td.testdays.size())
//	}
//	
//	public void testSomething(){
//		weeks = loadWeeks(db)
//		for each period in testPeriods
//			for each day in period.days
//				for each ts in day.timeslots:
//					for each spot in ts:
//						if(!spot.isFull)
//							print "Sign up"
//						else
//							print "Taken"
//		
//		
//		period = new Period("20110201", "20110210")
//		
//		expDay = new ExperimentDay("20110201")
//		
//		ts1 = new Timeslot("09:00", "10:00")
//		ts2 = new Timeslot("10:00", "11:00")
//		ts3 = new Timeslot("11:00", "12:00")
//		
//		expDay.addTimeslot(ts1)
//		expDay.addTimeslot(ts2)
//		expDay.addTimeslot(ts3)
//	}
}
