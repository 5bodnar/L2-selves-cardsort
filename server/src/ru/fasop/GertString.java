package ru.fasop;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GertString {	
	public static final int TYPE_PROVIDED = 0;
	public static final int TYPE_BLANK = 1;
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String content;
	public Integer optionType;
	
	public GertString(){}
	
	public GertString(String content, Integer optionType){
		this.content = content;
		this.optionType = optionType;
	}
	
	public int getType(){
		if(null != optionType){
			return optionType.intValue();
		}
		else {
			return new Integer(-1);
		}
	}
	
	public GertString clone(){
		return new GertString(content, optionType);
	}
	
	public String toString(){
		return content;
	}
}
