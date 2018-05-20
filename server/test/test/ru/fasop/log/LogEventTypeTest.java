package test.ru.fasop.log;

import junit.framework.TestCase;
import ru.fasop.log.LogEvent;

public class LogEventTypeTest extends TestCase {
	
	public void testEventType() {
        assertTrue(null !=  LogEvent.ACTOR_UNKNOWN);
		assertTrue(null !=  LogEvent.ACTOR_USER);
		assertTrue(null !=  LogEvent.ACTOR_SYSTEM);
		
		assertTrue(null !=  LogEvent.ACT_USER_VIEW_PAGE);
		assertTrue(null !=  LogEvent.ACT_USER_LOGIN);
		assertTrue(null !=  LogEvent.ACT_USER_LOGOUT);
		assertTrue(null !=  LogEvent.ACT_USER_RATE_CORRECTNESS);
		assertTrue(null !=  LogEvent.ACT_SYS_TIME_EXPIRED);
		assertTrue(null !=  LogEvent.ACT_USER_START_RECORD);
		assertTrue(null !=  LogEvent.ACT_USER_STOP_RECORD);
		assertTrue(null !=  LogEvent.ACT_SYS_SAVE_AUDIO);
    }
	
	public void testParseEventType() {
		//assertEquals(EventType.TYPE_ACTIVITY_END, EventType.parseEventType("TYPE_ACTIVITY_END"))
	}
}
