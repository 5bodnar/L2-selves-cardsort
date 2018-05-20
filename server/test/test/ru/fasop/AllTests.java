package test.ru.fasop;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.ru.fasop.log.LogEventParamTest;
import test.ru.fasop.log.LogEventTest;
import test.ru.fasop.log.QuestionnaireResultTest;
import test.ru.fasop.log.QuestionnaireSessionProcessorTest;
import test.ru.fasop.log.SessionTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(TimeSlotTest.class);
		suite.addTestSuite(FASOPUserTaskTest.class);
		suite.addTestSuite(UserTaskManagerTest.class);
		suite.addTestSuite(FASOPUserGroupTest.class);
		suite.addTestSuite(TestSubjectTest.class);
		suite.addTestSuite(TestSpotTest.class);
		suite.addTestSuite(QuestionnaireItemTest.class);
		suite.addTestSuite(GertContentTestCase.class);		
		suite.addTestSuite(TestPeriodTest.class);
		suite.addTestSuite(ExperimentCalendarTest.class);
		suite.addTestSuite(FASOPUserTaskListTest.class);
		suite.addTestSuite(TestDayTest.class);		
		suite.addTestSuite(LogEventParamTest.class);
		suite.addTestSuite(QuestionnaireSessionProcessorTest.class);
		suite.addTestSuite(SessionTest.class);
		suite.addTestSuite(QuestionnaireResultTest.class);
		suite.addTestSuite(LogEventTest.class);		
		//$JUnit-END$
		return suite;
	}

}
