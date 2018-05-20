package test.ru.fasop.motivation;

import ru.fasop.GertKeyValuePair;
import ru.fasop.motivation.L2PossibleSelf;
import ru.fasop.motivation.L2SelfConstruct;
import test.ru.fasop.GertContentTestCase;

public class L2SelfConstructTest extends GertContentTestCase {
	public void testConstructor(){
		String description = "Integrativeness is from Gardner. It's the idea that" +
		" you want to really fit in with the community of target language native speakers.";
		
		L2SelfConstruct integrativenessConstruct = new L2SelfConstruct("Integrativeness");
		integrativenessConstruct.description = description;
		
		assertEquals("Integrativeness", integrativenessConstruct.name);
		assertEquals(description, integrativenessConstruct.description);
		
		integrativenessConstruct.metadata.add(new GertKeyValuePair("comment", "this is an example possible self I am working with"));
		
		assertEquals("this is an example possible self I am working with", integrativenessConstruct.metadata.get(0).valyou);
	}
	
	public void testAddPossibleSelf(){
		L2SelfConstruct integrativenessConstruct = new L2SelfConstruct("Integrativeness");
		
		integrativenessConstruct.relatedPossibleSelves.add(new L2PossibleSelf("I want to be similar to Dutch people"));
		
		assertEquals("I want to be similar to Dutch people", integrativenessConstruct.relatedPossibleSelves.get(0).pSelfAsText);
	}
}
