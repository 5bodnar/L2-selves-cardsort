package ru.fasop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class QuestionnaireItem {
	public static final int TYPE_LIKERT = 0;
	public static final int TYPE_OPEN_LONG = 1;
	public static final int TYPE_OPEN_SHORT = 2;
	public static final int TYPE_MULTICHOICE = 3;
	public static final int TYPE_CHECKLIST = 4;
	public static final int TYPE_POSSIBLE_SELF = 5;
	public static final int TYPE_SEMANTIC_DIFFERENTIAL = 6;
	
	public static final List<String> MINI_MOTIV_ATT_QUESTION_IDS = new ArrayList<String>(){{add("2318052"); add("2318086");}};
	public static final List<String> MINI_MOTIV_MOT_QUESTION_IDS = new ArrayList<String>(){{add("2318063"); add("2318097");}};
	public static final List<String> MINI_MOTIV_CONF_QUESTION_IDS = new ArrayList<String>(){{add("2318074"); add("2318108");}};
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public Long parentId;
	public Integer type;
	public String category;
	public String content;
	public String contentDutch;
	public String comment;
	public String imgUrl;
	public String imgUrl2;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<GertString> mcitems;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public List<GertString> dutchMcitems;
	
	public QuestionnaireItem(){}
	
	public QuestionnaireItem(Long parentId, int type, String category, String content, String comment) {
		this.parentId = parentId;
		this.type = new Integer(type);
		this.category = category;
		this.content = content;
		this.comment = comment;
		this.mcitems = new ArrayList<GertString>();
		this.dutchMcitems = new ArrayList<GertString>();
	}
	
	public void addMultipleChoiceItems(String[] strings, Integer[] types) {
		mcitems = new ArrayList<GertString>();
		for(int i = 0; i < strings.length; i++) {
			mcitems.add(new GertString(strings[i], types[i]));
		}
	}
	
	public String getTypeAsString(){
		switch(type){
			case QuestionnaireItem.TYPE_LIKERT:
				return "likert";
			case QuestionnaireItem.TYPE_OPEN_SHORT:
				return "open_short";
			case QuestionnaireItem.TYPE_OPEN_LONG:
				return "open_long";
			case QuestionnaireItem.TYPE_MULTICHOICE:
				return "multiple_choice";
			case QuestionnaireItem.TYPE_CHECKLIST:
				return "checklist";
			case QuestionnaireItem.TYPE_POSSIBLE_SELF:
				return "possible_self";
			default:
				return "unknown type";
		}
	}
	
	public String getMcItemsAsString(){
		StringBuffer mcString = new StringBuffer();
		
		if(null != mcitems){
			for(int i = 0; i < mcitems.size(); i++) {
				GertString mcItem = mcitems.get(i);
				mcString.append(mcItem.content);
				mcString.append("|");
				mcString.append(getOptionType(mcItem));
				mcString.append(";");
			}
		}
		
		return mcString.toString();
	}
	
	public String getDutchMcItemsAsString(){
		StringBuffer mcString = new StringBuffer();
		
		if(null != dutchMcitems){
			for(int i = 0; i < dutchMcitems.size(); i++) {
				GertString mcItem = dutchMcitems.get(i);
				mcString.append(mcItem.content);
				mcString.append("|");
				mcString.append(getOptionType(mcItem));
				mcString.append(";");
			}
		}
		
		return mcString.toString();
	}

	private String getOptionType(GertString mcItem) {
		if(GertString.TYPE_BLANK == mcItem.getType()){
			return "b";
		}
		else if(GertString.TYPE_PROVIDED == mcItem.getType()){
			return "u";
		}
		else{
			return "-1";
		}
	}

	public String toSessionLogId() {
		return content.trim().replaceAll("[^A-Za-z0-9]", "_").toLowerCase() + id.toString();
	}
	
	public QuestionnaireItem clone() {
		QuestionnaireItem qi = new QuestionnaireItem();
		
		qi.type = type;
		qi.category = category;
		qi.content = content;
		qi.contentDutch = contentDutch;
		qi.comment = comment;
		qi.imgUrl = imgUrl;
		qi.imgUrl2= imgUrl2;
		
		qi.mcitems = new ArrayList<GertString>();
		
		for(GertString item : mcitems){
			qi.mcitems.add(item.clone());
		}
		
		qi.dutchMcitems = new ArrayList<GertString>();
		
		for(GertString item : dutchMcitems){
			qi.dutchMcitems.add(item.clone());
		}
		
		return qi;
	}
}
