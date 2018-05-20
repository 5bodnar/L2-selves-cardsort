package ru.fasop.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import ru.fasop.GertDatabaseConnection;
import ru.fasop.TestSubject;

@Entity
public class Session {
																			
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	@OneToOne(cascade=CascadeType.REFRESH)
	@OrderBy("id")
	public TestSubject subj;
	public Long timestamp;
	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("id")
	public List<LogEvent> events = new ArrayList<LogEvent>();
	
	public static List<String> PRACTICE_DESCRIPTORS = Arrays.asList("time_videos",
																	"time_questions",
																	"time_reformulating",
																	"time_prepping",
																	"time_recording",
																	"time_waiting",
																	"time_considering_skipping",
																	"time_prepping_attempt_one", "time_prepping_attempt_two_and_later",
																	"time_recording_attempt_one", "time_recording_attempt_two_and_later",
																	"loop_time",
																	
																	"time_per_page",
																	"time_attempting_per_page",
																	"mean_time_to_complete_attempt_one", "mean_time_reformulating",
																	"mean_time_before_attempt_one", "tfirstrecord",
																	"mean_time_prepping_for_attempt_one", "mean_time_prepping_for_attempt_two_and_later",
																	"mean_time_waiting_per_question_page",
																	"videos", "questions", "errors", "OK", "OK_pcnt", "WRSEQ", "WRSEQ_pcnt", "DNU",
																	"first_try_oks", "first_try_oks_pcnt",
																	"first_try_oks_first_pass_only", "first_try_oks_pcnt_first_pass_only",
																	"reformulations",
																	"skips", "skips_pcnt", "attempts",
																	"redos", "redos_pcnt",
																	"attempts_per_question",
																	"long_pages");
																	
																	/*"max_first_try_OK_stretch",
																	"mean_first_try_OK_stretch","max_difficult_question_stretch",
																	"mean_difficult_question_stretch");
																	*/
	
	public Session(){}
	
	public Session(TestSubject subj, Long timestamp) {
		this.subj = subj;
		this.timestamp = timestamp;
	}
	
	public void save(EntityManager mgr){
		mgr.getTransaction().begin();
		mgr.persist(this);
		mgr.getTransaction().commit();
	}

	public long getDurationInMillis() {
		//LogEvent earliestUserEvent = getEarliestUserEvent();
		LogEvent earliestUserEvent = getEarliestUserEventWhichIsNotLogin();
		LogEvent latestUserEvent = getLatestUserEvent();
		
		if(2 > events.size()){
			return 0;
		}
		else if(null == earliestUserEvent || null == latestUserEvent){
			return 0;
		}
		else{
			return latestUserEvent.timestamp - earliestUserEvent.timestamp;
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
	
	public LogEvent getEarliestUserEventWhichIsNotLogin() {
		LogEvent e = null;
		
		for(int i = 0; i < events.size(); i++){
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& ! LogEvent.ACT_USER_LOGIN.equals(events.get(i).act)
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
					// sometimes logins are registered after the first page view event
					// due to the server being in a different time zone.
					// We should ignore those.
					&& ! LogEvent.ACT_USER_LOGIN.equals(events.get(i).act)
					&& (null == e || events.get(i).timestamp > e.timestamp)){
				e = events.get(i);
			}
		}
		
		return e;
	}
	
	public LogEvent getLoginEvent(){
		for(int i = 0; i < events.size(); i++){
			
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& LogEvent.ACT_USER_LOGIN.equals(events.get(i).act)){
				return events.get(i);
			}
		}
		
		return null;
	}
	
	public List<String> getUniquePageViewIds(){
		List<String> pageIds = new ArrayList<String>();
		
		for(int i = 0; i < events.size(); i++){
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& LogEvent.ACT_USER_VIEW_PAGE.equals(events.get(i).act)){
				
				String pageId = events.get(i).getParam(LogEvent.PARAM_PAGE_ID);
				
				if(! pageIds.contains(pageId)){
					pageIds.add(pageId);
				}
			}
		}
		
		return pageIds;
	}
	
	public List<String> getPageVisitSequence(){
		List<String> pageIds = new ArrayList<String>();
		
		for(int i = 0; i < events.size(); i++){
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& LogEvent.ACT_USER_VIEW_PAGE.equals(events.get(i).act)){
				
				pageIds.add(events.get(i).getParam(LogEvent.PARAM_PAGE_ID));
			}
		}
		
		return pageIds;
	}
	
	public int countPageViewEvents(){
		int pageViewCount = 0;
		
		for(int i = 0; i < events.size(); i++){
			
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& LogEvent.ACT_USER_VIEW_PAGE.equals(events.get(i).act)){
				pageViewCount++;
			}
		}
		
		return pageViewCount;
	}
	
	public int countSavedAudioEvents(){
		int count = 0;
		
		for(int i = 0; i < events.size(); i++){
			
			if(LogEvent.ACTOR_SYSTEM.equals(events.get(i).actor)
					&& LogEvent.ACT_SYS_SAVE_AUDIO.equals(events.get(i).act)){
				count++;
			}
		}
		
		return count;
	}
	
	public double calcMeanPageViewVelocity(){
		return calcMeanPageViewVelocity() / (getDurationInMillis() / 1000 * 60);
	}
	
	public static List<Session> getPossibleSelvesSessions(GertDatabaseConnection con, TestSubject subj) {
		List<Session> pselvesSessions = new ArrayList<Session>();
		
		List<Session> sessions = (List<Session>) con.queryList("select e from Session e where e.subj.id = " + subj.id + " order by e.id desc");
		
		for(Session s : sessions){
			if(null != s.getLoginEvent() && 
					null != s.getLoginEvent().getParam("activity")
					&& -1 != s.getLoginEvent().getParam("activity").indexOf("possible_selves_mapping_interview")){
				
				pselvesSessions.add(s);
			}
		}
		
		return pselvesSessions;
	}
	
	public static List<Session> getPostQuestionnaireSessions(GertDatabaseConnection con, TestSubject subj) {
		List<Session> postQuestionnaireSessions = new ArrayList<Session>();
		
		List<Session> sessions = (List<Session>) con.queryList("select e from Session e where e.subj.id = " + subj.id + " order by e.id");
		
		for(Session s : sessions){
			if(null != s.getLoginEvent()
					&& "QNNAIRE".equals(s.getLoginEvent().getParam("question_set_type"))
					&& -1 != s.getLoginEvent().getParam("question_set_desc").indexOf("Post-questionnaire")){
				
				postQuestionnaireSessions.add(s);
			}
		}
		
		return postQuestionnaireSessions;
	}
	
	public int getMostRecentQuestionSetIdAttempted() {
		for(int i = events.size() - 1; i >= 0 ; i--){
			if(null != events.get(i).getParam("question_set_id")){
				return  Integer.parseInt(events.get(i).getParam("question_set_id"));
			}
			else if(null != events.get(i).getParam("questionSetId")){
				return Integer.parseInt(events.get(i).getParam("questionSetId"));
			}
		}
		
		return -1;
	}

	public String getLastNonTimeExpiredPageViewed() {
		for(int i = events.size() - 1; i >= 0; i--){
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& LogEvent.ACT_USER_VIEW_PAGE.equals(events.get(i).act)){
				return events.get(i).getParam("pageId");
			}
		}
		
		return null;
	}

	public String getMostRecentQuestionIdViewed() {
		for(int i = events.size() - 1; i >= 0 ; i--){
						
			if(LogEvent.ACTOR_USER.equals(events.get(i).actor)
					&& LogEvent.ACT_USER_VIEW_PAGE.equals(events.get(i).act)
					&& "greet_training_spoken_question_page".equals(events.get(i).getParam("pageId"))){

					System.out.println(events.get(i));
				
					if(null != events.get(i).getParam("question_id")){
						return  events.get(i).getParam("question_id");
					}
					else if(null != events.get(i).getParam("questionId")){
						return events.get(i).getParam("questionId");
					}
			}
		}
		
		return null;
	}
	
	public List<LogEvent> getSavedAudioEvents(){
		List<LogEvent> savedAudioEvents = new ArrayList<LogEvent>();
		
		List<PageViewEventCollection> pages = getEventsAsPageCollections();
		
		for(int i = 0; i < pages.size(); i++){
			savedAudioEvents.addAll(pages.get(i).getSavedAudioEvents());
		}
		
		return savedAudioEvents;
	}
	
	public List<PageViewEventCollection> getEventsAsPageCollections() {
		List<PageViewEventCollection> pageEventCollection = new ArrayList<PageViewEventCollection>();
		
		SessionLogProcessor proc = new SessionLogProcessor(this);
		proc.seekLogin();
		proc.seekNextPageViewEvent();
		PageViewEventCollection eventCollection = proc.buildPageViewEventCollection();
		
		while(null != eventCollection){
			if(eventCollection.isValidCollection()) {
				pageEventCollection.add(eventCollection);
			}
			else {
				System.out.println("Warning: encountered invalid event collection for subject " + subj.id);
				System.out.println(" with in session id " + id);
				System.out.println("For pageView with events " + eventCollection.events);
			}
			
			 eventCollection = proc.buildPageViewEventCollection();
		}
		return pageEventCollection;
	}
	
	public static boolean hasActiveSession(GertDatabaseConnection con, TestSubject subj, int limitInSeconds) {
		List<Session> sessions = (List<Session>) con.queryList("select e from Session e where e.subj.id = " + subj.id + " order by e.id desc");
		
		if(sessions.size() > 0){
			return sessions.get(sessions.size() - 1).isActive(limitInSeconds);
		}
		else {
			return false;
		}
	}

	public boolean isActive(int limitInSeconds) {
		TimeZone tz = TimeZone.getTimeZone("Europe/Amsterdam");
		return Calendar.getInstance(tz).getTimeInMillis() - events.get(events.size() - 1).timestamp <= 1000 * limitInSeconds;
	}

	public String getFormattedTimestamp() {
		return new SimpleDateFormat("EEEEEEEE, MMMMMMMM dd, yyyy").format(timestamp);
	}
}
