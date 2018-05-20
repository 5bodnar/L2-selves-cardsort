package test.ru.fasop;

import javax.persistence.Query;

import ru.fasop.FASOPUserGroup;
import ru.fasop.TestSubject;

public class FASOPUserGroupTest extends GertContentTestCase {
	public void testUserList(){
		String name = "Experiment 1 - EXP";
		
		FASOPUserGroup group = new FASOPUserGroup(name);
		
		assertEquals(name, group.name);
		
		assertEquals(0, group.userList.size());
		
		group.userList.add(new TestSubject("Subj 1", "subj1@gmail.com", "12345", "pword1"));
		group.userList.add(new TestSubject("Subj 2", "subj2@gmail.com", "12345", "pword2"));
		group.userList.add(new TestSubject("Subj 3", "subj3@gmail.com", "12345", "pword3"));
		
		assertEquals(3, group.userList.size());
	}
	
	public void testContainsUser(){
		String name = "Experiment 1 - EXP";
		
		FASOPUserGroup group = new FASOPUserGroup(name);
		
		assertEquals(name, group.name);
		
		assertEquals(0, group.userList.size());
		
		TestSubject subj1 = new TestSubject("Subj 1", "subj1@gmail.com", "12345", "pword1");
		
		group.userList.add(subj1);
		group.userList.add(new TestSubject("Subj 2", "subj2@gmail.com", "12345", "pword2"));
		group.userList.add(new TestSubject("Subj 3", "subj3@gmail.com", "12345", "pword3"));
		
		assertTrue(group.containsUser(subj1));
		assertTrue(! group.containsUser(new TestSubject("Subj 4", "subj4@gmail.com", "12345", "pword3")));
	}
	
	public void testLoadAndSave(){
		FASOPUserGroup group = new FASOPUserGroup("Some test group");
		
		TestSubject subj1 = new TestSubject("Subj 1", "subj1@gmail.com", "12345", "pword1");
		TestSubject subj2 = new TestSubject("Subj 2", "subj2@gmail.com", "12345", "pword2");
		
		con.create(subj1);
		con.create(subj2);
		
		group.userList.add(subj1);
		group.userList.add(subj2);
		
		con.create(group);
		
		Query query = mgr.createQuery("select e from FASOPUserGroup e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		FASOPUserGroup groupFromDB = (FASOPUserGroup) query.getResultList().get(0);
		
		assertEquals(group.userList.size(), groupFromDB.userList.size());
	}
}
