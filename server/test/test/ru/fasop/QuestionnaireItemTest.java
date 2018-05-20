package test.ru.fasop;

import javax.persistence.Query;

import ru.fasop.GertString;
import ru.fasop.QuestionnaireItem;

public class QuestionnaireItemTest extends GertContentTestCase {
	
	public void testConstructorForLikertType(){
		Long parentId = new Long(7);
		String content = "The feedback provided by the system is very useful.";
		int type = QuestionnaireItem.TYPE_LIKERT;
		String category = "feedback";
		String comment = "This is a comment about that item";
						
		QuestionnaireItem qi = new QuestionnaireItem(parentId, type, category, content, comment); 
		
		assertEquals(parentId, qi.parentId);
		assertEquals(content, qi.content);
		assertEquals(type, qi.type.intValue());
		assertEquals(category, qi.category);
		assertEquals(comment, qi.comment);
		
		qi = new QuestionnaireItem(parentId, QuestionnaireItem.TYPE_OPEN_LONG, category, "Additional Comments", comment); 
		
		assertEquals(parentId, qi.parentId);
		assertEquals("Additional Comments", qi.content);
		assertEquals(QuestionnaireItem.TYPE_OPEN_LONG, qi.type.intValue());
		assertEquals(category, qi.category);
		assertEquals(comment, qi.comment);
	}
	
	public void testConstructorForMultipleChoiceType(){
		Long parentId = new Long(7);
		String content = "Niveau van laatst gevolgde opleiding";
		int type = QuestionnaireItem.TYPE_MULTICHOICE;
		String category = "general";
		String comment = "Some comment here";
						
		QuestionnaireItem qi = new QuestionnaireItem(parentId, type, category, content, comment); 
		qi.addMultipleChoiceItems(new String[]{"WO(Universiteit","MBO","basisschool"},
									new Integer[] {GertString.TYPE_PROVIDED, GertString.TYPE_PROVIDED, GertString.TYPE_PROVIDED});
		
		assertEquals(parentId, qi.parentId);
		assertEquals(content, qi.content);
		assertEquals(type, qi.type.intValue());
		assertEquals(category, qi.category);
		assertEquals(3, qi.mcitems.size());
		assertEquals("WO(Universiteit", qi.mcitems.get(0).content);
	}
	
	public void testToSessionLogId(){
		Long parentId = new Long(7);
		String content = "Niveau van laatst gevolgde opleiding";
		int type = QuestionnaireItem.TYPE_MULTICHOICE;
		String category = "general";
		String comment = "Some comment here";
						
		QuestionnaireItem qi = new QuestionnaireItem(parentId, type, category, content, comment); 
		qi.addMultipleChoiceItems(new String[]{"WO(Universiteit","MBO","basisschool"},
									new Integer[] {GertString.TYPE_PROVIDED, GertString.TYPE_PROVIDED, GertString.TYPE_PROVIDED});
		
		qi.id = new Long(21);
		
		String expectedId = "niveau_van_laatst_gevolgde_opleiding";
		
		assertEquals(expectedId + qi.id, qi.toSessionLogId());
	}
	
	public void testLoadAndSave(){
		Long parentId = new Long(7);
		String content = "The feedback provided by the system is very useful.";
		int type = QuestionnaireItem.TYPE_LIKERT;
		String category = "feedback";
		String comment = "This is a comment about that item";
						
		QuestionnaireItem qi = new QuestionnaireItem(parentId, type, category, content, comment); 
		con.create(qi);
		
		Query query = mgr.createQuery("select e from QuestionnaireItem e order by e.id asc");

		assertEquals(1, query.getResultList().size());
		
		QuestionnaireItem qiFromDb = (QuestionnaireItem) query.getResultList().get(0);
		
		assertEquals(qi.content, qiFromDb.content);
	}

}
