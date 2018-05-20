package ru.fasop;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ExperimentCalendar {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public List<TestPeriod> testPeriods;

	public ExperimentCalendar(List<TestPeriod> testPeriods) {
		this.testPeriods = testPeriods;
	}

	public boolean hasAppointmentToday(TestSubject subj) {
		String todayYYYYMMDD = sdf.format(Calendar.getInstance().getTimeInMillis());
		
		for(int i = 0; i < testPeriods.size(); i++){
			TestPeriod tp = testPeriods.get(i);
			
			for(int j = 0; j < tp.testdays.size(); j++){
				TestDay td = tp.testdays.get(j);
				
				for(int k = 0; todayYYYYMMDD.equals(td.date) && k < td.timeslots.size(); k++){
					TimeSlot slot = td.timeslots.get(k);
					
					for(int l = 0; l < slot.spots.size(); l++) {
						if(slot.getSpot(l).isTaken()){
							TestSubject ts = slot.getSpot(l).subj;
							
							if(ts.email.equals(subj.email))
								return true;
						}
					}
				}
			}
		}
		
		return false;
	}

	public int indexOfTodaysAppointment(TestSubject subj) {
		int index = 0;
		
		String todayYYYYMMDD = sdf.format(Calendar.getInstance().getTimeInMillis());
		
		for(int i = 0; i < testPeriods.size(); i++){
			TestPeriod tp = testPeriods.get(i);
			
			for(int j = 0; j < tp.testdays.size(); j++){
				TestDay td = tp.testdays.get(j);
				
				if(todayYYYYMMDD.equals(td.date)){
					return index;
				}
				else {
					for(int k = 0; k < td.timeslots.size(); k++){
						TimeSlot slot = td.timeslots.get(k);
						
						for(int l = 0; l < slot.spots.size(); l++) {
							if(slot.getSpot(l).isTaken()){
								TestSubject ts = slot.getSpot(l).subj;
								
								if(ts.email.equals(subj.email))
									index++;
							}
						}
					}
				}
			}
		}
		
		return index;
	}

	public TestSpot getTodaysAppointment(TestSubject subj) {
		TestSpot spot = null;
		
		String todayYYYYMMDD = sdf.format(Calendar.getInstance().getTimeInMillis());
		
		for(int i = 0; i < testPeriods.size(); i++){
			TestPeriod tp = testPeriods.get(i);
			
			for(int j = 0; j < tp.testdays.size(); j++){
				TestDay td = tp.testdays.get(j);
				
				if(todayYYYYMMDD.equals(td.date)){
					for(int k = 0; k < td.timeslots.size(); k++){
						TimeSlot slot = td.timeslots.get(k);
						
						for(int l = 0; l < slot.spots.size(); l++) {
							if(slot.getSpot(l).isTaken()){
								TestSubject ts = slot.getSpot(l).subj;
								
								if(ts.email.equals(subj.email))
									return slot.getSpot(l);
							}
						}
					}
				}
			}
		}
		
		return spot;
	}

	public String getNextAppointmentYYYYMMDD(TestSubject subj) {
		String todayYYYYMMDD = sdf.format(Calendar.getInstance().getTimeInMillis());
		
		for(int i = 0; i < testPeriods.size(); i++){
			TestPeriod tp = testPeriods.get(i);
			
			for(int j = 0; j < tp.testdays.size(); j++){
				TestDay td = tp.testdays.get(j);
				
				//System.out.println(todayYYYYMMDD + "," + td.date + ": " + todayYYYYMMDD.compareTo(td.date));
				
				if(-1 == todayYYYYMMDD.compareTo(td.date)){
					for(int k = 0; k < td.timeslots.size(); k++){
						TimeSlot slot = td.timeslots.get(k);
						
						for(int l = 0; l < slot.spots.size(); l++) {
							if(slot.getSpot(l).isTaken()){
								TestSubject ts = slot.getSpot(l).subj;
								
								if(ts.email.equals(subj.email))
									return td.date;
							}
						}
					}
				}
			}
		}
		
		return null;
	}

}
