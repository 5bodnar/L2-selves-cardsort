package test.ru.fasop.log;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.PageViewEventCollection;

public class PageViewEventCollectionTest extends TestCase {

	public void testConstructor() {
		PageViewEventCollection window = new PageViewEventCollection();

		assertEquals(-1, window.getStartTime());
		assertEquals(0, window.events.size());
	}

	public void testGetStartTime() {
		PageViewEventCollection window = new PageViewEventCollection();

		assertEquals(0, window.events.size());

		LogEvent event = new LogEvent();
		event.actor = LogEvent.ACTOR_USER;
		event.timestamp = (long) 1005;

		window.addEvent(event);
		
		assertEquals(1005, window.getStartTime());
	}
	
	public void testGetDurationUntilLastPageEvent() {
		PageViewEventCollection window = new PageViewEventCollection();

		assertEquals(0, window.events.size());

		LogEvent event = new LogEvent();
		event.timestamp = (long) 1005;
		event.actor = LogEvent.ACTOR_USER;

		window.addEvent(event);
		
		assertEquals(-1, window.getDurationUntilLastPageEvent());
		
		event = new LogEvent();
		event.timestamp = (long) 2005;
		event.actor = LogEvent.ACTOR_USER;

		window.addEvent(event);
		
		assertEquals(1000, window.getDurationUntilLastPageEvent());
	}
	
	public void testGetDuration() {
		PageViewEventCollection window = new PageViewEventCollection();
		
		assertEquals(-1, window.getDuration());
		
		LogEvent event = new LogEvent();
		event.timestamp = (long) 1005;
		event.actor = LogEvent.ACTOR_USER;

		window.addEvent(event);
		
		assertEquals(-1, window.getDuration());
		
		window.duration = 5000 - window.getStartTime();
		
		assertEquals(3995, window.getDuration());
	}
	
	public void testGetLargetPeriodBetweenEvents(){
		PageViewEventCollection window = new PageViewEventCollection();
		
    	window.addEvent(new LogEvent(new Long(2),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGIN,
				new ArrayList<LogEventParam>()));
    	
    	window.addEvent(new LogEvent(new Long(3),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	window.addEvent(new LogEvent(new Long(5),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	window.addEvent(new LogEvent(new Long(105),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	window.addEvent(new LogEvent(new Long(107),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_PRESS_BUTTON,
				new ArrayList<LogEventParam>()));
    	
    	window.addEvent(new LogEvent(new Long(112),
				LogEvent.ACTOR_USER,
				LogEvent.ACT_USER_LOGOUT,
				new ArrayList<LogEventParam>()));
    	    	
    	assertEquals(100, window.getLargestPeriodBetweenAdjacentUserEvents());
    }
	
	public void testGetPageType(){
		PageViewEventCollection window = new PageViewEventCollection();
		
		try{
			window.getPageType();
			fail("Should have thrown exception because no events in collection");
		}
		catch(Exception e){}
		
		LogEvent event = new LogEvent();
		event.timestamp = (long) 1005;
		
		List<LogEventParam> params = LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
									+ "questionId : false;"
									+ "questionSetId : 453");
    	
		window.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										params));
		
		assertEquals("greet_training_spoken_question_page", window.getPageType());
		
		window = new PageViewEventCollection();
		
		event = new LogEvent();
		event.timestamp = (long) 1005;
		
		params = LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
									+ "questionId : false;"
									+ "questionSetId : 453");
    	
		window.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_SYS_SAVE_AUDIO,
										params));
		
		try{
			window.getPageType();
			fail("Should have thrown exception because first event is not of type viewpage");
		}
		catch(Exception e){}
	}
	
	public void testIsSortingPage() {
		LogEvent event = new LogEvent();
		event.timestamp = (long) 1005;
		
		List<LogEventParam> params = LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
									+ "questionId : false;"
									+ "questionSetId : 453");
    	
		PageViewEventCollection eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										params));
		
		assertEquals(false, eCollection.isSortingPage());
		
		event = new LogEvent();
		event.timestamp = (long) 1005;
		
		params = LogEventParam.parse("pageId : pselves_sort_free_desirable_pselves ;"
									+ "questionId : false;"
									+ "questionSetId : 453");
    	
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										params));
		
		assertEquals(true, eCollection.isSortingPage());

		event = new LogEvent();
		event.timestamp = (long) 1005;
		
		params = LogEventParam.parse("pageId : pselves_sort_free_undesirable_pselves ;"
									+ "questionId : false;"
									+ "questionSetId : 453");
    	
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										params));
		
		assertEquals(true, eCollection.isSortingPage());
		
		event = new LogEvent();
		event.timestamp = (long) 1005;
		
		params = LogEventParam.parse("pageId : sort_self_efficacy_desirable_pselves ;"
									+ "questionId : false;"
									+ "questionSetId : 453");
    	
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										params));
		
		assertEquals(true, eCollection.isSortingPage());
		
		params = LogEventParam.parse("pageId : sort_self_efficacy_undesirable_pselves ;"
				+ "questionId : false;"
				+ "questionSetId : 453");

		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
							LogEvent.ACTOR_USER,
							LogEvent.ACT_USER_VIEW_PAGE,
							params));
		
		assertEquals(true, eCollection.isSortingPage());
	}
	
	public void testGetSortingAreaHeight() {
		
//		2610204, user, user_login, 9:35:49 PM]
//
//			    user = hans.aarns@tiscali.nl
//			    activity = possible_selves_mapping_interview
//			    hopes_self_concept_instance_id = 2610201
//			    fears_self_concept_instance_id = 2610202 
		
		// add view page event for sorting page
		//10476, user, user_view_page, 9:56:40 PM]

//			    browser_type = mozilla/5.0 (windows nt 6.1; wow64; rv:10.0.2) gecko/20100101 firefox/10.0.2
//			    pageId = pselves_sort_free_desirable_pselves 
//
//			[2610483, user, drop_pselve_card_in_sorting_list, 9:58:51 PM]
//
//			    pself_div_top_abs = 183.93333435058594
//			    card_pselve_id = 2610439
//			    drop_div_top_abs = 65
//			    drop_div_height = 2122
//			    pself_div_top_from_zero = 118.93333435058594
//			    card_text = I understand Dutch people and their way of life 
		
		PageViewEventCollection eCollection = new PageViewEventCollection();

		try {
			assertEquals(null, eCollection.getSortingAreaHeight());
			fail("getPageType should have thrown exception, since there are no events yet");
		}
		catch(Exception e){}
		

		LogEvent event = new LogEvent();
		event.timestamp = (long) 1005;
		
		List<LogEventParam> params = LogEventParam.parse("pageId : pselves_sort_free_desirable_pselves ;"
									+ "questionId : false;"
									+ "questionSetId : 453");
    	
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										params));
		
		assertEquals(null, eCollection.getSortingAreaHeight());
		
		event = new LogEvent();
		event.timestamp = (long) 1006;
		
		params = LogEventParam.parse("drop_div_height : 2122;");
    	
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										"drop_pselve_card_in_sorting_list",
										params));
		
		assertEquals(true, eCollection.isSortingPage());
		
	    assertEquals(new Double(2122), eCollection.getSortingAreaHeight());
	}
	
	public void testIsTreatmentPage() {
		PageViewEventCollection eCollection = new PageViewEventCollection();
		
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										LogEventParam.parse("pageId : greet_training_spoken_video_page ;"
															+ "questionId : false;"
															+ "questionSetId : 1220")));
		
		assertEquals(false, eCollection.isTreatmentQuestionPage());
		
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
															+ "questionId : false;"
															+ "questionSetId : 1220")));
		
		assertEquals(true, eCollection.isTreatmentQuestionPage());
		assertEquals(false, eCollection.isTreatmentTargetQuestionPage());
		
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
															+ "questionId : false;"
															+ "questionSetId : 1220;"
															+ "is_target : true")));
		
		assertEquals(true, eCollection.isTreatmentTargetQuestionPage());
	}
	
	public void testGetAttemptCount() {
		PageViewEventCollection eCollection = new PageViewEventCollection();
		
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
															+ "questionId : false;"
															+ "questionSetId : 1220")));
		
		assertEquals(0, eCollection.getAttemptCount());
		
		eCollection.addEvent(new LogEvent(new Long(0),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_GIVE_CF,
				LogEventParam.parse("question_id : 123;"
									+ "questionSetId : 1220")));
		
		assertEquals(1, eCollection.getAttemptCount());
		
		eCollection.addEvent(new LogEvent(new Long(0),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_GIVE_CF,
				LogEventParam.parse("question_id : 123;"
									+ "questionSetId : 1220")));
		
		eCollection.addEvent(new LogEvent(new Long(0),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_GIVE_CF,
				LogEventParam.parse("question_id : 123;"
									+ "questionSetId : 1220")));
		
		eCollection.addEvent(new LogEvent(new Long(0),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_GIVE_CF,
				LogEventParam.parse("question_id : 123;"
									+ "questionSetId : 1220")));
		
		assertEquals(4, eCollection.getAttemptCount());
	}
	
	public void testGetDNUAndWRSEQCount() {
		PageViewEventCollection eCollection = new PageViewEventCollection();
		
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
															+ "questionId : false;"
															+ "questionSetId : 1220")));
		
		assertEquals(0, eCollection.getWRSEQCount());
		
		eCollection.addEvent(new LogEvent(new Long(0),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_GIVE_CF,
				LogEventParam.parse("question_id : 123;"
									+ "type_of_feedback : FEEDBACK_DNU;")));
		
		assertEquals(1, eCollection.getDNUCount());
		assertEquals(0, eCollection.getWRSEQCount());
		
		eCollection.addEvent(new LogEvent(new Long(0),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_GIVE_CF,
				LogEventParam.parse("question_id : 123;"
									+ "type_of_feedback : FEEDBACK_WR_SEQ;")));
		
		eCollection.addEvent(new LogEvent(new Long(0),
				LogEvent.ACTOR_SYSTEM,
				LogEvent.ACT_SYSTEM_GIVE_CF,
				LogEventParam.parse("question_id : 123;"
									+ "type_of_feedback : FEEDBACK_WR_SEQ;")));
		
		assertEquals(1, eCollection.getDNUCount());
		assertEquals(2, eCollection.getWRSEQCount());
	}
	
	public void testGetSkipCount() {
		PageViewEventCollection eCollection = new PageViewEventCollection();
		
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										LogEventParam.parse("pageId : greet_training_spoken_video_page ;"
															+ "questionId : false;"
															+ "questionSetId : 1220")));
		
		assertEquals(false, eCollection.isTreatmentQuestionPage());
		
		eCollection = new PageViewEventCollection();
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_VIEW_PAGE,
										LogEventParam.parse("pageId : greet_training_spoken_question_page ;"
															+ "questionId : false;"
															+ "questionSetId : 1220")));
		
		eCollection.addEvent(new LogEvent(new Long(0),
											LogEvent.ACTOR_SYSTEM,
											LogEvent.ACT_SYSTEM_GIVE_CF,
											LogEventParam.parse("question_id : 123;"
																+ "type_of_feedback : FEEDBACK_DNU;")));
		
		assertEquals(0, eCollection.getSkipCount());
		
		eCollection.addEvent(new LogEvent(new Long(0),
										LogEvent.ACTOR_USER,
										LogEvent.ACT_USER_SKIP_QUESTION,
										LogEventParam.parse("question_id : 123;"
															+ "type_of_feedback : FEEDBACK_DNU;")));

		assertEquals(1, eCollection.getSkipCount());
	}
	
//		[2393600, user, user_login, 2:42:32 PM]
//
//			    group_id = 2342678
//			    group_name = Exp. Nov2012 - Nijmegen - HighEd -EXP-A-s2
//			    tasklist_id = 2120688
//			    tasklist_label = Nov2012-GREET-EXP-A-S2
//			    task_id = 2263557
//			    task_label = Exp. 2012 Nov HTML GREET - EXP - S2c
//			    user = vendula.machova@centrum.cz
//			    question_set_type = TREAT
//			    question_set_desc = Tom cycles to butcher; Melvin is still reading the paper, it's 1026 am; Karin is helping the old woman in the shop; sells her the last 2 paprikas;
//			    question_set_id = 1220 
//			[2393614, user, user_view_page, 2:43:27 PM]
//
//			    video_file = NB-GERT-3-4-1.flv
//			    pageId = greet_training_spoken_video_page
//			    questionSetId = 1220 
//			[2393639, user, user_view_page, 2:43:39 PM]
//
//			    opt_blocks = []
//			    questionId = 1221
//			    prefix =
//			    prompt = Waar is Tom heen gefietst?
//			    req_blocks = [naar, gefietst, Tom is, de slager]
//			    pageId = greet_training_spoken_question_page
//			    is_target = false
//			    questionSetId = 1220 
//			[2393651, user, start_record, 2:43:44 PM]
//
//			    questionId = 1221
//			    onScreenHintString = ? | ? | ? | ?
//			    pageId = greet_training_spoken_question_page
//			    questionSetId = 1220 
//			[2393661, user, stop_record, 2:43:49 PM]
//
//			    questionId = 1221
//			    onScreenHintString = ? | ? | ? | ?
//			    pageId = greet_training_spoken_question_page
//			    questionSetId = 1220 
//			[2393668, system, save_audio, 2:42:56 PM]
//
//			    username = vendula.machova@centrum.cz
//			    questionSetId = 1220
//			    question_id = 1221
//			    questionContent = Waar is Tom heen gefietst?
//			    reqBlocks = [naar| gefietst| Tom is| de slager]
//			    filename = speech_recordings/vendula.machova@centrum.cz/practice/vendula.machova@centrum.cz_practice_1221_20121107_154344.wav.new.wav 
//			[2393676, system, give_cf, 2:43:51 PM]
//
//			    feedback_mesg = That's right! good job.
//			    type_of_feedback = FEEDBACK_OK
//			    recogResult = tom_is_naar_de_slager_gefietst
//			    questionId = 1221
//			    onScreenHintsString = ? | ? | ? | ?
//			    numTries = 1
//			    pageId = greet_training_spoken_question_page 

//			[2393685, user, user_view_page, 2:43:52 PM]
//
//			    opt_blocks = []
//			    questionId = 1223
//			    prefix = Bij
//			    prompt = Is lamsvlees duur bij de slager?
//			    req_blocks = [ is, duur, lamsvlees, de slager]
//			    pageId = greet_training_spoken_question_page
//			    is_target = true
//			    questionSetId = 1220 
	
//			[2393741, user, start_record, 2:44:11 PM]
//
//			    questionId = 1223
//			    onScreenHintString = ? | ? | ? | ?
//			    pageId = greet_training_spoken_question_page
//			    questionSetId = 1220 
//
//			[2393754, user, stop_record, 2:44:17 PM]
//
//			    questionId = 1223
//			    onScreenHintString = ? | ? | ? | ?
//			    pageId = greet_training_spoken_question_page
//			    questionSetId = 1220 
//
//			[2393762, system, save_audio, 2:43:24 PM]
//
//			    username = vendula.machova@centrum.cz
//			    questionSetId = 1220
//			    question_id = 1223
//			    questionContent = Is lamsvlees duur bij de slager?
//			    questionPrefix = Bij
//			    reqBlocks = [ is| duur| lamsvlees| de slager]
//			    filename = speech_recordings/vendula.machova@centrum.cz/practice/vendula.machova@centrum.cz_practice_1223_20121107_154411.wav.new.wav 
//
//			[2393770, system, give_cf, 2:44:19 PM]
//
//			    feedback_mesg = Dat klopt niet.
//
//			    Probeer het nog eens.
//			    type_of_feedback = FEEDBACK_WR_SEQ
//			    recogResult = bij_de_slager_lamsvlees_is_duur
//			    questionId = 1223
//			    onScreenHintsString = ? | ? | ? | ?
//			    numTries = 1
//			    pageId = greet_training_spoken_question_page 
//
//			[2393804, user, start_record, 2:44:26 PM]
//
//			    questionId = 1223
//			    onScreenHintString = ? | ? | ? | ?
//			    pageId = greet_training_spoken_question_page
//			    questionSetId = 1220 
//
//			[2393809, user, stop_record, 2:44:31 PM]
//
//			    questionId = 1223
//			    onScreenHintString = ? | ? | ? | ?
//			    pageId = greet_training_spoken_question_page
//			    questionSetId = 1220 
//
//			[2393817, system, save_audio, 2:43:38 PM]
//
//			    username = vendula.machova@centrum.cz
//			    questionSetId = 1220
//			    question_id = 1223
//			    questionContent = Is lamsvlees duur bij de slager?
//			    questionPrefix = Bij
//			    reqBlocks = [ is| duur| lamsvlees| de slager]
//			    filename = speech_recordings/vendula.machova@centrum.cz/practice/vendula.machova@centrum.cz_practice_1223_20121107_154426.wav.new.wav 
//
//			[2393825, system, give_cf, 2:44:33 PM]
//
//			    feedback_mesg = That's right! good job.
//			    type_of_feedback = FEEDBACK_OK
//			    recogResult = bij_de_slager_is_lamsvlees_duur
//			    questionId = 1223
//			    onScreenHintsString = ? | ? | ? | ?
//			    numTries = 2
//			    pageId = greet_training_spoken_question_page 
//
}
