package test.ru.fasop;

import javax.persistence.Query;

import ru.fasop.FASOPUserTask;
import ru.fasop.FASOPUserTaskList;

public class FASOPUserTaskListTest extends GertContentTestCase {
	public void testHasAppointmentToday(){
		String label = "Session 1 Tasklist";
		Integer type = FASOPUserTaskList.TYPE_SCHEDULED;
		
		FASOPUserTaskList taskList = new FASOPUserTaskList(label, type);
		
		taskList.tasks.add(new FASOPUserTask("task1", "Please do task 1", "Task1Controller", new Integer(15)));
		taskList.tasks.add(new FASOPUserTask("task2", "Please do task 2", "Task2Controller", new Integer(15)));
		taskList.tasks.add(new FASOPUserTask("task3", "Please do task 3", "Task3Controller", new Integer(15)));
		
		assertEquals(label, taskList.label);
		assertEquals(type, taskList.type);
		assertEquals(3, taskList.tasks.size());
	}
	
	public void testLoadAndSave(){
		String label = "Session 1 Tasklist";
		Integer type = FASOPUserTaskList.TYPE_SCHEDULED;
		
		FASOPUserTaskList taskList = new FASOPUserTaskList(label, type);
		
		FASOPUserTask task1 = new FASOPUserTask("task1", "Please do task 1", "Task1Controller", new Integer(15));
		con.create(task1);
		taskList.tasks.add(task1);
		
		FASOPUserTask task2 = new FASOPUserTask("task2", "Please do task 2", "Task2Controller", new Integer(15));
		con.create(task2);
		taskList.tasks.add(task2);
		
		FASOPUserTask task3 = new FASOPUserTask("task3", "Please do task 3", "Task3Controller", new Integer(15));
		con.create(task3);
		taskList.tasks.add(task3);
		
		con.create(taskList);
		
		Query query = mgr.createQuery("select e from FASOPUserTaskList e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		FASOPUserTaskList taskListFromDB = (FASOPUserTaskList) query.getResultList().get(0);
		
		assertEquals(taskListFromDB.tasks.size(), taskListFromDB.tasks.size());
	}
	public void testLoadAndSaveListContainingSameTask(){
		String label = "Session 1 Tasklist";
		Integer type = FASOPUserTaskList.TYPE_SCHEDULED;
		
		FASOPUserTaskList taskList = new FASOPUserTaskList(label, type);
		con.create(taskList);
		
		taskList.tasks.add(new FASOPUserTask("task1", "Please do task 1", "Task1Controller", new Integer(15)));
		taskList.tasks.add(new FASOPUserTask("task2", "Please do task 2", "Task2Controller", new Integer(15)));
		taskList.tasks.add(new FASOPUserTask("task3", "Please do task 3", "Task3Controller", new Integer(15)));
		
		Query query = mgr.createQuery("select e from FASOPUserTaskList e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		FASOPUserTaskList taskListFromDB = (FASOPUserTaskList) query.getResultList().get(0);
		
		assertEquals(taskListFromDB.tasks.size(), taskListFromDB.tasks.size());
	}
}
