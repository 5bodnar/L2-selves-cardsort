package ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import ru.fasop.GertDatabaseConnection;

import ru.fasop.motivation.L2PossibleSelf;

public class PageViewEventCollection {
	private long startTime = -1;
	public long duration = -1;
	
	public List<LogEvent> events = new ArrayList<LogEvent>();
	
	public String paramNameForFeedbackEvent = "type_of_feedback";
	
	GertDatabaseConnection con = null;
	
	public PageViewEventCollection() {}
	
	public PageViewEventCollection(GertDatabaseConnection con){
		this.con = con;
	}

	public long getStartTime() {
		if(0 < events.size()){
			return getEarliestUserEvent().timestamp;
		}
		else {
			return -1;
		}
	}

	public void addEvent(LogEvent event) {
		events.add(event);
	}

	public long getDuration() {
		return duration;
	}
	
	public long getDurationUntilLastPageEvent() {
		LogEvent earliestEvent = getEarliestUserEvent();
		LogEvent latestEvent = getLatestUserEvent();
		
		if(2 <= events.size()){
			return latestEvent.timestamp - earliestEvent.timestamp;
		}
		else {
			return 0;
		}
	}
	
	public long getDurationUntilFirstStartRecordButtonPress() {
		LogEvent earliestEvent = getEarliestUserEvent();
		LogEvent startRecordButtonEvent = getEarliestStartRecordButtonEvent();
		
		if(null != startRecordButtonEvent){
			return startRecordButtonEvent.timestamp - earliestEvent.timestamp;
		}
		else if(2 <= events.size()){
			LogEvent latestEvent = getLatestUserEvent();
			return latestEvent.timestamp - earliestEvent.timestamp;
		}
		else {
			return 0;
		}
	}
		
	public long getDurationFromEventUntilLastPageEvent(LogEvent e) {
		LogEvent latestEvent = getLatestUserEvent();
		
		if(2 <= events.size()){
			return latestEvent.timestamp - e.timestamp;
		}
		else {
			return 0;
		}
	}
	
	public LogEvent getEarliestUserEvent() {
		LogEvent e = null;
		
		for(int i = 0; i < events.size(); i++){
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& (null == e || events.get(i).timestamp < e.timestamp)){
				e = events.get(i);
			}
		}
		
		return e;
	}

	public LogEvent getLatestUserEvent() {
		LogEvent e = null;
		
		for(int i = events.size() - 1; i >= 0; i--){
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& (null == e || events.get(i).timestamp > e.timestamp)){
				e = events.get(i);
			}
		}
		
		return e;
	}
	
	
	public LogEvent getLastPageEvent(){
		return events.get(events.size() - 1);
	}
	
	public LogEvent getLatestEvent(){
		LogEvent latestEvent = null;
		
		for(LogEvent e : getSystemResponseEvents()){
			if(e.timestamp > latestEvent.timestamp){
				latestEvent = e;
			}
		}
		
		return latestEvent;
	}
	
	public LogEvent getFollowingEvent(LogEvent e){
		int i = events.indexOf(e) + 1;
		
		if(i < events.size()){
			return events.get(i);
		}
		else{
			return null;
		}
	}
	
	public LogEvent getEarliestEventThatFollows(LogEvent e){
		LogEvent earliestEventThatFollows = null;
		
		for(int i = 0; i < events.size();i++){
			if(e.timestamp < events.get(i).timestamp){
				if(null == earliestEventThatFollows || events.get(i).timestamp < earliestEventThatFollows.timestamp){
					earliestEventThatFollows = events.get(i);
				}
			}
		}
		
		return earliestEventThatFollows;
	}
	
	public LogEvent getEarliestUserEventThatFollows(LogEvent e){
		LogEvent earliestEventThatFollows = null;
		
		for(int i = 0; i < events.size();i++){
			if(e.timestamp < events.get(i).timestamp){
				if(null == earliestEventThatFollows || ( LogEvent.ACTOR_USER.equals(events.get(i).actor) && events.get(i).timestamp < earliestEventThatFollows.timestamp)){
					earliestEventThatFollows = events.get(i);
				}
			}
		}
		
		return earliestEventThatFollows;
	}
	
	
	public double getUserActivityIndexAsNumEventsPer10Seconds(){
		return 10 * countUserEvents() /  (getDurationUntilLastPageEvent() / (double) 1000);
	}

	public String getPageType() {
		if(isValidCollection()){
			return getPageViewEvent().getParam("pageId");
		}
		else if(0 < events.size()){
			System.out.println("First event in window should be view page but instead is : " + events.get(0) + " with id: " + events.get(0).id);
			System.out.println("Searching through collection for page view event");
			
			LogEvent pageViewEvent = getPageViewEvent();
			
			if(null != pageViewEvent){
				return pageViewEvent.getParam("pageId");
			}
			else{
				throw new IllegalStateException("Collection does not contain a pageview event");
			}
		}
		else {
			throw new IllegalStateException("Expected at least one event but size was : " + events.size());
		}
	}
	
	public boolean isValidCollection(){
		return null != events && 0 < events.size() && null != getPageViewEvent();
	}
	
	public LogEvent getPageViewEvent(){
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_VIEW_PAGE.equals(e.act)){
				return e;
			}
		}
		
		return null;
	}
	
	public boolean isSortingPage() {
		return "pselves_sort_free_desirable_pselves".equals(getPageType())
				|| "pselves_sort_free_undesirable_pselves".equals(getPageType())
				|| "sort_self_efficacy_desirable_pselves".equals(getPageType())
				|| "sort_self_efficacy_undesirable_pselves".equals(getPageType());
	}
	
	public boolean containsDroppedPselfEvent(L2PossibleSelf pself){
		for(LogEvent e : events){
			if("drop_pselve_card_in_sorting_list".equals(e.act)){ 
				if(pself.id.equals(Long.parseLong(e.getParam("card_pselve_id")))){
					return true;
				}
			}
			else if( -1 != e.act.indexOf("drop_pselve_card_in_self_efficacy")){
				if(pself.id.equals(Long.parseLong(e.getParam("card_pselve_id")))){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Double getSortingAreaHeightForDroppedPselfEvent(L2PossibleSelf pself){
		for(LogEvent e : events){
			if("drop_pselve_card_in_sorting_list".equals(e.act)){ 
				if(pself.id.equals(Long.parseLong(e.getParam("card_pselve_id")))){
					return new Double(e.getParam("drop_div_height"));
				}
			}
			else if( -1 != e.act.indexOf("drop_pselve_card_in_self_efficacy")){
				if(pself.id.equals(Long.parseLong(e.getParam("card_pselve_id")))){
					return new Double(e.getParam("drop_div_height"));
				}
			}
		}
		
		return null;
	}
	
	public Double getSortingAreaHeight() {
		Double sortingAreaHeight = null;
		
		if (-1 != getPageType().indexOf("free_desirable_")){
			for(LogEvent e : events){
				if("drop_pselve_card_in_sorting_list".equals(e.act)){
					sortingAreaHeight = new Double(e.getParam("drop_div_height"));
				}
			}
		}
		else if (-1 != getPageType().indexOf("free_undesirable_")){
			for(LogEvent e : events){
				if("drop_pselve_card_in_sorting_list".equals(e.act)){
					sortingAreaHeight =  new Double(e.getParam("drop_div_height"));
				}
			}
		}
		else if (-1 != getPageType().indexOf("efficacy_desirable")){
			for(LogEvent e : events){
				if("drop_pselve_card_in_self_efficacy_desirable_sorting_list".equals(e.act)){
					sortingAreaHeight =  new Double(e.getParam("drop_div_height"));
				}
			}
		}
		else if (-1 != getPageType().indexOf("efficacy_undesirable")){
			for(LogEvent e : events){
				if("drop_pselve_card_in_self_efficacy_undesirable_sorting_list".equals(e.act)){
					sortingAreaHeight =  new Double(e.getParam("drop_div_height"));
				}
			}
		}
		else{
			System.out.println("Unsure of how to deal with page type: " + getPageType());
		}
		
		if(null == sortingAreaHeight){
			System.out.println("Warning: PageViewEventCollection with id " + getPageViewEvent().id + " did not contain a sorting height");
		}
				
		return sortingAreaHeight;
	}
	
	public boolean isMiniMotivationPage() {
		return "mini_motivation_inst_page".equals(getPageType())
				|| "mini_motivation_show_category_page".equals(getPageType());
	}
	
	public boolean isTreatmentQuestionPage() {
		return "greet_training_spoken_question_page".equals(getPageType());
	}
	
	public boolean isTreatmentDebriefPage() {
		return "gert_training_spkn_debrief_page".equals(getPageType());
	}
	
	public boolean isTreatmentLoopPage() {
		return "gert_training_spkn_loop_page".equals(getPageType());
	}

	public boolean isTreatmentTargetQuestionPage() {
		return "greet_training_spoken_question_page".equals(getPageType())
				&& "true".equals(events.get(0).getParam("is_target"));
	}
	
	public boolean isDCTQuestionPage() {
		return "dc_show_item_page".equals(getPageType());
	}
	
	public boolean isGJTQuestionPage() {
		return "gt_show_item_page".equals(getPageType());
	}
	
	public boolean isTreatmentVideoPage() {
		return "greet_training_spoken_video_page".equals(getPageType());
	}

	public int getAttemptCount() {
		int attempts = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_SYSTEM_GIVE_CF.equals(e.act)){
				attempts++;
			}
		}
		
		return attempts;
	}
	
	public int getGJTAttemptCount() {
		int attempts = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_RATE_CORRECTNESS.equals(e.act)){
				attempts++;
			}
		}
		
		return attempts;
	}
	
	public int getDCTAttemptCount() {
		int attempts = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_START_RECORD.equals(e.act)){
				attempts++;
			}
		}
		
		return attempts;
	}
	
	public int getSavedAudioCount() {
		int savedAudioFiles = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_SYS_SAVE_AUDIO.equals(e.act)){
				savedAudioFiles++;
			}
		}
		
		return savedAudioFiles;
	}
	
	public int getTimeExpiredCount() {
		int attempts = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_SYS_TIME_EXPIRED.equals(e.act)){
				attempts++;
			}
		}
		
		return attempts;
	}
	
	public LogEvent getFirstDCTAnswer() {
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_START_RECORD.equals(e.act)){
				return e;
			}
		}
		
		return null;
	}
	
	public LogEvent getFirstGJTAnswer() {
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_RATE_CORRECTNESS.equals(e.act)){
				return e;
			}
		}
		
		return null;
	}
	
	public int getStartRecordButtonPressCount() {
		int attempts = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_START_RECORD.equals(e.act)){
				attempts++;
			}
		}
		
		return attempts;
	}
	
	public LogEvent getEarliestStartRecordButtonEvent() {
		LogEvent earliestStartRecordButtonEvent = null;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_START_RECORD.equals(e.act)
				&& (null == earliestStartRecordButtonEvent || e.timestamp < earliestStartRecordButtonEvent.timestamp)){
					earliestStartRecordButtonEvent = e;
			}
		}
		
		return earliestStartRecordButtonEvent;
	}
	
	public List<LogEvent> getStartRecordButtonEvents(){
		List<LogEvent> buttonEvents = new ArrayList<LogEvent>();
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_START_RECORD.equals(e.act)){
				buttonEvents.add(e);
			}
		}
		
		return buttonEvents;
	}
	
	public List<LogEvent> getStopRecordButtonEvents(){
		List<LogEvent> buttonEvents = new ArrayList<LogEvent>();
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_STOP_RECORD.equals(e.act)){
				buttonEvents.add(e);
			}
		}
		
		return buttonEvents;
	}
	
	public List<LogEvent> getSystemResponseEvents(){
		List<LogEvent> cfEvents = new ArrayList<LogEvent>();
		
		for(LogEvent e : events){
			if(LogEvent.ACT_SYSTEM_GIVE_CF.equals(e.act)){
				cfEvents.add(e);
			}
		}
		
		return cfEvents;
	}
	
	public List<LogEvent> getSavedAudioEvents(){
		List<LogEvent> cfEvents = new ArrayList<LogEvent>();
		
		for(LogEvent e : events){
			if(LogEvent.ACT_SYS_SAVE_AUDIO.equals(e.act)){
				cfEvents.add(e);
			}
		}
		
		return cfEvents;
	}
		
	public LogEvent getNextStopButtonEvent(LogEvent startButtonEvent){
		LogEvent earliestStopButtonEvent = null;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_STOP_RECORD.equals(e.act)
				&& startButtonEvent.timestamp < e.timestamp
				&& (null == earliestStopButtonEvent || e.timestamp < earliestStopButtonEvent.timestamp)){
				
				earliestStopButtonEvent = e;
			}
		}
		
		return earliestStopButtonEvent;
	}
	
	public LogEvent getNextStartButtonEvent(LogEvent cfEvent){
		LogEvent earliestStartButtonEvent = null;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_START_RECORD.equals(e.act)
				&& cfEvent.timestamp < e.timestamp
				&& (null == earliestStartButtonEvent || e.timestamp < earliestStartButtonEvent.timestamp)){
				
				earliestStartButtonEvent = e;
			}
		}
		
		return earliestStartButtonEvent;
	}
	
	public LogEvent getNextSystemResponseEvent(LogEvent e){
		for(int i = events.indexOf(e) + 1; i < events.size();i++){
			if(LogEvent.ACT_SYSTEM_GIVE_CF.equals(e.act)){
				return events.get(i);
			}
		}
		
		return null;
	}
	
	public LogEvent getEarliestSystemResponseEvent(){
		LogEvent earliestSystemResponseEvent = null;
		
		for(int i = 0; i < events.size();i++){
			LogEvent e = events.get(i);
			
			if(LogEvent.ACT_SYSTEM_GIVE_CF.equals(e.act)
					&& (null == earliestSystemResponseEvent || e.timestamp < earliestSystemResponseEvent.timestamp)){
				earliestSystemResponseEvent = events.get(i);
			}
		}
		
		return earliestSystemResponseEvent;
	}
	
	public LogEvent getEarliestSystemResponseEvent(LogEvent event){
		LogEvent earliestSystemResponseEvent = null;
		
		for(int i = 0; i < events.size();i++){
			LogEvent e = events.get(i);
			
			if(LogEvent.ACT_SYSTEM_GIVE_CF.equals(e.act)
					&& event.timestamp < e.timestamp
					&& (null == earliestSystemResponseEvent || e.timestamp < earliestSystemResponseEvent.timestamp)){
				earliestSystemResponseEvent = events.get(i);
			}
		}
		
		return earliestSystemResponseEvent;
	}
	
	public LogEvent getLatestSaveAudioEvent(){
		LogEvent latestSaveAudioEvent = null;
		
		for(LogEvent e : getSavedAudioEvents()){
			if(null == latestSaveAudioEvent || e.timestamp > latestSaveAudioEvent.timestamp){
				latestSaveAudioEvent = e;
			}
		}
		
		return latestSaveAudioEvent;
	}
	
	public LogEvent getLatestSystemResponseEvent(){
		LogEvent latestSystemResponseEvent = null;
		
		for(LogEvent e : getSystemResponseEvents()){
			if(null == latestSystemResponseEvent || e.timestamp > latestSystemResponseEvent.timestamp){
				latestSystemResponseEvent = e;
			}
		}
		
		return latestSystemResponseEvent;
	}
	
	public LogEvent getEarliestSkipEvent(LogEvent systemResponseEvent) {
		LogEvent earliestSkipEvent = null;
		
		if(null != systemResponseEvent){
			for(int i = 0; i < events.size();i++){
				LogEvent e = events.get(i);
				
				if(LogEvent.ACT_USER_SKIP_QUESTION.equals(e.act)
						&& systemResponseEvent.timestamp < e.timestamp
						&& (null == earliestSkipEvent || e.timestamp < earliestSkipEvent.timestamp)){
					earliestSkipEvent = events.get(i);
				}
			}
		}
		else{
			System.out.println("Warning: systemResponseEvent reference event for looking for earliest skip event was null. Returning null for getEarliestSkipEvent()");
			System.out.println("For pageCollection with events: ");
			for(LogEvent e : events){
				System.out.println("\t" + e.act + "\t" + e.timestamp);
			}
		}
		
		return earliestSkipEvent;
	}
	
	public int getStopRecordButtonPressCount() {
		int attempts = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_STOP_RECORD.equals(e.act)){
				attempts++;
			}
		}
		
		return attempts;
	}
	
	public boolean containsCNTRLGroupFeedbackEvent(){		
		for(LogEvent e : events){
			if(LogEvent.ACT_SYSTEM_GIVE_CF.equals(e.act)
					&& "CNTRL_FEEDBACK_MESG".equals(e.getParam("type_of_feedback"))){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsSkipEvent(){
		return getSkipCount() > 0;
	}
	
	public int getSkipCount() {
		int skips = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACT_USER_SKIP_QUESTION.equals(e.act)){
				skips++;
			}
		}
		
		return skips;
	}

	public int getWRSEQCount() {
		return countEvents("FEEDBACK_WR_SEQ", paramNameForFeedbackEvent);
	}

	public int getDNUCount() {
		return countEvents("FEEDBACK_DNU", paramNameForFeedbackEvent);
	}
	
	public int countUserEvents(){
		int count = 0;
		
		for(LogEvent e : events){
			if(LogEvent.ACTOR_USER.equals(e.actor)){
				count++;
			}
		}
		
		return count;
	}
	
	protected int countEvents(String act){
		int count = 0;
		
		for(LogEvent e : events){
			count++;
		}
		
		return count;
	}
	
	protected int countEvents(String act, String param){
		int count = 0;
		
		for(LogEvent e : events){
			if(act.equals(e.getParam(param))){
				count++;
			}
		}
		
		return count;
	}

	public long getLargestPeriodBetweenAdjacentUserEvents() {
		LogEvent curEvent = getEarliestUserEvent();
		LogEvent nextEvent = getEarliestUserEventThatFollows(curEvent);
		
		long difference = 0;
		
		while(null != nextEvent){
			if(nextEvent != null && difference < nextEvent.timestamp - curEvent.timestamp){
				difference = nextEvent.timestamp - curEvent.timestamp;
			}
			
			curEvent = nextEvent;
			nextEvent =  getEarliestUserEventThatFollows(curEvent);
		}
		
		return difference;
	}
}