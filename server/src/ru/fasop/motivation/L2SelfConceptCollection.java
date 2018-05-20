package ru.fasop.motivation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import ru.fasop.log.PossibleSelvesSurveySession;

public class L2SelfConceptCollection {
	public static final String TYPE_PERCENTILE = "quintile";
	public static final String TYPE_ZSCORE = "zscore";

	DecimalFormat zScoreResultFormat = new DecimalFormat("#,##0.0");
	
	List<PossibleSelvesSurveySession> pselvesSesssions = new ArrayList<PossibleSelvesSurveySession>();
	
	public L2SelfConceptCollection(){}
	
	public void addPossibleSelvesSurveySession(PossibleSelvesSurveySession possibleSelvesSurveySession) {
		pselvesSesssions.add(possibleSelvesSurveySession);
	}

	public double getPickScore(PossibleSelvesSurveySession sesh, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).countCardPicks();
		}
		
		return getCategoryByStat(data, sesh.countCardPicks(), method);
	}
	
	public double getUnpickScore(PossibleSelvesSurveySession sesh, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).countCardUnpicks();
		}
		
		return getCategoryByStat(data, sesh.countCardUnpicks(), method);
	}
	
	public double getMispickScore(PossibleSelvesSurveySession sesh, List<L2PossibleSelf> originalPselves, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).countCardMispicks(originalPselves);
		}
		
		return getCategoryByStat(data, sesh.countCardMispicks(originalPselves), method);
	}
	
	public double getTimeScore(PossibleSelvesSurveySession sesh, String pageId, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).getTimeOnPage(pageId);
		}
		
		return getCategoryByStat(data, sesh.getTimeOnPage(pageId), method);
	}
	
	public double getActiveTimeScore(PossibleSelvesSurveySession sesh, String pageId, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).getActiveTimeOnPage(pageId);
		}
		
		return getCategoryByStat(data, sesh.getActiveTimeOnPage(pageId), method);
	}

	public double getReshufflesScore(PossibleSelvesSurveySession sesh, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).countDesiredCardReshuffles();
		}
		
		return getCategoryByStat(data, sesh.countDesiredCardReshuffles(), method);
	}

	public double getForwardBrowsesScore(PossibleSelvesSurveySession sesh, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).countForwardsBrowsingInstances();
		}
		
		return getCategoryByStat(data, sesh.countForwardsBrowsingInstances(), method);
	}

	public double getBackwardsBrowsesScore(PossibleSelvesSurveySession sesh, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).countBackwardsBrowsingInstances();
		}
		
		return getCategoryByStat(data, sesh.countBackwardsBrowsingInstances(), method);
	}

	public double getRangeScore(PossibleSelvesSurveySession sesh, String pageId, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).getUpperSortLimit(pageId) - pselvesSesssions.get(i).getLowerSortLimit(pageId);
		}
		
		return getCategoryByStat(data, sesh.getUpperSortLimit(pageId) - sesh.getLowerSortLimit(pageId), method);
	}

	public double getRangeCentreScore(PossibleSelvesSurveySession sesh, String pageId, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).getRangeCentre(pageId);
		}
		
		return getCategoryByStat(data, sesh.getRangeCentre(pageId), method);
	}

	public double getHitsScore(PossibleSelvesSurveySession sesh, String pageId, int thresholdAsPercentage, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).countDesiredHits(pageId, thresholdAsPercentage);
		}
		
		return getCategoryByStat(data, sesh.countDesiredHits(pageId, thresholdAsPercentage), method);
	}

	public double getDesirednessDispersionScore(PossibleSelvesSurveySession sesh, String pageId, String method) {
		double[] data = new double[pselvesSesssions.size()];
		
		for(int i = 0; i < pselvesSesssions.size(); i++){
			data[i] = pselvesSesssions.get(i).getDesirednessSD(pageId);
		}
		
		return getCategoryByStat(data, sesh.getDesirednessSD(pageId), method);
	}
	
	double getCategoryByStat(double[] data, double val, String type){
		if(TYPE_ZSCORE.equals(type)){
			DescriptiveStatistics ds = new DescriptiveStatistics(data);
			
			double signedSDProportion = (val - ds.getMean()) / ds.getStandardDeviation();
			//signedSDProportion = Double.valueOf(zScoreResultFormat.format(signedSDProportion));
			
			return signedSDProportion;
		}
		else if(TYPE_PERCENTILE.equals(type)){
			DescriptiveStatistics ds = new DescriptiveStatistics(data);
			
			if(val <= ds.getPercentile(20)){
				return 1;
			}
			else if(ds.getPercentile(20) < val && val <= ds.getPercentile(40)){
				return 2;
			}
			else if(ds.getPercentile(40) < val && val <= ds.getPercentile(60)){
				return 3;
			}
			else if(ds.getPercentile(60) < val && val <= ds.getPercentile(80)){
				return 4;
			}
			else if(ds.getPercentile(80) < val && val <= ds.getPercentile(100)){
				return 5;
			}
			else{
				throw new IllegalStateException("value was not in a quartile.");
			}
		}
		else {
			throw new IllegalArgumentException("Unknown dispersion stat: " + type);
		}
	}
}
