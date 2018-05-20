package ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LogEventParam {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String name;
	public String value;
	
	public LogEventParam(){}
	
	public LogEventParam(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public LogEventParam clone(){
		return new LogEventParam(name, value);
	}

	public static List<LogEventParam> parse(String str) {
		List<LogEventParam> params = new ArrayList<LogEventParam>();
		
		String[] pairs = str.split(",");
		
		for(String p : pairs){
			String[] pairArr = p.split(":");
			if(2 == pairArr.length){
				params.add(new LogEventParam(pairArr[0].trim(), pairArr[1].trim()));
			}
			else{
				System.out.println("Illegal arguments in LogEvent.parse: " + p);
			}
		}
		
		return params;
	}
	
	public static List<LogEventParam> parse(String str, String paramDelim, String keyValDelim) {
		List<LogEventParam> params = new ArrayList<LogEventParam>();
		
		String[] pairs = str.split(paramDelim);
		
		for(String p : pairs){
			String[] pairArr = p.split(keyValDelim);
			if(2 == pairArr.length){
				params.add(new LogEventParam(pairArr[0].trim(), pairArr[1].trim()));
			}
			else{
				System.out.println("Illegal argument: " + p + " with size: " + pairArr.length);
			}
		}
		
		return params;
	}
}
