package ru.fasop.log;


public class SessionLogProcessor {
	public LogEvent curEvent = null;
	
	public int eventIndex = 0;
	public Session session = null;
	
	public SessionLogProcessor(Session session) {
		this.session = session;
	}
	
	public void seekLogin() {
		while(eventIndex < session.events.size() && ! isPositionedAtLoginEvent()){
			nextEvent();
		}
	}
	
	public void seekNextPageViewEvent() {
		while(eventIndex < session.events.size() && ! isPositionedAtViewQuestionEvent()){
			nextEvent();
		}
	}

	private boolean isPositionedAtLoginEvent() {
		return null != curEvent && LogEvent.ACT_USER_LOGIN.equals(curEvent.act);
	}
	
	public PageViewEventCollection buildPageViewEventCollection() {
		if(null != curEvent){
			PageViewEventCollection eCol = getCollection(curEvent);
			
			eCol.addEvent(curEvent);
			
			nextEvent();
			
			while(null != curEvent && ! isPositionedAtViewQuestionEvent()) {
				eCol.addEvent(curEvent);
				nextEvent();
			}
			
			if(null != curEvent){
				eCol.duration = curEvent.timestamp - eCol.getStartTime();
			}
			
			return eCol;
		}
		else{
			return null;
		}
	}

	private boolean isPositionedAtViewQuestionEvent() {
		return null != curEvent && LogEvent.ACT_USER_VIEW_PAGE.equals(curEvent.act);
	}
	
	public PageViewEventCollection getCollection(LogEvent curEvent){
		if(isPossibleSelvesPage(curEvent)){
			return new PossibleSelvesPageViewEventCollection();
		}
		else {
			return new PageViewEventCollection();
		}
	}
	
	private boolean isPossibleSelvesPage(LogEvent curEvent) {
		return "pselves_inst_page_general_intro".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_choose_lang".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_inst_1_browse_eg_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_inst_1_try_drag_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_inst_2_try_drag_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_select_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_sort_free_desirable_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_sort_free_undesirable_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_self_efficacy_inst_page".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "sort_self_efficacy_desirable_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "sort_self_efficacy_undesirable_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_rate_likelihood_pselves".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "pselves_add_custom_opportunity_page".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "possible_selves_debrief_page".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_pself".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_intervention".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID))
				|| "add_custom_final".equals(curEvent.getParam(LogEvent.PARAM_PAGE_ID));
	}
	
	public void nextEvent() {
		if(eventIndex < session.events.size()){
			curEvent = session.events.get(eventIndex);
			eventIndex++;
		}
		else {
			curEvent = null;
		}
	}
}
