package ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import ru.fasop.GertDatabaseConnection;
import ru.fasop.TestSubject;
import ru.fasop.motivation.L2PossibleSelf;
import ru.fasop.motivation.L2SelfConceptInstance;

public class PossibleSelvesSurveySession extends Session {

	public static final String PAGE_ID_SELECT_PSELVES = "pselves_select_pselves";
	public static final String PAGE_ID_DES_DESIREDNESS = "pselves_sort_free_desirable_pselves";
	public static final String PAGE_ID_DES_UNDESIREDNESS = "pselves_sort_free_undesirable_pselves";
	public static final String PAGE_ID_EFF_DESIREDNESS = "sort_self_efficacy_desirable_pselves";
	public static final String PAGE_ID_EFF_UNDESIREDNESS = "sort_self_efficacy_undesirable_pselves";
	public static final String PAGE_ID_RATE_LIKELIHOOD = "pselves_rate_likelihood_pselves";
	public static final String PAGE_ID_AUTHOR_CUSTOM_PSELVES = "add_custom_pself";
		
	List<PossibleSelvesPageViewEventCollection> pViewCollections = new ArrayList<PossibleSelvesPageViewEventCollection>();
	
	public PossibleSelvesSurveySession(){}
	
	public PossibleSelvesSurveySession(Session session){
		this.id = session.id;
		this.subj = session.subj;
		this.timestamp = session.timestamp;
		this.events = session.events;
		
		SessionLogProcessor proc = new SessionLogProcessor(session);
		proc.seekLogin();
		
		proc.seekNextPageViewEvent();
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		while(null != eventCollection){
			if(eventCollection instanceof PossibleSelvesPageViewEventCollection){
				pViewCollections.add((PossibleSelvesPageViewEventCollection) eventCollection);
			}
			else{
				System.out.println("Got unexpected non-pview page collection: " + eventCollection.events);
			}
			
			eventCollection =  proc.buildPageViewEventCollection();
		}
	}
	
	public PossibleSelvesSurveySession(TestSubject subj, Long timestamp) {
		super(subj, timestamp);
	}
	
	public L2SelfConceptInstance getL2SelfConceptInstance(GertDatabaseConnection con){
		L2SelfConceptInstance inst = null;
		
		String hopesId = getLoginEvent().getParam("hopes_self_concept_instance_id");
		
		inst = (L2SelfConceptInstance) con.querySingle("select e from L2SelfConceptInstance e where e.id = " + hopesId);
		
		return inst;
	}

	public int countCardPicks() {
		int picks = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			picks += pvc.countCardPicks();
		}
		
		return picks;
	}
	
	public int countCardPickThenMoveOccurences() {
		int picks = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			picks += pvc.countCardPickThenMoveOccurences();
		}
		
		return picks;
	}
	
	public int countDesiredCardPicks() {
		int picks = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			picks += pvc.countDesiredCardPicks();
		}
		
		return picks;
	}
	
	public int countUnDesiredCardPicks() {
		int picks = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			picks += pvc.countUnDesiredCardPicks();
		}
		
		return picks;
	}

	public int countCardUnpicks() {
		int unpicks = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			unpicks += pvc.countCardUnpicks();
		}
		
		return unpicks;
	}
	
	public int countForwardsBrowsingInstances() {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			count += pvc.countForwardsBrowsingInstances();
		}
		
		return count;
	}
	
	public int countBackwardsBrowsingInstances() {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			count += pvc.countBackwardsBrowsingInstances();
		}
		
		return count;
	}

	public long getTimeOnPage(String pageId) {
		long time = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if(pageId.equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				time += -1 != pvc.duration ? pvc.duration : pvc.getDurationUntilLastPageEvent(); 
			}
		}
		
		return time;
	}
	
	public long getActiveTimeOnPage(String pageId) {
		long time = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if(pageId.equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				
				long tempTime = -1 != pvc.duration ? pvc.duration : pvc.getDurationUntilLastPageEvent();
				
				if(tempTime > (60 * 1000 * 30)){
					tempTime = tempTime - pvc.getLargestPeriodBetweenAdjacentUserEvents();
				}
				
				time +=  tempTime;
			}
		}
		
		return time;
	}

	public int countCardMispicks(List<L2PossibleSelf> originalPselves) {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			count += pvc.countCardMispicks(originalPselves);
		}
		
		return count;
	}

	public Long getL2SelfConceptId(String type) {
		LogEvent loginEvent = getLoginEvent();
		
		if("hopes".equals(type)){
			return new Long(loginEvent.getParam("hopes_self_concept_instance_id"));
		}
		else if("fears".equals(type)){
			return new Long(loginEvent.getParam("fears_self_concept_instance_id"));
		}
		else{
			return null;
		}
	}

	public int countSortByDesirednessCardDrops() {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if( "pselves_sort_free_desirable_pselves".equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				count += pvc.countCardDrops();
			}
		}
		
		return count;
	}
	
	public int countCardReshuffles() {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			count += pvc.countCardReshuffles();
		}
		
		return count;
	}
	
	public int countDesiredCardReshuffles() {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			count += pvc.countDesiredCardReshuffles();
		}
		
		return count;
	}
	
	public int countDesirednessMispicks(List<L2PossibleSelf> originalPselves, int thresholdAsPercentage){
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			count += pvc.countDesirednessMispicks(originalPselves, thresholdAsPercentage);
		}
		
		return count;
	}

	public int countUndesiredCardReshuffles() {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			count += pvc.countUndesiredCardReshuffles();
		}
		
		return count;
	}
	
	public int countCustomCardsAuthored() {
		int picks = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			picks += pvc.countCustomCardsAuthored();
		}
		
		return picks;
	}
	
	public int countCardCustomPicks() {
		int picks = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			picks += pvc.countCardCustomPicks();
		}
		
		return picks;
	}
	
	public List<String> getCustomAuthoredCardContents(){
		List<String> customAuthordCardContent = new ArrayList<String>();
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			customAuthordCardContent.addAll(pvc.getCustomAuthoredCardContents());
		}
		
		return customAuthordCardContent;
	}

	public Double getSortingBoxHeight(String pageId) {
		Double sortingBoxHeight = null;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if(pageId.equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				
				if(null != pvc.getSortingAreaHeight()){
					sortingBoxHeight = pvc.getSortingAreaHeight(); 
				}
			}
		}
		
		if(null == sortingBoxHeight){
			System.out.println("Warning: PossibleSelvesSurveySession did not contain a sorting height for page with id " + pageId + " in session " + this.id);
		}
		
		return sortingBoxHeight;
	}

	public Double getLowerSortLimit(String pageId) {
		Double lowerLimit = new Double(-1);
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if(pageId.equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				if(-1 < pvc.getLowerSortLimit()){
					lowerLimit = pvc.getLowerSortLimit();
				}
			}
		}
		
		return lowerLimit;
	}

	public Double getUpperSortLimit(String pageId) {
		Double upperLimit = new Double(-1);
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if(pageId.equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				if(-1 < pvc.getUpperSortLimit()){
					upperLimit = pvc.getUpperSortLimit();
				}
			}
		}
		
		return upperLimit;
	}

	public Double getRangeCentre(String pageId) {
		List<Double> desirednessPoints = getDesirednessDataPoints(pageId);
		
		DescriptiveStatistics ds = new DescriptiveStatistics();
		
		for(Double val : desirednessPoints){
			ds.addValue(val.doubleValue());
		}
		
		return ds.getMean();
	}

	public Double getDesirednessSD(String pageId) {
		List<Double> desirednessPoints = getDesirednessDataPoints(pageId);
		
		DescriptiveStatistics ds = new DescriptiveStatistics();
		
		for(Double val : desirednessPoints){
			ds.addValue(val.doubleValue());
		}
		
		return ds.getStandardDeviation();
	}
	
	public int countDesiredHits(String pageId, int thresholdAsPercentage) {
		int hits = 0;
		
		double threshold = getSortingBoxHeight(pageId) * (thresholdAsPercentage / 100.0);
		
		List<Double> values = getDesirednessDataPoints(pageId);
		
		for(Double val : values){
			if(threshold <= val){
				hits++;
			}
		}
		
		return hits;
	}
	
	public List<Double> getDesirednessDataPoints(String pageId) {
		List<Double> desirednessPoints = new ArrayList<Double>();
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if(pageId.equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				desirednessPoints.addAll(pvc.getDesirednessDataPoints(pageId)); 
			}
		}
		
		return desirednessPoints;
	}

	// for project with Vanessa due to giving incorrect pselves set second time around after dialog
	public int countDialogViewsForMustChooseThreeUnwantedSelves() {
		int views = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if("pselves_select_pselves".equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				views += pvc.countErrorDialogViewsForMustChooseThreeUnwantedSelves();
			}
		}
		
		return views;
	}
	
	// for project with Vanessa due to giving incorrect pselves set second time around after dialog
	public int countCustomCardButtonPressOnSelectCardsPage() {
		int presses = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if("pselves_select_pselves".equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				presses += pvc.countCustomCardButtonPressOnSelectCardsPage();
			}
		}
		
		return presses;
	}
	
	// for project with Vanessa due to giving incorrect pselves set second time around after dialog
	public int countCoOccurenceOfErrorDialogViewAndCustomCardButtonPress() {
		int count = 0;
		
		for(PossibleSelvesPageViewEventCollection pvc : pViewCollections){
			if("pselves_select_pselves".equals(pvc.getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
				count += pvc.countCoOccurenceOfErrorDialogViewAndCustomCardButtonPress();
			}
		}
		
		return count;
	}
}
