package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class QueryLexerTest {

	@Test
	public void testQuery() {
		var lexer = new Lexer("query");
		Token[] correctData = { new Token(TokenType.WORD, "query"), new Token(TokenType.EOF, null) };
		checkQueryTokenStream(lexer, correctData);

	}

	@Test
	public void testDirectQuery() {
		var lexer = new Lexer("query jmbag=\"0000000003\"");
		Token[] correctData = { new Token(TokenType.WORD, "query"), new Token(TokenType.WORD, "jmbag"),
				new Token(TokenType.OPERATOR, "="), new Token(TokenType.QUOTED, "\"0000000003\""),
				new Token(TokenType.EOF, null) };
		checkQueryTokenStream(lexer, correctData);
	}

	@Test
	public void testSpacesQuery() {
		var lexer = new Lexer("query lastName =     \"Blažić\"");
		Token[] correctData = { new Token(TokenType.WORD, "query"), new Token(TokenType.WORD, "lastName"),
				new Token(TokenType.OPERATOR, "="), new Token(TokenType.QUOTED, "\"Blažić\""),
				new Token(TokenType.EOF, null) };
		checkQueryTokenStream(lexer, correctData);
	}

	@Test
	public void testMultipleQuery() {
		var lexer = new Lexer("query firstName>\"A\" and lastName LIKE \"B*ć\"");
		Token[] correctData = { new Token(TokenType.WORD, "query"), new Token(TokenType.WORD, "firstName"),
				new Token(TokenType.OPERATOR, ">"), new Token(TokenType.QUOTED, "\"A\""),
				new Token(TokenType.WORD, "and"), new Token(TokenType.WORD, "lastName"),
				new Token(TokenType.WORD, "LIKE"), new Token(TokenType.QUOTED, "\"B*ć\""),
				new Token(TokenType.EOF, null) };
		checkQueryTokenStream(lexer, correctData);
	}

	@Test
	public void testHugeQuery() {
		var lexer = new Lexer(
				"query firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");
		Token[] correctData = { new Token(TokenType.WORD, "query"), new Token(TokenType.WORD, "firstName"),
				new Token(TokenType.OPERATOR, ">"), new Token(TokenType.QUOTED, "\"A\""),
				new Token(TokenType.WORD, "and"), new Token(TokenType.WORD, "firstName"),
				new Token(TokenType.OPERATOR, "<"), new Token(TokenType.QUOTED, "\"C\""),
				new Token(TokenType.WORD, "and"), new Token(TokenType.WORD, "lastName"),
				new Token(TokenType.WORD, "LIKE"), new Token(TokenType.QUOTED, "\"B*ć\""),
				new Token(TokenType.WORD, "and"), new Token(TokenType.WORD, "jmbag"),
				new Token(TokenType.OPERATOR, ">"), new Token(TokenType.QUOTED, "\"0000000002\""),
				new Token(TokenType.EOF, null) };
		checkQueryTokenStream(lexer, correctData);
	}

	@Test
	public void testNotNull() {
		Lexer lexer = new Lexer("");
		assertNotNull(lexer.nextToken(), "QueryToken was expected but null was returned.");
	}

	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");
		assertEquals(TokenType.EOF, lexer.nextToken().type(), "Empty input must generate only EOF token.");
	}

	@Test
	public void testGetReturnsLastNext() {
		// Calling getQueryToken once or several times after calling nextQueryToken must
		// return
		// each time what nextQueryToken returned...
		Lexer lexer = new Lexer("");
		Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getQueryToken returned different token than nextQueryToken.");
		assertEquals(token, lexer.getToken(), "getQueryToken returned different token than nextQueryToken.");
	}

	@Test
	public void testNoActualContent() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("   \t    ");
		assertEquals(TokenType.EOF, lexer.nextToken().type(),
				"Input had no content. QueryLexer should generated only EOF token.");
	}

	@Test
	public void testTwoWords() {
		// Lets check for several words...
		Lexer lexer = new Lexer("  Štefanija Automobil   ");
		// We expect the following stream of tokens
		Token[] correctData = { new Token(TokenType.WORD, "Štefanija"), new Token(TokenType.WORD, "Automobil"),
				new Token(TokenType.EOF, null) };
		checkQueryTokenStream(lexer, correctData);
	}

	// Helper method for checking if lexer generates the same stream of tokens
	// as the given stream.	
	private void checkQueryTokenStream(Lexer lexer, Token[] correctData) {
		int counter = 0;
		for (Token expected : correctData) {
			Token actual = lexer.nextToken();
			String msg = "Checking token " + counter + ":";
			assertEquals(expected.type(), actual.type(), msg);
			assertEquals(expected.value(), actual.value(), msg);
			counter++;
		}
	}

}
