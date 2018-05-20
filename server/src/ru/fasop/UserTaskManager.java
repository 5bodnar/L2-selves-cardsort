package ru.fasop;

import java.util.List;

public class UserTaskManager {

	public FASOPUserTask getTaskForSubject(TestSubject subject, FASOPUserTaskList taskList) {
		FASOPUserTask task = null;
		
		try{	
			if(subject.currentTaskIndex < taskList.tasks.size()){
				task = taskList.tasks.get(subject.currentTaskIndex.intValue());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return task;
	}

	public TestSubject updateSubjectTaskIndices(TestSubject subject, FASOPUserTaskList taskList) {
		try {
			if(subject.currentTaskIndex + 1 < taskList.tasks.size()){
				subject.currentTaskIndex++;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return subject;
	}

}
