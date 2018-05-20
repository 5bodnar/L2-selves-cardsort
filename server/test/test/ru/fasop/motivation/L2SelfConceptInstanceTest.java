package test.ru.fasop.motivation;

import java.util.List;

import ru.fasop.TestSubject;
import ru.fasop.motivation.L2PossibleSelf;
import ru.fasop.motivation.L2SelfConceptInstance;
import ru.fasop.motivation.L2SelfConstruct;
import test.ru.fasop.GertContentTestCase;

public class L2SelfConceptInstanceTest extends GertContentTestCase {
	
	public void testBasicConstructor(){
		L2SelfConceptInstance self = new L2SelfConceptInstance();
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		self.addPossibleSelf(heavySelf);
	}
	
	public void testSetTestSubject(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		self.addPossibleSelf(heavySelf);
		
		assertEquals("Steve", self.testSubject.name);
	}
	
	public void testGetUniqueConstructs(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(heavySelf);
		
		List<L2SelfConstruct> uniqueCs = self.getUniqueConstructs();
		
		assertEquals(2, uniqueCs.size());
	}
	
	public void testGetL2PossibleSelvesByConstruct(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(heavySelf);
		
		List<L2PossibleSelf> pselves = self.getL2PossibleSelvesByConstruct(new L2SelfConstruct("Construct 2"));
		
		assertEquals(2, pselves.size());
	}
	
	public void testGetDesirednessByPSelf(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(8);
		manSelf.polarity = L2PossibleSelf.POLARITY_POS_OR_NEG;
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		con.create(manSelf);
		manSelf.parentId = manSelf.id;
		
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(46);
		likedSelf.polarity = L2PossibleSelf.POLARITY_POS;
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		con.create(likedSelf);
		likedSelf.parentId = likedSelf.id;
		
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.desiredness = new Double(2);
		heavySelf.polarity = L2PossibleSelf.POLARITY_NEG;
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		con.create(likedSelf);
		heavySelf.parentId = heavySelf.id;
		
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.desiredness = new Double(102);
		intelligentSelf.polarity = L2PossibleSelf.POLARITY_POS;
		intelligentSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		con.create(intelligentSelf);
		intelligentSelf.parentId = intelligentSelf.id;
		
		self.addPossibleSelf(intelligentSelf);
		
		assertTrue(-1 != manSelf.id);
		
		assertEquals(new Double(8), self.getDesirednessByPSelfParentId(manSelf.id));
		assertEquals(new Double(46), self.getDesirednessByPSelfParentId(likedSelf.id));
		
		assertEquals(new Double(0), self.getDesirednessByPSelfParentId(new Long(22)));
	}
	
	public void testNormalise(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(8);
		manSelf.efficacy = new Double(16);
		manSelf.polarity = L2PossibleSelf.POLARITY_POS_OR_NEG;
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		con.create(manSelf);
		manSelf.parentId = manSelf.id;
		
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(46);
		likedSelf.efficacy = new Double(56);
		likedSelf.polarity = L2PossibleSelf.POLARITY_POS;
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		con.create(likedSelf);
		likedSelf.parentId = likedSelf.id;
		
		self.addPossibleSelf(likedSelf);
		
		assertEquals(new Double(8), self.getDesirednessByPSelfParentId(manSelf.id));
		assertEquals(new Double(16), self.getEfficacyByPSelfParentId(manSelf.id));
		
		self.maxValDesiredness = new Double(100);
		self.maxValEfficacy =  new Double(100);
		
		Double newMaxValue = new Double(50);
		
		L2SelfConceptInstance normalisedSelf = self.getNormalisedInstance(newMaxValue);
		
		assertEquals(new Double(4), normalisedSelf.getDesirednessByPSelfParentId(manSelf.id));
		assertEquals(new Double(8), normalisedSelf.getEfficacyByPSelfParentId(manSelf.id));
		assertEquals(new Double(23), normalisedSelf.getDesirednessByPSelfParentId(likedSelf.id));
	}
	
	public void testGetDesirednessByConstruct(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(8);
		manSelf.polarity = L2PossibleSelf.POLARITY_POS_OR_NEG;
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(46);
		likedSelf.polarity = L2PossibleSelf.POLARITY_POS;
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.desiredness = new Double(2);
		heavySelf.polarity = L2PossibleSelf.POLARITY_NEG;
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.desiredness = new Double(102);
		intelligentSelf.polarity = L2PossibleSelf.POLARITY_POS;
		intelligentSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(intelligentSelf);
		
		assertEquals(new Double(54), self.getDesirednessByConstruct(new L2SelfConstruct("Construct 1")));
		assertEquals(new Double(104), self.getDesirednessByConstruct(new L2SelfConstruct("Construct 2")));
		
		assertEquals(new Double(8), self.getDesirednessByConstruct(new L2SelfConstruct("Construct 1"), L2PossibleSelf.POLARITY_NEG));
		//assertEquals(new Double(104), self.getDesirednessByConstruct(new L2SelfConstruct("Construct 2")));
	}
	
	public void testGetDesirednessByConstructIfDesiredNotSet(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(-1);
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		assertEquals(new Double(0), self.getDesirednessByConstruct(new L2SelfConstruct("Construct 1")));
	}
	
	public void testGetRelativeDesirednessByConstruct(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.relativeImportance = new Double(9);
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.relativeImportance = new Double(46);
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.relativeImportance = new Double(4);
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.relativeImportance = new Double(102);
		intelligentSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(intelligentSelf);
		
		assertEquals(new Double(55), self.getRelativeDesirednessByConstruct(new L2SelfConstruct("Construct 1")));
		assertEquals(new Double(106), self.getRelativeDesirednessByConstruct(new L2SelfConstruct("Construct 2")));
	}
	
	public void testGetRelativeDesirednessByConstructIfValueNotSet(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.relativeImportance = new Double(-1);
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		assertEquals(new Double(0), self.getRelativeDesirednessByConstruct(new L2SelfConstruct("Construct 1")));
	}
	
	public void testGetLikelihoodByConstruct(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.likelihood = new Double(500);
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.likelihood = new Double(60);
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.likelihood = new Double(20);
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.likelihood = new Double(2);
		intelligentSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(intelligentSelf);
		
		assertEquals(new Double(560), self.getLikelihoodByConstruct(new L2SelfConstruct("Construct 1")));
		assertEquals(new Double(22), self.getLikelihoodByConstruct(new L2SelfConstruct("Construct 2")));
	}
	
	public void testGetLikelihoodByConstructIfValueNotSet(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.likelihood = new Double(-1);
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		assertEquals(new Double(0), self.getLikelihoodByConstruct(new L2SelfConstruct("Construct 1")));
	}
	
	public void testCount(){
		//Hope count : How many hopes do they have?
		//Fear count : How many fears do they have?
		
		L2SelfConceptInstance sConcept = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		sConcept.addPossibleSelf(manSelf);
		
		assertEquals(1, sConcept.count());
		
		L2PossibleSelf handsomeSelf = new L2PossibleSelf("I am handsome");
		sConcept.addPossibleSelf(handsomeSelf);
		
		assertEquals(2, sConcept.count());
	}
	
	// getPSelvesHits(threshholdAsPercentage)
	// getConstructHits(thresholdAsPercentage)
	
	public void testGetPSelvesHits(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(100);
		manSelf.likelihood = new Double(500);
		manSelf.efficacy = new Double(33);
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(200);
		likedSelf.likelihood = new Double(60);
		likedSelf.efficacy = new Double(99);
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.desiredness = new Double(300);
		heavySelf.likelihood = new Double(20);
		heavySelf.efficacy = new Double(56);
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.desiredness = new Double(700);
		intelligentSelf.likelihood = new Double(2);
		intelligentSelf.efficacy = new Double(25);
		self.addPossibleSelf(intelligentSelf);
		
		double threshold = 300;
		List<L2PossibleSelf> hits = self.getPSelvesHits(threshold);
		
		assertEquals(2, hits.size());
		assertEquals(heavySelf.pSelfAsText, hits.get(0).pSelfAsText);
		assertEquals(intelligentSelf.pSelfAsText, hits.get(1).pSelfAsText);
	}
	
	public void testIsInterestedInPSelves(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(100);
		manSelf.likelihood = new Double(500);
		manSelf.efficacy = new Double(33);
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(200);
		likedSelf.likelihood = new Double(60);
		likedSelf.efficacy = new Double(99);
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.desiredness = new Double(300);
		heavySelf.likelihood = new Double(20);
		heavySelf.efficacy = new Double(56);
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.desiredness = new Double(700);
		intelligentSelf.likelihood = new Double(2);
		intelligentSelf.efficacy = new Double(25);
		self.addPossibleSelf(intelligentSelf);
		
		assertTrue(! self.isInterestedInPSelves(1000));
		assertTrue(! self.isInterestedInPSelves(701));
		assertTrue(self.isInterestedInPSelves(700));
	}
	
	public void testIsInterestedInWholeConstruct(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2SelfConstruct construct = new L2SelfConstruct("Construct 1");
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(200);
		likedSelf.likelihood = new Double(60);
		likedSelf.efficacy = new Double(99);
		likedSelf.originalPolarity = L2PossibleSelf.POLARITY_POS;
		likedSelf.associatedConstructs.add(construct);
		construct.relatedPossibleSelves.add(likedSelf);
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.desiredness = new Double(700);
		intelligentSelf.likelihood = new Double(2);
		intelligentSelf.efficacy = new Double(25);
		intelligentSelf.originalPolarity = L2PossibleSelf.POLARITY_POS;
		intelligentSelf.associatedConstructs.add(construct);
		construct.relatedPossibleSelves.add(intelligentSelf);
		self.addPossibleSelf(intelligentSelf);
		
		assertEquals(2, construct.relatedPossibleSelves.size());
		assertEquals(2, construct.getRelatedPossibleSelves(L2PossibleSelf.POLARITY_POS).size());
		
		assertTrue(! self.isInterestedInWholeConstruct(701, L2PossibleSelf.POLARITY_POS));
		assertTrue(! self.isInterestedInWholeConstruct(300, L2PossibleSelf.POLARITY_POS));
		assertTrue(self.isInterestedInWholeConstruct(200, L2PossibleSelf.POLARITY_POS));
	}
	
	public void testGetConstructHits(){
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(100);
		manSelf.likelihood = new Double(500);
		manSelf.efficacy = new Double(33);
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		manSelf.polarity = L2PossibleSelf.POLARITY_POS_OR_NEG;
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(200);
		likedSelf.likelihood = new Double(60);
		likedSelf.efficacy = new Double(99);
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		likedSelf.polarity = L2PossibleSelf.POLARITY_POS;
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.desiredness = new Double(300);
		heavySelf.likelihood = new Double(20);
		heavySelf.efficacy = new Double(56);
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		heavySelf.polarity = L2PossibleSelf.POLARITY_NEG;
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.desiredness = new Double(700);
		intelligentSelf.likelihood = new Double(2);
		intelligentSelf.efficacy = new Double(25);
		intelligentSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		intelligentSelf.polarity = L2PossibleSelf.POLARITY_POS;
		self.addPossibleSelf(intelligentSelf);
		
		double threshold = 600;
		List<L2SelfConstruct> hits = self.getConstructHits(threshold, L2PossibleSelf.POLARITY_POS);
		
		assertEquals(1, hits.size());
		assertEquals("Construct 2", hits.get(0).name);
		
		hits = self.getConstructHits(threshold, L2PossibleSelf.POLARITY_NEG);
		
		assertEquals(0, hits.size());
		
		hits = self.getConstructHits(300, L2PossibleSelf.POLARITY_NEG);
		
		assertEquals(1, hits.size());
		assertEquals("Construct 2", hits.get(0).name);
	}
	
	public void testGetOverallCharacteristics(){
		//Are they motivated? : What is their mean hope motivation level?
		//What are their self-efficacy levels? : their mean hope self-efficacy level?
		//What are their expectancy levels? : their mean hope expectancy levels?
		
		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		L2PossibleSelf manSelf = new L2PossibleSelf("I am a man");
		manSelf.desiredness = new Double(100);
		manSelf.likelihood = new Double(500);
		manSelf.efficacy = new Double(33);
		manSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(manSelf);
		
		L2PossibleSelf likedSelf = new L2PossibleSelf("I am liked by others");
		likedSelf.desiredness = new Double(200);
		likedSelf.likelihood = new Double(60);
		likedSelf.efficacy = new Double(99);
		likedSelf.associatedConstructs.add(new L2SelfConstruct("Construct 1"));
		self.addPossibleSelf(likedSelf);
		
		L2PossibleSelf heavySelf = new L2PossibleSelf("I am too heavy");
		heavySelf.desiredness = new Double(300);
		heavySelf.likelihood = new Double(20);
		heavySelf.efficacy = new Double(56);
		heavySelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(heavySelf);
		
		L2PossibleSelf intelligentSelf = new L2PossibleSelf("I am smart");
		intelligentSelf.desiredness = new Double(700);
		intelligentSelf.likelihood = new Double(2);
		intelligentSelf.efficacy = new Double(25);
		intelligentSelf.associatedConstructs.add(new L2SelfConstruct("Construct 2"));
		self.addPossibleSelf(intelligentSelf);
		
		assertEquals((new Double(700 + 300 + 200 + 100)/4), self.getMeanDesiredness());
		//assertEquals(213.0, self.getMeanEfficacy());
		//assertEquals(582.0, self.getMeanLikelihood());
		
		// SD for motivation, self-efficacy, expectancy (to see if they are spread, or uniform).
		assertEquals(262, self.getDesirednessSD().intValue());
		assertEquals(33, self.getEfficacySD().intValue());
		assertEquals(237, self.getLikelihoodSD().intValue());
		
//		sConcept.getDesirednessHighestFrequencyTertile();
//		sConcept.getEfficacyHighestFrequencyTertile();
//		sConcept.getLikelihoodHighestFrequencyTertile();
//		
	}
	
	public void testGetTopNPossibleSelves(){			
		// Where do their motivations lie? : How are they distributed? Build Top N list (with desiredness)
		// Where do their self-efficacy lie? How are they distributed? Build Top N list (with self-efficacy)
		// Where do their expectancy lie? How are they distributed? Build Top N list (with likelihood)
		// * Can repeat the same, but create top N lists of constructs

		L2SelfConceptInstance self = new L2SelfConceptInstance(new TestSubject("Steve", "s.bodnar@let.ru.nl", "234", "some pword"));
		
		for(int i = 0; i < 100; i++){
			L2PossibleSelf pself = new L2PossibleSelf("I am possible self " + i);
			pself.desiredness = new Double(Math.random());
			pself.likelihood = new Double(Math.random());
			pself.efficacy = new Double(Math.random());
			self.addPossibleSelf(pself);
		}
		
		List<L2PossibleSelf> pselves = self.getTopKPossibleSelves(10, L2SelfConceptInstance.DIM_DESIREDNESS);
		
		assertEquals(10, pselves.size());
		
		L2PossibleSelf prevPSelf = pselves.get(0);
		
		for(int i = 1; i < pselves.size(); i++){
			assertTrue(prevPSelf.desiredness > pselves.get(i).desiredness);
			prevPSelf = pselves.get(i);
		}
				
		pselves = self.getTopKPossibleSelves(7, L2SelfConceptInstance.DIM_EFFICACY);
	
		assertEquals(7, pselves.size());

		prevPSelf = pselves.get(0);
		
		for(int i = 1; i < pselves.size(); i++){
			assertTrue(prevPSelf.efficacy > pselves.get(i).efficacy);
			prevPSelf = pselves.get(i);
		}
	
		pselves = self.getTopKPossibleSelves(3, L2SelfConceptInstance.DIM_LIKELIHOOD);
		
		assertEquals(3, pselves.size());
		
		prevPSelf = pselves.get(0);
		
		for(int i = 1; i < pselves.size(); i++){
			assertTrue(prevPSelf.likelihood > pselves.get(i).likelihood);
			prevPSelf = pselves.get(i);
		}
		
//		L2Tutor tutor = new L2Tutor();
//		
//		tutor.getPersonalisedMessage(subj);
	}
	
	public void testGetVariousScaleValues(){
//		self.computeMotivationCoef();
//		self.computeIntegrativenessOrientationCoef();
	}
	
	public void testDiscrepancyAreas(){
//		L2PossibleSelf self = new L2PossibleSelf();
//		
//		L2PossibleSelf idealFutureSelf = new L2PossibleSelf();
//
//		self.calcDiscrepancyCoef(idealFutureSelf)
//		self.calcDiscrepancyReport(idealFutureSelf)
	}
	
	public void testSearchForMotivationalStrategies(){
//		L2PossibleSelf currentSelf = new L2PossibleSelf();
//		
//		L2PossibleSelf idealFutureSelf = new L2PossibleSelf();
//		
//		SelfDeltaStruct selfData = currentSelf.calcDiscrepancyCoef(idealFutureSelf)
//		
//		motivationalStrategies = system.generateMotivationalStrategies(selfData)
//		
//		assertEquals(expectedMotivationalStrategies, motivationalStrategies)
	}
}
