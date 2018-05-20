package test.ru.fasop.motivation;

import ru.fasop.GertKeyValuePair;
import ru.fasop.motivation.L2PossibleSelf;
import ru.fasop.motivation.L2SelfConstruct;
import test.ru.fasop.GertContentTestCase;

public class L2PossibleSelfTest extends GertContentTestCase {
	public void testConstructor(){
		String pSelfAsTest = "Some idea I have about a way I am or could be";
		
		L2PossibleSelf pSelf = new L2PossibleSelf(pSelfAsTest);
		
		assertEquals(pSelfAsTest, pSelf.pSelfAsText);
	}
	
	public void testVariousParameters(){
		String pSelfAsTest = "I am a bad person";
		
		L2PossibleSelf pSelf = new L2PossibleSelf(pSelfAsTest);
		pSelf.isOriginal = new Boolean(true);
		pSelf.isCustom = new Boolean(false);
		
		assertEquals(new Boolean(true), pSelf.isOriginal);
		assertEquals(new Boolean(false), pSelf.isCustom);
	}
	
	public void testOriginalPolarity(){
		String pSelfAsTest = "I am a bad person";
		
		L2PossibleSelf pSelf = new L2PossibleSelf(pSelfAsTest);
		pSelf.originalPolarity = L2PossibleSelf.POLARITY_NEG;
		
		assertEquals(L2PossibleSelf.POLARITY_NEG, pSelf.originalPolarity);
		
		pSelf = new L2PossibleSelf("I am a nice person");
		pSelf.originalPolarity = L2PossibleSelf.POLARITY_POS;
		
		assertEquals(L2PossibleSelf.POLARITY_POS, pSelf.originalPolarity);
		
		pSelf = new L2PossibleSelf("I have free pizza for the rest of my life");
		pSelf.originalPolarity = L2PossibleSelf.POLARITY_POS_OR_NEG;
		
		assertEquals(L2PossibleSelf.POLARITY_POS_OR_NEG, pSelf.originalPolarity);
	}
	
	public void testGetPolarity(){
		String pSelfAsTest = "I am a bad person";
		
		L2PossibleSelf pSelf = new L2PossibleSelf(pSelfAsTest);
		pSelf.polarity = L2PossibleSelf.POLARITY_NEG;
		
		assertEquals(L2PossibleSelf.POLARITY_NEG, pSelf.polarity);
		
		pSelf = new L2PossibleSelf("I am a nice person");
		pSelf.polarity = L2PossibleSelf.POLARITY_POS;
		
		assertEquals(L2PossibleSelf.POLARITY_POS, pSelf.polarity);
		
		pSelf = new L2PossibleSelf("I have free pizza for the rest of my life");
		pSelf.polarity = L2PossibleSelf.POLARITY_POS_OR_NEG;
		
		assertEquals(L2PossibleSelf.POLARITY_POS_OR_NEG, pSelf.polarity);
	}
	
	public void testSetAssociatedConstructs(){
		String pSelfAsTest = "Some idea I have about a way I am or could be";
		
		L2PossibleSelf pSelf = new L2PossibleSelf(pSelfAsTest);
		
		pSelf.associatedConstructs.add(new L2SelfConstruct("Integrativeness"));
		pSelf.associatedConstructs.add(new L2SelfConstruct("Instrumentality"));
		pSelf.associatedConstructs.add(new L2SelfConstruct("Language Use Anxiety"));
		
		assertEquals("Integrativeness", pSelf.associatedConstructs.get(0).name);
		assertEquals("Instrumentality", pSelf.associatedConstructs.get(1).name);
		assertEquals("Language Use Anxiety", pSelf.associatedConstructs.get(2).name);
	}
	
	public void testAddPossibleData(){
		String pSelfAsTest = "I am too heavy";
		
		L2PossibleSelf pSelf = new L2PossibleSelf(pSelfAsTest);
		pSelf.desiredness = new Double(-2);
		pSelf.likelihood = new Double(2);
		pSelf.efficacy = new Double(45);
		
		pSelf.metadata.add(new GertKeyValuePair("comment", "this is an example possible self I am working with"));
		
		assertEquals(new Double(-2), pSelf.desiredness);
		assertEquals(new Double(2), pSelf.likelihood);
		assertEquals(new Double(45), pSelf.efficacy);
		assertEquals("this is an example possible self I am working with", pSelf.metadata.get(0).valyou);
	}
	
	public void testGetInstance(){
		String pSelfAsTest = "I am too heavy";
		
		L2PossibleSelf pSelf = new L2PossibleSelf(pSelfAsTest);
		pSelf.desiredness = new Double(-2);
		pSelf.likelihood = new Double(2);
		
		pSelf.metadata.add(new GertKeyValuePair("comment", "this is an example possible self I am working with"));
		
		L2PossibleSelf inst = pSelf.getInstance();
		
		assertEquals(null, inst.id);
		assertEquals(new Double(-1), inst.desiredness);
		assertEquals(new Double(-1), inst.likelihood);
		assertEquals(pSelf.metadata.get(0).valyou, inst.metadata.get(0).valyou);
	}
}
