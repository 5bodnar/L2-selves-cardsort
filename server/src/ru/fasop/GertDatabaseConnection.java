package ru.fasop;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.jpa.JpaEntityManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

// try this http://onpersistence.blogspot.nl/2008/04/eclipselink-and-datasources.html
// and this http://www.mchange.com/projects/c3p0/#installation

public class GertDatabaseConnection {
	public static String DATE_SQL = "SELECT e FROM TestSubject e where e.email = 's.bodnar@let.ru.nl'";
	public static int MAX_TRIES = 15;
	boolean isDebugMode = false;
	
	public String storeName = null;
	EntityManagerFactory factory = null;
	
	public void close(){
		factory.close();
	}
	
	public GertDatabaseConnection(){
		if(null == factory){
			/*
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			
			try {
				cpds.setDriverClass( "com.mysql.jdbc.Driver" );
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//loads the jdbc driver
			cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/gert_content" );
			cpds.setUser("root");
			cpds.setPassword("eipi+1=0");
			

			cpds.setAcquireIncrement(5);
			cpds.setMinPoolSize(10);
			cpds.setMaxPoolSize(30);
			
			//cpds.setMaxIdleTime(5*60);
			cpds.setMaxConnectionAge(5*60);
			cpds.setIdleConnectionTestPeriod(60);
			
			cpds.setAcquireRetryAttempts(30);
			cpds.setAcquireRetryDelay(3);
			
			Map properties = new HashMap();
		    properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, cpds);
		    			
		    */
			//factory = Persistence.createEntityManagerFactory("gert_content_store", properties);
			factory = Persistence.createEntityManagerFactory("gert_content_store");
			storeName = "gert_content_store";
		}
	}
	
	public GertDatabaseConnection(boolean isDebugMode){
		if(null == factory){
			factory = Persistence.createEntityManagerFactory("gert_content_store");
			storeName = "gert_content_store";
			isDebugMode = isDebugMode;
		}
	}
	
	public GertDatabaseConnection(String store){
		if(null == factory){
			factory = Persistence.createEntityManagerFactory(store);
			storeName = store;
		}
	}
	
	public Object querySingle(String sql){
		return querySingle(sql, 0);
	}
	
	public Object querySingle(String sql, int numTries){
		Object object = null;
		
		EntityManager mgr = null;
		
		if(numTries < MAX_TRIES){
			try{
				boolean isError = false;
				
				mgr = factory.createEntityManager();
				Query query = mgr.createQuery(sql);
				
				if(1 == query.getResultList().size()){
					object = query.getResultList().get(0);
				}
				else if(1 < query.getResultList().size()){
					isError = true;
				}
				
				if(isError){
					//throw new IllegalStateException("Expected 1 result but got many");
					System.out.println("Expected 1 result but got many:" + query.getResultList().size());
				}
			}
			catch(Exception e){
				System.out.println("Got db exception : " + e);
				System.out.println("Exception was caused by : " + e.getCause());
				
				e.printStackTrace();
				
				if(e instanceof DatabaseException
					&& e.getCause() instanceof CommunicationsException){
					
					closeUpEntityManagerFactoryAndTryAgain(numTries, e, mgr);
					
					return querySingle(sql, numTries + 1);
				}
			}
			finally {
				closeUpEntityManager(mgr);
		    }
		}
		
		if(isDebugMode){
			System.out.println("Method completed on attempt " + numTries);
		}
		
		return object;
	}
	
	public Object queryList(TestSubject user, String sql){
		List<Object> objects = (List<Object>) queryList(sql);
		
		List<Object> filteredObjects = new ArrayList<Object>();
		
		for(Object o : objects){
			OwnerRecord ownerRecord = OwnerRecord.getOwnerRecord(this, o);
			
			if(null != ownerRecord && ownerRecord.owners.contains(user)){
				filteredObjects.add(o);
			}
		}
		
		return filteredObjects;
	}
	
	public Object queryList(String sql){
		return queryList(sql, 0);
	}
	
	public Object queryList(String sql, int numTries){
		List<Object> objects = new ArrayList<Object>();
		
		EntityManager mgr = null;
		
		if(numTries < MAX_TRIES){
			try{	
				mgr = factory.createEntityManager();
				Query query = mgr.createQuery(sql);
				
				for(Object item : query.getResultList()){
					objects.add(item);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				
				if(e instanceof DatabaseException
					&& e.getCause() instanceof CommunicationsException){
					
					closeUpEntityManagerFactoryAndTryAgain(numTries, e, mgr);
					
					return querySingle(sql, numTries + 1);
				}
			}
			finally {
		        closeUpEntityManager(mgr);
		    }
		}
		
		return objects;
	}

	public void create(TestSubject user, Object obj){
		create(obj);
		
		OwnerRecord ownerRecord = new OwnerRecord(obj);
		ownerRecord.owners.add(user);
		
		TestSubject adminUser = (TestSubject) querySingle("select e from TestSubject e where e.email = 's.bodnar@let.ru.nl'");
		
		if(! ownerRecord.owners.contains(adminUser)){
			ownerRecord.owners.add(adminUser);
		}
		
		adminUser = (TestSubject) querySingle("select e from TestSubject e where e.email = 'b.penningdevries@let.ru.nl'");
		
		if(! ownerRecord.owners.contains(adminUser)){
			ownerRecord.owners.add(adminUser);
		}
		
		create(ownerRecord);
	}
	
	public void create(Object obj){
		create(obj, 0);
	}
	
	public void create(Object obj, int numTries){
		EntityManager mgr = null;
		
		if(numTries < MAX_TRIES){
			try {
				mgr = factory.createEntityManager();
				
				mgr.getTransaction().begin();
				
				mgr.persist(obj);
				
				mgr.getTransaction().commit();
				
			}
			catch(Exception e){
				e.printStackTrace();
				
				if(e instanceof DatabaseException
					&& e.getCause() instanceof CommunicationsException){
					
					closeUpEntityManagerFactoryAndTryAgain(numTries, e, mgr);
					
					create(obj, numTries + 1);
				}
			}
			finally {
		        closeUpEntityManager(mgr);
		    }
		}
	}
	
	public void update(Object obj){
		update(obj, 0);
	}
	
	public void update(Object obj, int numTries){
		EntityManager mgr = null;
		
		if(numTries < MAX_TRIES){
			try{
				mgr = factory.createEntityManager();
				
				mgr.getTransaction().begin();
				
				mgr.merge(obj);
				
				mgr.getTransaction().commit();
				
			}
			catch(Exception e){
				e.printStackTrace();
				
				if(e instanceof DatabaseException
					&& e.getCause() instanceof CommunicationsException){
					
					closeUpEntityManagerFactoryAndTryAgain(numTries, e, mgr);
					
					update(obj, numTries + 1);
				}
			}
			finally {
		        closeUpEntityManager(mgr);
		    }
		}
	}
	
	public void remove(Object obj){
		remove(obj, 0);
	}
	
	public void remove(Object obj, int numTries){
		EntityManager mgr = null;
		
		if(numTries < MAX_TRIES){
			try {
				mgr = factory.createEntityManager();
				mgr.getTransaction().begin();
				
				Object newObj = mgr.merge(obj);
				mgr.remove(newObj);
				
				mgr.getTransaction().commit();
				
				((JpaEntityManager) mgr.getDelegate()).getServerSession().getIdentityMapAccessor().invalidateAll();
			}
			catch(Exception e){
				e.printStackTrace();
				
				if(e instanceof DatabaseException
					&& e.getCause() instanceof CommunicationsException){
						
					closeUpEntityManagerFactoryAndTryAgain(numTries, e, mgr);
					
					remove(obj, numTries + 1);
				}
			}
			finally {
		        closeUpEntityManager(mgr);
		    }
		}
	}
	
	public void closeUpEntityManagerFactoryAndTryAgain(int numTries, Exception e, EntityManager mgr) {
		System.out.println("Got db exception : " + e);
		
		e.printStackTrace();
		
		System.out.println("Num tries = " + numTries);
		
		if(numTries < MAX_TRIES){
			System.out.println("Trying again...");
		}
		
		closeUpEntityManager(mgr);
		
		if(null != factory){
			factory.close();
		}
		
		factory = Persistence.createEntityManagerFactory(storeName);
	}

	public void closeUpEntityManager(EntityManager mgr) {
		if(null != mgr){
			if (null != mgr.getTransaction() && mgr.getTransaction().isActive()) {
			    mgr.getTransaction().rollback();
			}
	
			if (mgr.isOpen()) {
			    mgr.close();
			}
		}
	}
}