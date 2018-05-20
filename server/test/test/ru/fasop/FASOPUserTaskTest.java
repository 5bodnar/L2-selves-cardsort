package test.ru.fasop;

import javax.persistence.Query;

import ru.fasop.FASOPUserTask;

public class FASOPUserTaskTest extends GertContentTestCase {
	public void testConstructor(){
		String label = "Session 1 Questionnaire";
		String initialMessage = "Lets start with the questionnaire first";
		String controller = "signup";
						
		FASOPUserTask task = new FASOPUserTask(label, initialMessage, controller, new Integer(15));
		
		assertEquals(label, task.label);
		assertEquals(initialMessage, task.initialMessage);
		assertEquals(controller, task.controller);
		assertEquals(new Integer(15), task.timeLimitInMinutes);		
	}
	
	public void testLoadAndSave(){
		String label = "Session 1 Questionnaire";
		String initialMessage = "Lets start with the questionnaire first";
		String controller = "signup";
		
		FASOPUserTask task = new FASOPUserTask(label, initialMessage, controller, new Integer(15));
		
		con.create(task);
		
		Query query = mgr.createQuery("select e from FASOPUserTask e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		FASOPUserTask taskFromDB = (FASOPUserTask) query.getResultList().get(0);
		
		assertEquals(task.label, taskFromDB.label);		
	}
}
