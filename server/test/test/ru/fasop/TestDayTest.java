package test.ru.fasop;

import javax.persistence.Query;

import ru.fasop.TestDay;
import ru.fasop.TimeSlot;

public class TestDayTest extends GertContentTestCase {
	
	public void testTestDay(){
		int maxSpots = 3;
		String testDay = "20110201";
		
		TestDay td = new TestDay(testDay);
		
		td.addTimeslot(new TimeSlot("09:00", "10:00", maxSpots));
		td.addTimeslot(new TimeSlot("10:00", "11:00", maxSpots));
		
		assertEquals(testDay, td.date);
		assertEquals(2, td.timeslots.size());
	}
	
	public void testLoadAndSave(){
		int maxSpots = 3;
		String testDay = "20110201";
		
		TestDay td = new TestDay(testDay);
		
		td.addTimeslot(new TimeSlot("09:00", "10:00", maxSpots));
		td.addTimeslot(new TimeSlot("10:00", "11:00", maxSpots));
						
		td.save(super.mgr);
		
		Query query = mgr.createQuery("select e from TestDay e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		TestDay tdFromDB = (TestDay) query.getResultList().get(0);
		
		assertEquals(td.date, tdFromDB.date);
	}
	
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
