package hr.fer.oprpp1.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ScriptLexerTest {

	@Test
	public void testConstructorPassingNull() {
		assertThrows(NullPointerException.class, () -> new ScriptLexer(null));
	}

	@Test
	public void testEmptyDocument() {
		ScriptLexer lex = new ScriptLexer("");
		assertEquals(ScriptTokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void testCallingAfterEOF() {
		ScriptLexer lex = new ScriptLexer("");
		lex.nextToken();
		assertThrows(ScriptLexerException.class, () -> lex.nextToken());
	}

	@Test
	public void testAllBlanks() {
		ScriptLexer lex = new ScriptLexer("                    ");
		assertEquals(ScriptTokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void testSingleWord() {
		ScriptLexer lex = new ScriptLexer("Rimac");
		var actual = new ScriptToken[] { new ScriptToken(ScriptTokenType.WORD, "Rimac"),
				new ScriptToken(ScriptTokenType.EOF, null) };
		checkTokenStream(lex, actual);
	}

	public void checkToken(ScriptToken expected, ScriptToken actual, String msg) {
		assertEquals(expected.getType(), actual.getType(), msg);
		assertEquals(expected.getValue(), actual.getValue(), msg);
	}

	private void checkTokenStream(ScriptLexer lexer, ScriptToken[] correctData) {
		int counter = 0;
		for (ScriptToken expected : correctData) {
			ScriptToken actual = lexer.nextToken();
			String msg = "Checking token " + counter + ":";
			checkToken(expected, actual, msg);
			counter++;
		}
	}
}
