package test.ru.fasop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.fasop.ExperimentCalendar;
import ru.fasop.TestDay;
import ru.fasop.TestPeriod;
import ru.fasop.TestSubject;
import ru.fasop.TimeSlot;

public class ExperimentCalendarTest extends GertContentTestCase {
	
	public void testHasAppointmentToday(){
		//ExperimentCalendar eCal = ExperimentCalendar.getInstance();
		
		List<TestPeriod> testPeriods = new ArrayList<TestPeriod>();
		
		TestPeriod tp1 = new TestPeriod("a first period");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		cal.add(Calendar.DAY_OF_MONTH, 1);
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		cal.add(Calendar.DAY_OF_MONTH, 1);
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		testPeriods.add(tp1);		
				
		ExperimentCalendar eCal = new ExperimentCalendar(testPeriods);
		
		assertEquals(1, eCal.testPeriods.size());
		
		for(int i = 0; i < eCal.testPeriods.get(0).testdays.size(); i++){
			TestDay td = eCal.testPeriods.get(0).testdays.get(i);
			td.addTimeslot(new TimeSlot("0900", "1030", 3));
		}
		
		TestSubject subj = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");
		
		assertTrue(! eCal.hasAppointmentToday(subj));
		
		eCal.testPeriods.get(0).testdays.get(1).timeslots.get(0).getSpot(0).subj = subj;
		
		assertTrue(! eCal.hasAppointmentToday(subj));
		
		eCal.testPeriods.get(0).testdays.get(0).timeslots.get(0).getSpot(0).subj = subj;
		
		assertTrue(eCal.hasAppointmentToday(subj));
	}
	
	public void testIndexOfTodaysAppointment(){
		//ExperimentCalendar eCal = ExperimentCalendar.getInstance();
		
		List<TestPeriod> testPeriods = new ArrayList<TestPeriod>();
		
		TestPeriod tp1 = new TestPeriod("a first period");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		cal.add(Calendar.DAY_OF_MONTH, -1);
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		cal.add(Calendar.DAY_OF_MONTH, 1);
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		cal.add(Calendar.DAY_OF_MONTH, 1);
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		testPeriods.add(tp1);		
				
		ExperimentCalendar eCal = new ExperimentCalendar(testPeriods);
		
		assertEquals(1, eCal.testPeriods.size());
		
		for(int i = 0; i < eCal.testPeriods.get(0).testdays.size(); i++){
			TestDay td = eCal.testPeriods.get(0).testdays.get(i);
			td.addTimeslot(new TimeSlot("0900", "1030", 3));
		}
		
		TestSubject subj = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");
		
		eCal.testPeriods.get(0).testdays.get(1).timeslots.get(0).getSpot(0).subj = subj;
		
		assertEquals(0, eCal.indexOfTodaysAppointment(subj));
		
		eCal.testPeriods.get(0).testdays.get(0).timeslots.get(0).getSpot(0).subj = subj;
		
		assertEquals(1, eCal.indexOfTodaysAppointment(subj));
		
		eCal.testPeriods.get(0).testdays.get(2).timeslots.get(0).getSpot(0).subj = subj;
		
		assertEquals(1, eCal.indexOfTodaysAppointment(subj));
	}
	
	public void testGetTodaysAppointment(){
		List<TestPeriod> testPeriods = new ArrayList<TestPeriod>();
		
		TestPeriod tp1 = new TestPeriod("a first period");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		testPeriods.add(tp1);		
				
		ExperimentCalendar eCal = new ExperimentCalendar(testPeriods);
		
		TestDay td = eCal.testPeriods.get(0).testdays.get(0);
		td.addTimeslot(new TimeSlot("0900", "1030", 3));
		
		TestSubject subj = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");
		
		assertEquals(null , eCal.getTodaysAppointment(subj));
		
		eCal.testPeriods.get(0).testdays.get(0).timeslots.get(0).getSpot(0).subj = subj;
		
		assertEquals(true , eCal.getTodaysAppointment(subj).isTaken());
	}
	
	public void testGetNextAppointment(){
		List<TestPeriod> testPeriods = new ArrayList<TestPeriod>();
		
		TestPeriod tp1 = new TestPeriod("a first period");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		cal.add(Calendar.DAY_OF_MONTH, 1);
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		cal.add(Calendar.DAY_OF_MONTH, 1);
		tp1.addTestDay(new TestDay(sdf.format(cal.getTimeInMillis())));
		
		testPeriods.add(tp1);
				
		ExperimentCalendar eCal = new ExperimentCalendar(testPeriods);
		
		TestDay td = eCal.testPeriods.get(0).testdays.get(0);
		td.addTimeslot(new TimeSlot("0900", "1030", 3));
		
		td = eCal.testPeriods.get(0).testdays.get(1);
		td.addTimeslot(new TimeSlot("0900", "1030", 3));
		
		td = eCal.testPeriods.get(0).testdays.get(2);
		td.addTimeslot(new TimeSlot("0900", "1030", 3));
		
		TestSubject subj = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");
		
		assertEquals(null, eCal.getNextAppointmentYYYYMMDD(subj));
		
		eCal.testPeriods.get(0).testdays.get(0).timeslots.get(0).getSpot(0).subj = subj;
		eCal.testPeriods.get(0).testdays.get(1).timeslots.get(0).getSpot(0).subj = subj;
				
		String expectedNextAppointmentYYYYMMDD = eCal.testPeriods.get(0).testdays.get(1).date;
		
		assertEquals(expectedNextAppointmentYYYYMMDD, eCal.getNextAppointmentYYYYMMDD(subj));
	}
}
