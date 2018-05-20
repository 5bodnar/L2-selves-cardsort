package test.ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import test.ru.fasop.GertContentTestCase;

public class LogEventTest extends GertContentTestCase {

    public void testConstructors() {
    	Long timestamp = new Long(12345);
    	List<LogEventParam> params = new ArrayList<LogEventParam>(); 
    	
		LogEvent event = new LogEvent(timestamp,
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										params);
		
		assertEquals(timestamp, event.timestamp);
		assertEquals(LogEvent.ACTOR_USER, event.actor);
		assertEquals(LogEvent.ACT_USER_LOGIN, event.act);
		assertEquals(params.size(), event.params.size());
    }
	
    public void testLoadAndSave(){
    	Long timestamp = new Long(12345);
    	List<LogEventParam> params = new ArrayList<LogEventParam>(); 
    	params.add(new LogEventParam(LogEvent.PARAM_USER, "sbodnar"));
    	
		LogEvent event = new LogEvent(timestamp,
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_LOGIN,
										params);
		
		event.save(super.mgr);
		
		Query query = mgr.createQuery("select e from LogEvent e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		LogEvent eventFromDB = (LogEvent) query.getResultList().get(0);
		
		assertEquals(timestamp, eventFromDB.timestamp);
		assertEquals(params.size(), eventFromDB.params.size());
		assertEquals("sbodnar", eventFromDB.getParam(LogEvent.PARAM_USER));
	}
}
