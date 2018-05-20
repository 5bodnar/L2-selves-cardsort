package test.ru.fasop.motivation;

import java.util.List;

import ru.fasop.motivation.LanguageAndCulturePair;
import test.ru.fasop.GertContentTestCase;

public class LanguageAndCulturePairTest extends GertContentTestCase {
	
	public void testConstructor(){
		String lang = "English";
		String culture = "The Phillipines";
		
		LanguageAndCulturePair pair = new LanguageAndCulturePair(lang, culture);
		
		assertEquals(lang, pair.lang);
		
	}
	
	public void testGetLanguages(){
		LanguageAndCulturePair landCPair1 = new LanguageAndCulturePair("French", "France");
		con.create(landCPair1);
		
		LanguageAndCulturePair landCPair2 = new LanguageAndCulturePair("French", "Canada");
		con.create(landCPair2);
		
		LanguageAndCulturePair landCPair3 = new LanguageAndCulturePair("Dutch", "The Netherlands");
		con.create(landCPair3);
		
		List<String> languages = LanguageAndCulturePair.getLanguages(con);
		
		assertEquals("Dutch", languages.get(0));
		assertEquals("French", languages.get(1));
		assertEquals("French", languages.get(2));
	}
	
	public void testGetCountriesByLanguage(){
		LanguageAndCulturePair landCPair1 = new LanguageAndCulturePair("French", "France");
		con.create(landCPair1);
		
		LanguageAndCulturePair landCPair2 = new LanguageAndCulturePair("French", "Canada");
		con.create(landCPair2);
		
		LanguageAndCulturePair landCPair3 = new LanguageAndCulturePair("Dutch", "The Netherlands");
		con.create(landCPair3);
		
		List<String> countries = LanguageAndCulturePair.getCulturesByLanguage(con, "French");
		
		assertEquals("Canada", countries.get(0));
		assertEquals("France", countries.get(1));
	}
	
	public void testLanguageByCulture(){
		LanguageAndCulturePair landCPair1 = new LanguageAndCulturePair("French", "France");
		con.create(landCPair1);
		
		LanguageAndCulturePair landCPair2 = new LanguageAndCulturePair("French", "Canada");
		con.create(landCPair2);
		
		LanguageAndCulturePair landCPair3 = new LanguageAndCulturePair("Dutch", "The Netherlands");
		con.create(landCPair3);
		
		List<String> countries = LanguageAndCulturePair.getLanguageByCulture(con, "France");
		
		assertEquals("French", countries.get(0));
	}
}
