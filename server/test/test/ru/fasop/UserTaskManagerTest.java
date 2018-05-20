package test.ru.fasop;

import java.util.ArrayList;
import java.util.List;

import ru.fasop.FASOPUserTask;
import ru.fasop.FASOPUserTaskList;
import ru.fasop.TestSubject;
import ru.fasop.UserTaskManager;

public class UserTaskManagerTest extends GertContentTestCase {
	// given a user and an experiment schedule,
	// decide what task the user should work on next
	// if there is no suitable task, return null
	// otherwise, return the task
	// logic:
	// tasks are done in the order they are contained in the list
	// scheduled tasks can only be completed if the learner has an appointment
	
	public void testGetTaskReturnsNullIfNoSuitableTaskList(){
		TestSubject subj = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");
		
		UserTaskManager mgr = new UserTaskManager();
		FASOPUserTaskList emptyTaskList = new FASOPUserTaskList("a name", FASOPUserTaskList.TYPE_ANYTIME);
		
		FASOPUserTask task = mgr.getTaskForSubject(subj, emptyTaskList);
		
		assertEquals(null, task);
	}
	
	public void testGetTaskListReturnsBookmarkedTask(){
		TestSubject subject = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");
		
		UserTaskManager mgr = new UserTaskManager();
		
		FASOPUserTaskList threeItemTaskList = new FASOPUserTaskList("a taskList", FASOPUserTaskList.TYPE_ANYTIME);
		threeItemTaskList.tasks.add(new FASOPUserTask("task1", "Please do task 1", "Task1Controller", new Integer(15)));
		threeItemTaskList.tasks.add(new FASOPUserTask("task2", "Please do task 2", "Task2Controller", new Integer(15)));
		threeItemTaskList.tasks.add(new FASOPUserTask("task3", "Please do task 3", "Task3Controller", new Integer(15)));

		subject.currentTaskIndex = new Long(0);
		
		FASOPUserTask task = mgr.getTaskForSubject(subject, threeItemTaskList);
		
		assertEquals("task1", task.label);
		
		subject.currentTaskIndex = new Long(2);
		
		task = mgr.getTaskForSubject(subject, threeItemTaskList);
		
		assertEquals("task3", task.label);
		
		subject.currentTaskIndex = new Long(100);
		
		task = mgr.getTaskForSubject(subject, threeItemTaskList);
		
		assertEquals(null, task);
	}
	
	public void testUpdateSubjectTaskIndices(){
		TestSubject subject = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");

		FASOPUserTaskList taskList1 = new FASOPUserTaskList("taskList 1", FASOPUserTaskList.TYPE_ANYTIME);
		taskList1.tasks.add(new FASOPUserTask("task1", "Please do task 1", "Task1Controller", new Integer(15)));
		taskList1.tasks.add(new FASOPUserTask("task2", "Please do task 2", "Task2Controller", new Integer(15)));
		taskList1.tasks.add(new FASOPUserTask("task2", "Please do task 3", "Task2Controller", new Integer(15)));
		
		UserTaskManager mgr = new UserTaskManager();
				
		subject.currentTaskIndex = new Long(0);
		
		assertEquals(new Long(0), subject.currentTaskIndex);
		
		
		subject = mgr.updateSubjectTaskIndices(subject, taskList1);
		
		assertEquals(new Long(1), subject.currentTaskIndex);
		
		
		subject = mgr.updateSubjectTaskIndices(subject, taskList1);
		
		assertEquals(new Long(2), subject.currentTaskIndex);
		
		
		subject = mgr.updateSubjectTaskIndices(subject, taskList1);
		
		assertEquals(new Long(2), subject.currentTaskIndex);
	}
	
	public void testUpdateSubjectTaskIndicesForScheduledTasks(){
		FASOPUserTaskList taskList1 = new FASOPUserTaskList("taskList 1", FASOPUserTaskList.TYPE_SCHEDULED);
		taskList1.tasks.add(new FASOPUserTask("task1", "Please do task 1", "Task1Controller", new Integer(15)));
		taskList1.tasks.add(new FASOPUserTask("task2", "Please do task 2", "Task2Controller", new Integer(15)));
		
		UserTaskManager mgr = new UserTaskManager();
		
		TestSubject subject = new TestSubject("Steve", "s.bodnar@let.ru.nl", "12345", "test");
		
		subject.currentTaskIndex = new Long(1);
		
		subject = mgr.updateSubjectTaskIndices(subject, taskList1);
				
		subject.currentTaskIndex = new Long(1);
		
		//change 'today' to second appointment
		
		subject = mgr.updateSubjectTaskIndices(subject, taskList1);
		
		//assertEquals(new Long(1), subject.currentTaskListIndex);
//		assertEquals(new Long(0), subject.currentTaskIndex);
//		
//		subject = mgr.updateSubjectTaskIndices(subject, lists, testSpot);
//		
//		assertEquals(new Long(1), subject.currentTaskListIndex);
//		assertEquals(new Long(1), subject.currentTaskIndex);
//		
//		subject = mgr.updateSubjectTaskIndices(subject, lists, testSpot);
//		
//		assertEquals(new Long(1), subject.currentTaskListIndex);
//		assertEquals(new Long(2), subject.currentTaskIndex);
	}
}
