package test.ru.fasop.log;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import ru.fasop.TestSubject;

import junit.framework.TestCase;

public class SessionIntegrationTest extends TestCase {
	
	public void testRetrievingSessionCausesDataCommunicationErrors() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("gert_content_store");
		EntityManager mgr = factory.createEntityManager();
		
		for(int j = 0; j < 100; j++){
			Query query = mgr.createQuery("SELECT e FROM TestSubject e");
							
			assertTrue(0 < query.getResultList().size());
			
			List<TestSubject> results = query.getResultList();
			
			for(int i = 0; i < results.size();i++){
				TestSubject subj = results.get(i);
				
				//System.out.println("Retrieved session for subject: " + subj.email);
				
				query = mgr.createQuery("SELECT e FROM Session e where e.subj.id = " + subj.id + " order by e.id desc");
					        
				//assertTrue(0 < query.getResultList().size());
				
		        List sessions = query.getResultList();
			}
		}
        				
	    mgr.close();		
    }
	
}
