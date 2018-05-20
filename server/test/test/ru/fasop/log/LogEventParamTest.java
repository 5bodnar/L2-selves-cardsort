package test.ru.fasop.log;

import java.util.List;

import ru.fasop.log.LogEventParam;
import test.ru.fasop.GertContentTestCase;

public class LogEventParamTest extends GertContentTestCase {

    public void testParse() {
    	List<LogEventParam> params = LogEventParam.parse("name1 : value1, name2 : value2, name3 : value3");
    	
    	assertEquals(3, params.size());
    	assertEquals("name1", params.get(0).name);
    	assertEquals("value3", params.get(2).value);
    }
}
