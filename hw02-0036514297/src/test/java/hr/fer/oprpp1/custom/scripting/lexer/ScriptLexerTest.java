package hr.fer.oprpp1.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class LexerTest {

	@Test
	public void testConstructorPassingNull() {
		assertThrows(NullPointerException.class, () -> new Lexer(null));
	}

	@Test
	public void testEmptyDocument() {
		Lexer lex = new Lexer("");
		assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test
	public void testCallingAfterEOF() {
		Lexer lex = new Lexer("");
		lex.nextToken();
		assertThrows(LexerException.class, () -> lex.nextToken());
	}

	@Test
	private void testAllBlanks() {
		Lexer lex = new Lexer("                    ");
		assertEquals(TokenType.EOF, lex.nextToken().getType());
	}

	@Test
	private void testSingleWord() {
		Lexer lex = new Lexer("  Rimac");
		checkToken(new Token(TokenType.WORD, "Rimac"), lex.getToken());
	}

	private void checkToken(Token expected, Token actual) {
		String msg = "Token are not equal.";
		assertEquals(expected.getType(), actual.getType(), msg);
		assertEquals(expected.getValue(), actual.getValue(), msg);
	}

	private void checkTokenStream(Lexer lexer, Token[] correctData) {
		int counter = 0;
		for (Token expected : correctData) {
			Token actual = lexer.nextToken();
			String msg = "Checking token " + counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}
}
