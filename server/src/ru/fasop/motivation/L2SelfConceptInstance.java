package ru.fasop.motivation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import ru.fasop.GertDatabaseConnection;
import ru.fasop.GertKeyValuePair;
import ru.fasop.TestSubject;
import ru.fasop.log.PossibleSelvesSurveySession;
import ru.fasop.log.Session;

@Entity
public class L2SelfConceptInstance {
	public static final String DIM_DESIREDNESS = "dim_desiredness";
	public static final String DIM_EFFICACY = "dim_efficacy";
	public static final String DIM_LIKELIHOOD = "dim_likelihood";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@OneToOne(cascade={CascadeType.REFRESH})
	public TestSubject testSubject;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	List<L2PossibleSelf> pSelves = new ArrayList<L2PossibleSelf>();
	
	public Long createdTimestamp;
	public Long lastUpdatedTimestamp;
	
	public Double maxValDesiredness;
	public Double maxValEfficacy;
	public Double maxValLikelihood;
	
	public L2SelfConceptInstance(){}
	
	public L2SelfConceptInstance(TestSubject testSubject) {
		this.testSubject = testSubject;
	}
	
	public void addPossibleSelf(L2PossibleSelf pSelf) {
		pSelves.add(pSelf);
	}
	
	public int count(){
		return pSelves.size();
	}
		
	public boolean contains(Object other){
		if(null == other){
			return false;
		}
		else if(! (other instanceof L2PossibleSelf)){
			return false;
		}
		
		L2PossibleSelf p2 = (L2PossibleSelf) other;
		
		for(L2PossibleSelf p : pSelves){
			if(p.parentId.equals(p2.parentId)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsPselfWithId(Long id){
		for(L2PossibleSelf p : pSelves){
			if(id.equals(p.id)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsPselfWithParentId(Long id){
		for(L2PossibleSelf p : pSelves){
			if(id.equals(p.parentId)){
				return true;
			}
		}
		
		return false;
	}
	
	public List<L2SelfConstruct> getUniqueConstructs(){
		List<L2SelfConstruct> uniqueConstructs = new ArrayList<L2SelfConstruct>();

		for(L2PossibleSelf p : pSelves){
			for(L2SelfConstruct c : p.associatedConstructs){
				if(! uniqueConstructs.contains(c)){
					uniqueConstructs.add(c);
				}
			}
		}
		
		return uniqueConstructs;
	}
	
	public List<L2SelfConstruct> getUniqueConstructs(String polarity){
		List<L2SelfConstruct> uniqueConstructs = new ArrayList<L2SelfConstruct>();

		for(L2PossibleSelf p : pSelves){
			for(L2SelfConstruct c : p.associatedConstructs){
				if(! uniqueConstructs.contains(c)
						&& polarity.equals(p.polarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity)){
					uniqueConstructs.add(c);
				}
			}
		}
		
		return uniqueConstructs;
	}
	
	public List<L2PossibleSelf> getL2PossibleSelves(String polarity){
		List<L2PossibleSelf> l2PossibleSelves = new ArrayList<L2PossibleSelf>();
		
		for(L2PossibleSelf p : pSelves){
			if(polarity.equals(p.polarity)
					|| L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity)){
				l2PossibleSelves.add(p);
			}
		}
		
		return l2PossibleSelves;
	}
	
	public List<L2PossibleSelf> getL2PossibleSelvesByConstruct(L2SelfConstruct c){
		List<L2PossibleSelf> l2PossibleSelves = new ArrayList<L2PossibleSelf>();
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c)){
				l2PossibleSelves.add(p);
			}
		}
		
		return l2PossibleSelves;
	}
	
	public List<L2PossibleSelf> getL2PossibleSelvesByConstruct(L2SelfConstruct c, String polarity){
		List<L2PossibleSelf> l2PossibleSelves = new ArrayList<L2PossibleSelf>();
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c)
						&& (polarity.equals(p.polarity)
								|| L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity))){
				l2PossibleSelves.add(p);
			}
		}
		
		return l2PossibleSelves;
	}
	
	public Double getDesirednessByConstruct(L2SelfConstruct c) {
		double totDesiredness = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c) && -1 < p.desiredness){
				totDesiredness += p.desiredness;
			}
		}
		
		return new Double(totDesiredness);
	}
	
	public Double getDesirednessByConstruct(L2SelfConstruct c, String polarity) {
		double totDesiredness = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c) && -1 < p.desiredness
					&& (polarity.equals(p.polarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity))){
				totDesiredness += p.desiredness;
			}
		}
		
		return new Double(totDesiredness);
	}
	
	public Double getRelativeDesirednessByConstruct(L2SelfConstruct c) {
		double totRelDesiredness = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c) && -1 < p.relativeImportance){
				totRelDesiredness += p.relativeImportance;
			}
		}
		
		return new Double(totRelDesiredness);
	}
	
	public Double getRelativeDesirednessByConstruct(L2SelfConstruct c, String polarity) {
		double totRelDesiredness = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c) && -1 < p.relativeImportance
					&& (polarity.equals(p.polarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity))){
				totRelDesiredness += p.relativeImportance;
			}
		}
		
		return new Double(totRelDesiredness);
	}

	public Double getLikelihoodByConstruct(L2SelfConstruct c) {
		double totLikelihood = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c) && -1 < p.likelihood){
				totLikelihood += p.likelihood;
			}
		}
		
		return new Double(totLikelihood);
	}
	
	public Double getLikelihoodByConstruct(L2SelfConstruct c, String polarity) {
		double totLikelihood = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(p.associatedConstructs.contains(c) && -1 < p.likelihood
					&& (polarity.equals(p.polarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity))){
				totLikelihood += p.likelihood;
			}
		}
		
		return new Double(totLikelihood);
	}
	
	public Double getMaxLikelihood() {
		double maxLikelihood = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(maxLikelihood < p.likelihood){
				maxLikelihood = p.likelihood;
			}
		}
		
		return new Double(maxLikelihood);
	}
	
	public Double getMaxLikelihood(String polarity) {
		double maxLikelihood = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(maxLikelihood < p.likelihood
					&& (polarity.equals(p.polarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity))){
				maxLikelihood = p.likelihood;
			}
		}
		
		return new Double(maxLikelihood);
	}
	
	public Double getMaxDesirability() {
		double maxDesirability = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(maxDesirability < p.desiredness){
				maxDesirability = p.desiredness;
			}
		}
		
		return new Double(maxDesirability);
	}
	
	public Double getMaxDesirability(String polarity) {
		double maxDesirability = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(maxDesirability < p.desiredness
					&& (polarity.equals(p.polarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity))){
				maxDesirability = p.desiredness;
			}
		}
		
		return new Double(maxDesirability);
	}
	
	public Double getMaxRelativeImportance() {
		double maxRelativeImportance = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(maxRelativeImportance < p.relativeImportance){
				maxRelativeImportance = p.relativeImportance;
			}
		}
		
		return new Double(maxRelativeImportance);
	}
	
	public Double getMaxRelativeImportance(String polarity) {
		double maxRelativeImportance = 0;
		
		for(L2PossibleSelf p : pSelves){
			if(maxRelativeImportance < p.relativeImportance
					&& (polarity.equals(p.polarity) || L2PossibleSelf.POLARITY_POS_OR_NEG.equals(p.polarity))){
				maxRelativeImportance = p.relativeImportance;
			}
		}
		
		return new Double(maxRelativeImportance);
	}

	public Double getDesirednessByPSelfParentId(Long otherId) {
		for(L2PossibleSelf p : pSelves){
			if(otherId.equals(p.parentId)){
				return p.desiredness;
			}
		}
		
		return new Double(0);
	}
	
	public Double getEfficacyByPSelfParentId(Long otherId) {
		for(L2PossibleSelf p : pSelves){
			if(otherId.equals(p.parentId)){
				return p.efficacy;
			}
		}
		
		return new Double(0);
	}
	
	public L2PossibleSelf getPSelfById(Long id) {
		for(L2PossibleSelf p : pSelves){
			if(id.equals(p.id)){
				return p;
			}
		}
		
		return null;
	}
	
	public L2PossibleSelf getPSelfByParentId(Long otherId) {
		for(L2PossibleSelf p : pSelves){
			if(otherId.equals(p.parentId)){
				return p;
			}
		}
		
		return null;
	}
	
	public boolean containsPSelfByParentId(Long otherId) {
		for(L2PossibleSelf p : pSelves){
			if(otherId.equals(p.parentId)){
				return true;
			}
		}
		
		return false;
	}
	
	public Double getLikelihoodByPSelfParentId(Long otherId) {
		for(L2PossibleSelf p : pSelves){
			if(otherId.equals(p.parentId)){
				return p.likelihood;
			}
		}
		
		return new Double(0);
	}
	
	public L2SelfConceptInstance getLowerLimitAdjustedInstance(GertDatabaseConnection con){
		L2SelfConceptInstance normalisedInstance = new L2SelfConceptInstance();
		
		normalisedInstance.testSubject = testSubject;
		normalisedInstance.createdTimestamp = createdTimestamp;
		normalisedInstance.lastUpdatedTimestamp = lastUpdatedTimestamp;
		
		PossibleSelvesSurveySession pSesh = getCorrespondingLogSession(con);
		
		Double lowerLimitDesirednessVal = pSesh.getLowerSortLimit(PossibleSelvesSurveySession.PAGE_ID_EFF_DESIREDNESS);
		Double lowerLimitEfficacyVal = pSesh.getLowerSortLimit(PossibleSelvesSurveySession.PAGE_ID_EFF_UNDESIREDNESS);
		
		for(L2PossibleSelf p : pSelves){
			L2PossibleSelf newP = new L2PossibleSelf(p.pSelfAsText);
			
			newP.parentId = p.parentId;
			newP.isOriginal = false;
			newP.isCustom = p.isCustom;
			newP.originalPolarity = p.polarity;
					
			newP.associatedConstructs = new ArrayList<L2SelfConstruct>();
			
			for(L2SelfConstruct c : p.associatedConstructs){
				newP.associatedConstructs.add(c);		
			}
			
			newP.metadata = new ArrayList<GertKeyValuePair>();
			
			for(int i = 0; i < p.metadata.size(); i++){
				p.metadata.add(new GertKeyValuePair(p.metadata.get(i).kee,
														p.metadata.get(i).valyou));		
			}
						
			newP.desiredness = p.desiredness - lowerLimitDesirednessVal;
			newP.efficacy = p.efficacy - lowerLimitEfficacyVal;
						
			newP.likelihood = p.likelihood;
			newP.relativeImportance = p.relativeImportance;
			
			normalisedInstance.pSelves.add(newP);
		}
		
		return normalisedInstance;
	}
	
	public L2SelfConceptInstance getNormalisedInstance(Double newMaxValue) {
		L2SelfConceptInstance normalisedInstance = new L2SelfConceptInstance();
		
		normalisedInstance.testSubject = testSubject;
		normalisedInstance.createdTimestamp = createdTimestamp;
		normalisedInstance.lastUpdatedTimestamp = lastUpdatedTimestamp;
		
		for(L2PossibleSelf p : pSelves){
			L2PossibleSelf newP = new L2PossibleSelf(p.pSelfAsText);
			
			newP.parentId = p.parentId;
			newP.isOriginal = false;
			newP.isCustom = p.isCustom;
			newP.originalPolarity = p.polarity;
			newP.polarity = p.polarity;
					
			newP.associatedConstructs = new ArrayList<L2SelfConstruct>();
			
			for(L2SelfConstruct c : p.associatedConstructs){
				newP.associatedConstructs.add(c);		
			}
			
			newP.metadata = new ArrayList<GertKeyValuePair>();
			
			for(int i = 0; i < p.metadata.size(); i++){
				p.metadata.add(new GertKeyValuePair(p.metadata.get(i).kee,
														p.metadata.get(i).valyou));		
			}
			
			newP.desiredness = new Double(0);
			newP.efficacy = new Double(0);
			newP.likelihood = new Double(0);
			newP.relativeImportance = new Double(0);
			
			if(0 < p.desiredness){
				newP.desiredness = p.desiredness * (newMaxValue / maxValDesiredness);
			}
			
			if(0 < p.efficacy){
				newP.efficacy = p.efficacy * (newMaxValue / maxValEfficacy);
			}
			
			if(0 < p.likelihood){
				newP.likelihood = p.likelihood * (newMaxValue / maxValLikelihood);
			}
			
			if(0 < p.relativeImportance){
				newP.relativeImportance = p.relativeImportance;
			}
			
			normalisedInstance.pSelves.add(newP);
		}
		
		return normalisedInstance;
	}

	public Double getMeanDesiredness() {
		return getTotVal(DIM_DESIREDNESS) / count(DIM_DESIREDNESS);
	}
		
	public Double getMeanEfficacy() {
		return getTotVal(DIM_EFFICACY) / count(DIM_EFFICACY);
	}
	
	public Double getMeanLikelihood() {
		return getTotVal(DIM_LIKELIHOOD) / count(DIM_LIKELIHOOD);
	}
	
	public Double getTotVal(String dimension) {
		double totVal = 0;
		
		if(dimension.equals(DIM_DESIREDNESS)){
			for(L2PossibleSelf p : pSelves){
				if(0 < p.desiredness){
					totVal += p.desiredness;
				}
			}
		}
		else if(dimension.equals(DIM_EFFICACY)){
			for(L2PossibleSelf p : pSelves){
				if(0 < p.efficacy){
					totVal += p.efficacy;
				}
			}
		}
		else if(dimension.equals(DIM_LIKELIHOOD)){
			for(L2PossibleSelf p : pSelves){
				if(0 < p.likelihood){
					totVal += p.likelihood;
				}
			}
		}
		else {
			throw new IllegalArgumentException("Unknown dimension argument: " + dimension);
		}
				
		return new Double(totVal);
	}
	
	public int count(String dimension){
		int count = 0;
		
		if(dimension.equals(DIM_DESIREDNESS)){
			for(L2PossibleSelf p : pSelves){
				if(0 < p.desiredness){
					count++;
				}
			}
		}
		else if (dimension.equals(DIM_EFFICACY)){
			for(L2PossibleSelf p : pSelves){
				if(0 < p.efficacy){
					count++;
				}
			}
		}
		else if( dimension.equals(DIM_LIKELIHOOD)){
			for(L2PossibleSelf p : pSelves){
				if(0 < p.likelihood){
					count++;
				}
			}
		}
		else {
			throw new IllegalArgumentException("Unknown argument: " + dimension);
		}
				
		return count;
	}

	public Double getDesirednessSD() {
		DescriptiveStatistics ds = new DescriptiveStatistics();
		
		for(L2PossibleSelf p : pSelves){
			if(0 <= p.desiredness){
				ds.addValue(p.desiredness);
			}
		}
		
		return new Double(ds.getStandardDeviation());
	}
	
	public Double getEfficacySD() {
		DescriptiveStatistics ds = new DescriptiveStatistics();
		
		for(L2PossibleSelf p : pSelves){
			if(0 <= p.efficacy){
				ds.addValue(p.efficacy);
			}
		}
		
		return new Double(ds.getStandardDeviation());
	}
	
	public Double getLikelihoodSD() {
		DescriptiveStatistics ds = new DescriptiveStatistics();
		
		for(L2PossibleSelf p : pSelves){
			if(0 <= p.likelihood){
				ds.addValue(p.likelihood);
			}
		}
		
		return new Double(ds.getStandardDeviation());
	}

	public List<L2PossibleSelf> getTopKPossibleSelves(int k, String dim) {
		List<L2PossibleSelf> sortedPselves = new ArrayList<L2PossibleSelf>();
		sortedPselves.addAll(pSelves);
		Collections.sort(sortedPselves, getComparator(dim));
		sortedPselves = sortedPselves.subList(0, k);
		return sortedPselves;
	}
	
	public Comparator<L2PossibleSelf> getComparator(String dim){
		if(DIM_DESIREDNESS.equals(dim)){
			return new Comparator<L2PossibleSelf>() {
		        public int compare(L2PossibleSelf p1, L2PossibleSelf p2) {
		            return p2.desiredness.compareTo(p1.desiredness);
		        }
		    };
		}
		else if(DIM_EFFICACY.equals(dim)){
			return new Comparator<L2PossibleSelf>() {
		        public int compare(L2PossibleSelf p1, L2PossibleSelf p2) {
		            return p2.efficacy.compareTo(p1.efficacy);
		        }
		    };
		}
		else if(DIM_LIKELIHOOD.equals(dim)){
			return new Comparator<L2PossibleSelf>() {
		        public int compare(L2PossibleSelf p1, L2PossibleSelf p2) {
		            return p2.likelihood.compareTo(p1.likelihood);
		        }
		    };
		}
		else{
			throw new IllegalArgumentException("Unknown dimension given: " + dim);
		}
	}
	
	public boolean isHit(L2PossibleSelf pself, double threshold) {
		if(containsPSelfByParentId(pself.parentId)
				&& threshold <= pself.desiredness){
			return true;
		}
		else {
			return false;
		}
	}

	public List<L2PossibleSelf> getPSelvesHits(double threshold) {
		List<L2PossibleSelf> hits = new ArrayList<L2PossibleSelf>();
		
		for(L2PossibleSelf p : pSelves){
			if(threshold <= p.desiredness){
				hits.add(p);
			}
		}
		
		return hits;
	}

	public List<L2SelfConstruct> getConstructHits(double threshold, String polarity) {
		List<L2SelfConstruct> hits = new ArrayList<L2SelfConstruct>();
		
		for(L2SelfConstruct c : getUniqueConstructs()){
			if(threshold <= getDesirednessByConstruct(c, polarity)){
				hits.add(c);
			}
		}
		
		return hits;
	}
	
	public List<L2SelfConstruct> getConstructStrictHits(double threshold, String polarity) {
		List<L2SelfConstruct> hits = new ArrayList<L2SelfConstruct>();
		
		for(L2SelfConstruct c : getUniqueConstructs()){
			int hitCount = 0;
			
			List<L2PossibleSelf> pSelves = c.getRelatedPossibleSelves(polarity);
			
			for(L2PossibleSelf protoSelf : pSelves){
				if(threshold <= getDesirednessByPSelfParentId(protoSelf.id)){
					hitCount++;
				}
			}
			
			if(hitCount > 0 && hitCount == pSelves.size()){
				hits.add(c);
			}
		}
		
		return hits;
	}

	public boolean isInterestedInPSelves(double threshold) {
		return 0 < getPSelvesHits(threshold).size();
	}

	public boolean isInterestedInWholeConstruct(double threshold, String polarity) {
		try{
			for(L2SelfConstruct c : getUniqueConstructs()){
				
				//System.out.println(c.name);
				
				int hitCount = 0;
				
				List<L2PossibleSelf> pSelves = c.getRelatedPossibleSelves(polarity);
				
				for(L2PossibleSelf protoSelf : pSelves){
					//System.out.println(protoSelf.pSelfAsText + "| score: " + getDesirednessByPSelfParentId(protoSelf.id));
					
					if(threshold <= getDesirednessByPSelfParentId(protoSelf.id)){
						hitCount++;
					}
				}
				
				if(hitCount > 0 && hitCount == pSelves.size()){
					return true;
				}
			}
			
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean containsCustomPselves() {
		for(L2PossibleSelf p : pSelves){
			if(p.isCustom){
				return true;
			}
		}
		
		return false;
	}
	
	public PossibleSelvesSurveySession getCorrespondingLogSession(GertDatabaseConnection con){
		PossibleSelvesSurveySession pSesh = null;
		
		List<Session> sessions = Session.getPossibleSelvesSessions(con, testSubject);
		
		for(Session sesh : sessions){
			String hopesSelfConceptInstanceId = sesh.getLoginEvent().getParam("hopes_self_concept_instance_id");
			
			if(id.equals(new Long(hopesSelfConceptInstanceId))){
				pSesh = new PossibleSelvesSurveySession(sesh);
			}
		}
		
		return pSesh;
	}
}
