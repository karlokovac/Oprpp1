package hr.fer.oprpp1.hw04.db;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {

	@Test
	public void testEmptyQuery() {
		assertThrows(ParserException.class, () -> new QueryParser(""));
	}

	@Test
	public void testOnlyCommandQuery() {
		assertThrows(ParserException.class, () -> new QueryParser("query"));
	}

	@Test
	public void testDirectQuery() {
		var parser = new QueryParser("query jmbag=\"0000000003\"");
		assertTrue(parser.isDirectQuery());
		checkExpressions(
				List.of(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000003", ComparisonOperators.EQUALS)),
				parser.getQuery());
	}

	@Test
	public void testLastNameQuery() {
		var parser = new QueryParser("query lastName =     \"Blažić\"");
		assertFalse(parser.isDirectQuery());
		checkExpressions(
				List.of(new ConditionalExpression(FieldValueGetters.LAST_NAME, "Blažić", ComparisonOperators.EQUALS)),
				parser.getQuery());
	}

	@Test
	public void testMultipleQuery() {
		var parser = new QueryParser("query firstName>\"A\" and lastName LIKE \"B*ć\"");
		checkExpressions(
				List.of(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER),
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE)),
				parser.getQuery());
	}

	@Test
	public void testBigQuery() {
		var parser = new QueryParser(
				"query firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");
		checkExpressions(
				List.of(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER),
						new ConditionalExpression(FieldValueGetters.FIRST_NAME, "C", ComparisonOperators.LESS),
						new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE),
						new ConditionalExpression(FieldValueGetters.JMBAG, "0000000002", ComparisonOperators.GREATER)),
				parser.getQuery());
	}

	private void checkExpressions(List<ConditionalExpression> expected, List<ConditionalExpression> actual) {
		assertEquals(expected.size(), actual.size());
		for (int i = 0, size = expected.size(); i < size; i++) {
			assertEquals(expected.get(i).getFieldGetter(), actual.get(i).getFieldGetter());
			assertEquals(expected.get(i).getStringLiteral(), actual.get(i).getStringLiteral());
			assertEquals(expected.get(i).getComparisonOperator(), actual.get(i).getComparisonOperator());
		}
	}
}
