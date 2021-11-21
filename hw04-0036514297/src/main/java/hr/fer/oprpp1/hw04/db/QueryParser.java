package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class QueryParser {

	private static final Map<String, Function<Lexer, ConditionalExpression>> ATTRIBUTES_LOOKUP;
	private static final Map<String, IComparisonOperator> OPERATORS_LOOKUP;

	static {
		ATTRIBUTES_LOOKUP = new HashMap<>();
		ATTRIBUTES_LOOKUP.put("jmbag", QueryParser::processJmbag);
		ATTRIBUTES_LOOKUP.put("firstname", QueryParser::processFirstName);
		ATTRIBUTES_LOOKUP.put("lastname", QueryParser::processLastName);

		OPERATORS_LOOKUP = new HashMap<>();
		OPERATORS_LOOKUP.put("<", ComparisonOperators.LESS);
		OPERATORS_LOOKUP.put("<=", ComparisonOperators.LESS_OR_EQUALS);
		OPERATORS_LOOKUP.put(">", ComparisonOperators.GREATER);
		OPERATORS_LOOKUP.put(">=", ComparisonOperators.GREATER_OR_EQUALS);
		OPERATORS_LOOKUP.put("=", ComparisonOperators.EQUALS);
		OPERATORS_LOOKUP.put("!=", ComparisonOperators.NOT_EQUALS);
		OPERATORS_LOOKUP.put("LIKE", ComparisonOperators.LIKE);
	}

	private final List<ConditionalExpression> queryExpression;

	public QueryParser(String query) {
		var lexer = new Lexer(query);
		var first = lexer.nextToken();
		if (first.getType() == TokenType.EOF || "query".compareToIgnoreCase(first.getValue()) != 0)
			throw new ParserException("Only query command is supported");

		try {
			queryExpression = parse(lexer);
		} catch (LexerException e) {
			throw new ParserException(e.getMessage());
		}

		if (queryExpression.size() == 0)
			throw new ParserException("No query arguments provided");

	}

	private static List<ConditionalExpression> parse(Lexer lexer) {
		var expression = new ArrayList<ConditionalExpression>();
		for (var token = lexer.nextToken(); token.getType() != TokenType.EOF; token = lexer.nextToken()) {
			var word = token.getValue().toLowerCase();
			if (!ATTRIBUTES_LOOKUP.containsKey(word)) {
				if ("and".compareToIgnoreCase(word) == 0)
					continue;
				else
					throw new ParserException(token.getValue() + " is not a valid attribute");
			}
			expression.add(ATTRIBUTES_LOOKUP.get(word).apply(lexer));
		}
		return expression;
	}

	public boolean isDirectQuery() {
		if (queryExpression.size() == 1) {
			var expr = queryExpression.get(0);
			return expr.getFieldGetter() == FieldValueGetters.JMBAG
					&& expr.getComparisonOperator() == ComparisonOperators.EQUALS;
		}
		return false;
	}

	public String getQueriedJMBAG() {
		if (!isDirectQuery())
			throw new IllegalStateException();
		return queryExpression.get(0).getStringLiteral();
	}

	public List<ConditionalExpression> getQuery() {
		return queryExpression;
	}

	private static ConditionalExpression processJmbag(Lexer lexer) {
		return process(lexer, FieldValueGetters.JMBAG);
	}

	private static ConditionalExpression processFirstName(Lexer lexer) {
		return process(lexer, FieldValueGetters.FIRST_NAME);
	}

	private static ConditionalExpression processLastName(Lexer lexer) {
		return process(lexer, FieldValueGetters.LAST_NAME);
	}

	private static ConditionalExpression process(Lexer lexer, IFieldValueGetter getter) {
		var token = lexer.nextToken();
		if (token.getType() != TokenType.OPERATOR && !OPERATORS_LOOKUP.containsKey(token.getValue()))
			throw new ParserException("Expected an operator: " + token.getValue());
		var operator = OPERATORS_LOOKUP.get(token.getValue());
		token = lexer.nextToken();
		if (token.getType() != TokenType.QUOTED)
			throw new ParserException("Expected a literal");
		var literal = token.getValue().replace("\"", "");
		return new ConditionalExpression(getter, literal, operator);
	}
}
