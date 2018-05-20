package test.ru.fasop;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import ru.fasop.GertDatabaseConnection;

public class GertContentTestCase extends TestCase {

	protected EntityManager mgr;
	
	protected GertDatabaseConnection con = null;
	protected GertDatabaseConnection conProd = null;
	
	public GertContentTestCase(){}
	
	public void setUp() {
		con = new GertDatabaseConnection("gert_content_store_test");
		conProd = new GertDatabaseConnection();
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("gert_content_store_test");
		mgr = factory.createEntityManager();
		
		clearTable("LanguageAndCulturePair");
		clearTable("L2SelfConceptInstance");
		clearTable("L2SelfConstruct");
		clearTable("L2PossibleSelf");
		clearTable("GertKeyValuePair");
		
		clearTable("TestPeriod");
		clearTable("TestDay");
		clearTable("TimeSlot");
		clearTable("TestSpot");		
		
		
		clearTable("Session");
		clearTable("OwnerRecord");
		clearTable("FASOPUserGroup");
		clearTable("TestSubject");
		
		clearTable("LogEvent");
		clearTable("LogEventParam");
		
		clearTable("QuestionnaireItem");
		clearTable("GertString");
		clearTable("FASOPUserTaskList");
		clearTable("FASOPUserTask");
		
		factory = Persistence.createEntityManagerFactory("gert_content_store_test");
		mgr = factory.createEntityManager();
	}
	
	public void testDummy(){}
	
	public void clearTable(String tablename){
		for(Object a : (List) con.queryList("SELECT e FROM " + tablename + " e"))
			con.remove(a);
	}
	
	protected void tearDown() {
		try {
			con.close();
			conProd.close();
			
			mgr.close();
			super.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}