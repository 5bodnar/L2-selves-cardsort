package ru.fasop;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GertKeyValuePair {	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String kee;
	public String valyou;
	
	public GertKeyValuePair(){}
	
	public GertKeyValuePair(String key, String value){
		this.kee = key;
		this.valyou = value;
	}
}
