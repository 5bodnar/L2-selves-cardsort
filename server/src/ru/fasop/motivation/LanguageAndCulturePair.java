package ru.fasop.motivation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ru.fasop.GertDatabaseConnection;

@Entity
public class LanguageAndCulturePair {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String culture;
	public String lang;

	public LanguageAndCulturePair() {}
	
	public LanguageAndCulturePair(String lang, String culture) {
		this.lang = lang;
		this.culture = culture;
	}

	public static List<String> getLanguages(GertDatabaseConnection con) {
		List<String> l = new ArrayList<String>();
		
		for(LanguageAndCulturePair pair : (List<LanguageAndCulturePair>) con.queryList("select e from LanguageAndCulturePair e order by e.lang asc")){
			l.add(pair.lang);
		}
		
		return l; 
	}

	public static List<String> getCulturesByLanguage(GertDatabaseConnection con, String lang) {
		return (List<String>) con.queryList("select e.culture from LanguageAndCulturePair e where e.lang = '" + lang +"' order by e.culture asc"); 
	}

	public static List<String> getLanguageByCulture(GertDatabaseConnection con, String culture) {
		return (List<String>) con.queryList("select e.lang from LanguageAndCulturePair e where e.culture = '" + culture +"' order by e.lang asc");
	}

}
