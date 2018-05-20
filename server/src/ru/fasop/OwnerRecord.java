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

import ru.fasop.log.LogEvent;
import ru.fasop.log.LogEventParam;
import ru.fasop.log.Session;
import ru.fasop.motivation.L2PossibleSelf;
import ru.fasop.motivation.L2SelfConceptInstance;
import ru.fasop.motivation.L2SelfConstruct;
import ru.fasop.motivation.LanguageAndCulturePair;


@Entity
public class OwnerRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	Long objectId = null;
	
	@OneToMany(cascade=CascadeType.REFRESH)
	@OrderBy("id")
	public List<TestSubject> owners = new ArrayList<TestSubject>();
	
	public OwnerRecord(){
	}
	
	public OwnerRecord(Object object){
		this.objectId = getId(object);	
	}
	
	public static OwnerRecord getOwnerRecord(GertDatabaseConnection con, Object obj){
		return (OwnerRecord) con.querySingle("select e from OwnerRecord e where e.objectId = " + getId(obj));
	}
	
	public static Long getId(Object object){
		if (object instanceof QuestionnaireItem) {return ((QuestionnaireItem) object).id;
		} else if (object instanceof TestSubject) { return ((TestSubject) object).id;
		} else if (object instanceof TestSpot) { return ((TestSpot) object).id;
		} else if (object instanceof TimeSlot) { return ((TimeSlot) object).id;
		} else if (object instanceof TestDay) { return ((TestDay) object).id;
		} else if (object instanceof TestPeriod) { return ((TestPeriod) object).id;
		} else if (object instanceof LogEvent) { return ((LogEvent) object).id;
		} else if (object instanceof LogEventParam) { return ((LogEventParam) object).id;
		} else if (object instanceof Session) { return ((Session) object).id;
		} else if (object instanceof GertString) { return ((GertString) object).id;
		} else if (object instanceof OwnerRecord) { return ((OwnerRecord) object).id;
		} else if (object instanceof FASOPUserGroup) { return ((FASOPUserGroup) object).id;
		} else if (object instanceof FASOPUserTask) { return ((FASOPUserTask) object).id;
		} else if (object instanceof FASOPUserTaskList) { return ((FASOPUserTaskList) object).id;
		} else if (object instanceof L2SelfConceptInstance) { return ((L2SelfConceptInstance) object).id;
		} else if (object instanceof L2PossibleSelf) { return ((L2PossibleSelf) object).id;
		} else if (object instanceof L2SelfConstruct) { return ((L2SelfConstruct) object).id;
		} else if (object instanceof LanguageAndCulturePair) { return ((LanguageAndCulturePair) object).id;
		} else if (object instanceof GertKeyValuePair){  return ((GertKeyValuePair) object).id;
		} else {
			return null;
		}
	}
}
