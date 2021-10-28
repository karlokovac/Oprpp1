package hr.fer.oprpp1.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SmartScriptParserTest {

	@Test
	public void testConstructorNull() {
		assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
	}

}
