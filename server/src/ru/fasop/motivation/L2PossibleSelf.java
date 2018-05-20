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
public class L2PossibleSelf {
	public static final String POLARITY_POS = "positive";
	public static final String POLARITY_NEG = "negative";
	public static final String POLARITY_POS_OR_NEG = "positive_or_negative";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public Long parentId;
	public Boolean isOriginal = false;
	public Boolean isCustom = false;
	
	public String originalPolarity;
	public String polarity;
	
	public String pSelfAsText;
	public String pSelfAsTextDutch;
	
	public Double desiredness;
	public Double efficacy;
	public Double likelihood;
	
	public Double xCoordDesiredness;
	public Double xCoordEfficacy;
	public Double xCoordLikelihood;
	public Double yCoordDesiredness;
	public Double yCoordEfficacy;
	public Double yCoordLikelihood;
	
	public Double relativeImportance;
	
	// get rid of this when you have time (and keep the single one)
	@OneToMany(cascade={CascadeType.MERGE, CascadeType.REFRESH})
	@OrderBy("id")
	public List<L2SelfConstruct> associatedConstructs = new ArrayList<L2SelfConstruct>();
	
	public L2SelfConstruct associatedConstruct = null;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<GertKeyValuePair> metadata = new ArrayList<GertKeyValuePair>();
		
	public L2PossibleSelf() {}
	
	public L2PossibleSelf(String pSelfAsText) {
		this.pSelfAsText = pSelfAsText;
	}
	
	// need to add a method that checks for valid state
	// if desirednessMax == -1 and desiredness == -1 OK
	// if desirednessMax == -1 and desiredness > -1 invalid and needs to be fixed
	
	public L2PossibleSelf getInstance(){
		L2PossibleSelf inst = new L2PossibleSelf(this.pSelfAsText);
		
		inst.parentId = this.id;
		inst.isOriginal = false;
		inst.isCustom = this.isCustom;
		inst.originalPolarity = polarity;
				
		inst.desiredness = new Double(-1);
		inst.efficacy = new Double(-1);
		inst.likelihood = new Double(-1);
		
		inst.xCoordDesiredness = new Double(-1);
		inst.xCoordEfficacy = new Double(-1);
		inst.xCoordLikelihood = new Double(-1);
		
		inst.yCoordDesiredness = new Double(-1);
		inst.yCoordEfficacy = new Double(-1);
		inst.yCoordLikelihood = new Double(-1);
		
		inst.relativeImportance = new Double(-1);
				
		inst.associatedConstructs = new ArrayList<L2SelfConstruct>();
		
		inst.associatedConstruct = associatedConstruct;
		
		for(L2SelfConstruct c : this.associatedConstructs){
			inst.associatedConstructs.add(c);		
		}
		
		inst.metadata = new ArrayList<GertKeyValuePair>();
		
		for(int i = 0; i < this.metadata.size(); i++){
			inst.metadata.add(new GertKeyValuePair(this.metadata.get(i).kee,
													this.metadata.get(i).valyou));		
		}
		
		return inst;
	}
	
	@Override public boolean equals(Object aThat) {
		if(null == aThat){
			return false;
		}
		else if(! (aThat instanceof L2PossibleSelf)){
			return false;
		}
		else{
			L2PossibleSelf other = (L2PossibleSelf) aThat;
			return this.pSelfAsText.equals(other.pSelfAsText);
		}
	}
}
