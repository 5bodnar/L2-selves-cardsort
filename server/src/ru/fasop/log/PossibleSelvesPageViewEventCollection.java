package ru.fasop.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.fasop.motivation.L2PossibleSelf;

public class PossibleSelvesPageViewEventCollection extends PageViewEventCollection{
	
	public int countErrorDialogViewsForMustChooseThreeUnwantedSelves(){
		if(! "pselves_select_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			int views = 0;
			
			for(LogEvent e : events){
				if("show_dialog".equals(e.act)
					&& "select_dialog_min_3_notwants_err_mesg".equals(e.getParam("dialog_contents"))){ 
					views++;
				}
			}
			
			return views;
		}
	}
	
	public int countCoOccurenceOfErrorDialogViewAndCustomCardButtonPress(){
		if(! "pselves_select_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			int count = 0;
			
			for(LogEvent e : events){
				if("show_dialog".equals(e.act)
					&& "select_dialog_min_3_notwants_err_mesg".equals(e.getParam("dialog_contents"))){ 
					
					if(0 < countCustomCardButtonPressOnSelectCardsPage()){
						count++;
					}
				}
			}
			
			return count;
		}
	}
	
	public int countCustomCardButtonPressOnSelectCardsPage(){
		if(! "pselves_select_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			int count = 0;
			
			for(LogEvent e : events){
				if("press_button".equals(e.act)
					&& "make_custom_cards_button".equals(e.getParam("button_name"))){ 
					
					count++;
				}
			}
			
			return count;
		}
	}
	
	public int countCardPicks(){
		if("pselves_inst_2_try_drag_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_final".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			int picks = 0;
			
			for(LogEvent e : events){
				if("drop_pselve_card".equals(e.act)
						&& ("undesirable_pselves".equals(e.getParam("drop_target"))
								|| "desirable_pselves".equals(e.getParam("drop_target")))){ 
					picks++;
				}
			}
			
			return picks;
		}
	}
	
	public int countCardPickThenMoveOccurences(){
		if("pselves_inst_2_try_drag_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_final".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			Map<String, Integer> dropCounts = new HashMap<String, Integer>();
			
			for(LogEvent e : events){
				if("drop_pselve_card".equals(e.act)
						&& ("undesirable_pselves".equals(e.getParam("drop_target"))
								|| "desirable_pselves".equals(e.getParam("drop_target")))){ 
					
					String cardText = e.getParam("card_text");
					
					if(! dropCounts.keySet().contains(cardText)){
						dropCounts.put(cardText, 1);
					}
					else {
						dropCounts.put(cardText, dropCounts.get(cardText) + 1);
					}
				}
			}
			
			int pickThenMoves = 0;
			
			for(String key : dropCounts.keySet()){
				if(1 < dropCounts.get(key)){
					pickThenMoves++;
				}
			}
			
			return pickThenMoves;
		}
	}
	
	public int countDesiredCardPicks(){
		if("pselves_inst_2_try_drag_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_final".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			int picks = 0;
			
			for(LogEvent e : events){
				if("drop_pselve_card".equals(e.act)
						&& "desirable_pselves".equals(e.getParam("drop_target"))){ 
					picks++;
				}
			}
			
			return picks;
		}
	}
	
	public int countUnDesiredCardPicks(){
		if("pselves_inst_2_try_drag_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_final".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			int picks = 0;
			
			for(LogEvent e : events){
				if("drop_pselve_card".equals(e.act)
						&& "undesirable_pselves".equals(e.getParam("drop_target"))){ 
					picks++;
				}
			}
			
			return picks;
		}
	}

	public int countCardUnpicks() {
		if("pselves_inst_2_try_drag_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_final".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else {
			int unpicks = 0;
			
			for(LogEvent e : events){
				if("drop_pselve_card".equals(e.act)
						&& "pselves_card_deck".equals(e.getParam("drop_target"))){ 
					unpicks++;
				}
			}
			
			return unpicks;
		}
	}
	
	public int countCustomCardsAuthored(){
		int cardsAuthored = 0;
		
		for(LogEvent e : events){
			if("author_custom_pself".equals(e.act)){ 
				cardsAuthored++;
			}
		}
		
		return cardsAuthored;
	}
	
	public List<String> getCustomAuthoredCardContents(){
		List<String> customAuthordCardContent = new ArrayList<String>();
		
		for(LogEvent e : events){
			if("author_custom_pself".equals(e.act)){ 
				customAuthordCardContent.add(e.getParam("content"));
			}
		}
		
		return customAuthordCardContent;
	}
	
	
	public int countCardCustomPicks(){
		
		System.out.println("<" + getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID) + ">");
		
		if("add_custom_pself".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_final".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			int picks = 0;
			
			for(LogEvent e : events){
				if("drop_pselve_card".equals(e.act)
						&& ("undesirable_pselves".equals(e.getParam("drop_target"))
								|| "desirable_pselves".equals(e.getParam("drop_target")))){ 
					picks++;
				}
			}
			
			return picks;
		}
		else {
			return 0;
		}
	}
	
	public int countForwardsBrowsingInstances() {
//		if(! "pselves_select_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
//		return 0;
//	}
//	else {
		int count = 0;
		
		for(LogEvent e : events){
			if("press_button".equals(e.act)
					&& "next_pselves_browse_button".equals(e.getParam("button_name"))){ 
				count++;
			}
		}
		
		return count;
	//}
	}
	
	public int countBackwardsBrowsingInstances() {
//		if(! "pselves_select_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
//			return 0;
//		}
//		else {
			int count = 0;
			
			for(LogEvent e : events){
				if("press_button".equals(e.act)
						&& "prev_pselves_browse_button".equals(e.getParam("button_name"))){ 
					count++;
				}
			}
			
			return count;
		//}
	}
	
	public int countCardDrops() {
		return countEvents("drop_pselve_card_in_sorting_list");
	}
	
	public int countCardReshuffles() {
		HashMap<String, Integer> dropCounts = new HashMap<String, Integer>();
		
		for(LogEvent e : events){
			if("drop_pselve_card_in_sorting_list".equals(e.act)){
				String cardId = e.getParam("card_pselve_id");
				
				if(dropCounts.containsKey(cardId)){
					dropCounts.put(cardId, dropCounts.get(cardId) + 1);
				}
				else{
					dropCounts.put(cardId, 1);
				}
			}
		}
		
		int reshuffles = 0;
		for(String key : dropCounts.keySet()){
			if(1 < dropCounts.get(key)){
				reshuffles++;
			}
		}
		
		return reshuffles;
	}
	
	public int countDesiredCardReshuffles() {
		if(! "pselves_sort_free_desirable_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else{
			HashMap<String, Integer> dropCounts = new HashMap<String, Integer>();
			
			for(LogEvent e : events){
				if("drop_pselve_card_in_sorting_list".equals(e.act)){
					String cardId = e.getParam("card_pselve_id");
					
					if(dropCounts.containsKey(cardId)){
						dropCounts.put(cardId, dropCounts.get(cardId) + 1);
					}
					else{
						dropCounts.put(cardId, 1);
					}
				}
			}
			
			int reshuffles = 0;
			for(String key : dropCounts.keySet()){
				if(1 < dropCounts.get(key)){
					reshuffles++;
				}
			}
			
			return reshuffles;
		}
	}
	
	public int countUndesiredCardReshuffles() {
		if(! "pselves_sort_free_undesirable_pselves".equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else{
			HashMap<String, Integer> dropCounts = new HashMap<String, Integer>();
			
			for(LogEvent e : events){
				if("drop_pselve_card_in_sorting_list".equals(e.act)){
					String cardId = e.getParam("card_pselve_id");
					
					if(dropCounts.containsKey(cardId)){
						dropCounts.put(cardId, dropCounts.get(cardId) + 1);
					}
					else{
						dropCounts.put(cardId, 1);
					}
				}
			}
			
			int reshuffles = 0;
			for(String key : dropCounts.keySet()){
				if(1 < dropCounts.get(key)){
					reshuffles++;
				}
			}
			
			return reshuffles;
		}
	}

	public int countCardMispicks(List<L2PossibleSelf> originalPselves) {
		if(! PossibleSelvesSurveySession.PAGE_ID_SELECT_PSELVES.equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			return 0;
		}
		else{
			int count = 0;
					
			for(LogEvent e : events){
				if("drop_pselve_card".equals(e.act)){
					
					//String cardPselfId = e.getParam("card_pselve_id");
					String cardPselfText = e.getParam("card_text").trim();
					
					//if(null != cardPselfId){
					if(null != cardPselfText){
						//L2PossibleSelf ps = getPSelfById(new Long(cardPselfId), originalPselves);
						L2PossibleSelf ps = getPSelfByText(cardPselfText, originalPselves);
						
						if(null != ps){
							if("undesirable_pselves".equals(e.getParam("drop_target"))
									&& L2PossibleSelf.POLARITY_POS.equals(ps.originalPolarity)){
								count++;
							}
							else if("desirable_pselves".equals(e.getParam("drop_target"))
									&& L2PossibleSelf.POLARITY_NEG.equals(ps.originalPolarity)){ 
								count++;
							}
						}
					}
					else {
						throw new IllegalStateException("Found drop_pselve_card event (" + e.id + ") but could not find card_pselve_id value");
					}
				}
			}
			
			return count;
		}
	}
	
	public int countDesirednessMispicks(List<L2PossibleSelf> originalPselves, double thresholdAsPercentage) {
		Map<String, Double> pSelfAndDesirednessPairs = new HashMap<String, Double>();
		
		if(PossibleSelvesSurveySession.PAGE_ID_DES_DESIREDNESS.equals(getPageViewEvent().getParam(LogEvent.PARAM_PAGE_ID))){
			for(LogEvent e : events){
				
				if("drop_pselve_card_in_sorting_list".equals(e.act)){
					
					//String cardPselfId = e.getParam("card_pselve_id");
					String cardPselfText = e.getParam("card_text").trim();
					
					//if(null != cardPselfId){
					if(null != cardPselfText){
						//L2PossibleSelf ps = getPSelfById(new Long(cardPselfId), originalPselves);
						L2PossibleSelf ps = getPSelfByText(cardPselfText, originalPselves);
						
						if(null != ps){
							if(L2PossibleSelf.POLARITY_NEG.equals(ps.originalPolarity)){
								double dropDivHeight = new Double(e.getParam("drop_div_height"));
								double yPosition = new Double(e.getParam("pself_div_top_from_zero"));
								
								Double desirednessAsPercentage = 100 * ((dropDivHeight - yPosition) / dropDivHeight); 
								
								pSelfAndDesirednessPairs.put(cardPselfText, desirednessAsPercentage);
							}
						}
					}
					else {
						throw new IllegalStateException("Found drop_pselve_card event (" + e.id + ") but could not find card_pselve_id value");
					}
				}
			}
		}
		
		int count = 0;
		
		for(String pSelf : pSelfAndDesirednessPairs.keySet()){
			if(pSelfAndDesirednessPairs.get(pSelf) > thresholdAsPercentage){
				count++;
				
				System.out.println(pSelf);
				System.out.println(pSelfAndDesirednessPairs.get(pSelf) + " vs. " + thresholdAsPercentage);
				
			}
		}
		
		return count;
	}


	private L2PossibleSelf getPSelfById(Long id, List<L2PossibleSelf> originalPselves) {
		for(L2PossibleSelf ps : originalPselves){
			if(id.equals(ps.id)){ 
				return ps;
			}
		}
		
		System.out.println("Warning: could not find pself with id: " + id + ". Returning null");
		
		return null;
	}
	
	public L2PossibleSelf getPSelfByText(String text, List<L2PossibleSelf> originalPselves) {
		text = text.toLowerCase().replace("belgium", "the netherlands");
		
		if(-1 != text.indexOf("h5>")){
			text = text.substring(text.indexOf("h5>") + "h5>".length());
		}
		
		text = text.replaceAll("\\s+", " ").trim();
				
		for(L2PossibleSelf ps : originalPselves){
			if(text.equals(ps.pSelfAsText.toLowerCase())){ 
				return ps;
			}
		}
		
		System.out.println("Warning: could not find pself with text: " + text + ". Returning null");
		
		return null;
	}

	public Double getLowerSortLimit() {
		double lowerLimit = -1.0;
		
		for(LogEvent e : events){
			if("drop_pselve_card_in_sorting_list".equals(e.act)){
				
				double dropDivHeight = new Double(e.getParam("drop_div_height"));
				
				//Here, positions lower on the screen actually receive higher y values...
				// the origin is at the top
				double yPosition = new Double(e.getParam("pself_div_top_from_zero"));
				
				double tempLowerLimit = dropDivHeight - yPosition;
				
				if(tempLowerLimit < lowerLimit || lowerLimit == -1){
					lowerLimit = tempLowerLimit;
				}
				                           
			}
		}
		
		return lowerLimit;
	}

	public Double getUpperSortLimit() {
		double upperLimit = -1.0;
		
		for(LogEvent e : events){
			if("drop_pselve_card_in_sorting_list".equals(e.act)){
				
				double dropDivHeight = new Double(e.getParam("drop_div_height"));
				
				//Here, positions lower on the screen actually receive higher y values...
				// the origin is at the top
				double yPosition = new Double(e.getParam("pself_div_top_from_zero"));
				
				double tempUpperLimit = dropDivHeight - yPosition;
				
				if(tempUpperLimit > upperLimit || upperLimit == -1){
					upperLimit = tempUpperLimit;
				}
				                           
			}
		}
		
		return upperLimit;
	}

	public double getSummedCardDesiredness() {
		List<String> recorded = new ArrayList<String>();
		
		double sum = 0;
		
		for(int i = events.size() - 1; i >= 0; i--){
			LogEvent e = events.get(i);
			
			if("drop_pselve_card_in_sorting_list".equals(e.act) && ! recorded.contains(e.getParam("card_pselve_id"))){
				sum += new Double(e.getParam("pself_div_top_abs"));
				
				recorded.add(e.getParam("card_pselve_id"));
			}
		}
		
		return sum;
	}
	
	public double getSummedCardCounts() {
		List<String> recorded = new ArrayList<String>();
		
		int count = 0;
		
		for(int i = events.size() - 1; i >= 0; i--){
			LogEvent e = events.get(i);
			
			if("drop_pselve_card_in_sorting_list".equals(e.act) && ! recorded.contains(e.getParam("card_pselve_id"))){
				count++;
				
				recorded.add(e.getParam("card_pselve_id"));
			}
		}
		
		return count;
	}

	public List<Double> getDesirednessDataPoints(String pageId) {
		List<Double> vals = new ArrayList<Double>();
		
		List<String> recorded = new ArrayList<String>();
		
		for(int i = events.size() - 1; i >= 0; i--){
			LogEvent e = events.get(i);
			
			if("drop_pselve_card_in_sorting_list".equals(e.act) && ! recorded.contains(e.getParam("card_pselve_id"))){
				vals.add(new Double(e.getParam("pself_div_top_abs")));
				recorded.add(e.getParam("card_pselve_id"));
			}
		}
		
		return vals;
	}
}
