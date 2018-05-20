package ru.fasop.motivation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import ru.fasop.GertKeyValuePair;

@Entity
public class L2SelfConstruct {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String name;
	public String description;
	
	@OneToMany(cascade=CascadeType.PERSIST)
	@OrderBy("id")
	public List<L2PossibleSelf> relatedPossibleSelves = new ArrayList<L2PossibleSelf>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<GertKeyValuePair> metadata = new ArrayList<GertKeyValuePair>();
	
	public L2SelfConstruct() {}
	
	public L2SelfConstruct(String name) {
		this.name = name;
	}
	
	public List<L2PossibleSelf> getRelatedPossibleSelves(String polarity){
		List<L2PossibleSelf> pselves = new ArrayList<L2PossibleSelf>();

		for(L2PossibleSelf p : relatedPossibleSelves){
			if(! pselves.contains(p)
					&& (polarity.equals(p.originalPolarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.originalPolarity))){
				pselves.add(p);
			}
		}
		
		return pselves;
	}
	
	public List<L2PossibleSelf> getRelatedPossibleSelves(String polarity, boolean includePosOrNeg){
		List<L2PossibleSelf> pselves = new ArrayList<L2PossibleSelf>();

		for(L2PossibleSelf p : relatedPossibleSelves){
			if(! pselves.contains(p)
					&& (polarity.equals(p.originalPolarity) || (includePosOrNeg && L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.originalPolarity)))){
				pselves.add(p);
			}
		}
		
		return pselves;
	}
	
	@Override public boolean equals(Object aThat) {
		if(null == aThat){
			return false;
		}
		else if(! (aThat instanceof L2SelfConstruct)){
			return false;
		}
		else{
			L2SelfConstruct other = (L2SelfConstruct) aThat;
			return this.name.equals(other.name);
		}
	}
}
