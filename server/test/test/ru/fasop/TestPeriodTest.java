package test.ru.fasop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import ru.fasop.TestDay;
import ru.fasop.TestPeriod;
import ru.fasop.TimeSlot;

public class TestPeriodTest extends GertContentTestCase {
	
	public void testTestPeriod(){
		String description = "some description";
		TestPeriod tp = new TestPeriod(description);
		
		tp.addTestDay(new TestDay("20110201"));
		tp.addTestDay(new TestDay("20110202"));
		tp.addTestDay(new TestDay("20110203"));
		
		assertEquals(description, tp.description);
		assertEquals(3, tp.testdays.size());
	}
	
	public void testLoadAndSave(){
		String description = "some description";
		TestPeriod tp = new TestPeriod(description);
		
		tp.addTestDay(new TestDay("20110201"));
		tp.addTestDay(new TestDay("20110202"));
		tp.addTestDay(new TestDay("20110203"));
						
		tp.save(super.mgr);
		
		Query query = mgr.createQuery("select e from TestPeriod e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		TestPeriod tpFromDB = (TestPeriod) query.getResultList().get(0);
		
		assertEquals(tp.description, tpFromDB.description);
	}
}
