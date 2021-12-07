package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class QueryParser {

    /**
     * Lookup table for attribute specific processing
     */
    private static final Map<String, Function<Lexer, ConditionalExpression>> ATTRIBUTES_LOOKUP;
    /**
     * Lookup table for operator specific IComparisonOperator
     */
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

    /**
     * Stores the generated ConditionalExpression list
     */
    private final List<ConditionalExpression> queryExpression;

    /**
     * Constructs and parses the query
     *
     * @param query to parse
     * @throws ParserException if an error occurred
     */
    public QueryParser(String query) {
        var lexer = new Lexer(query);
        var first = lexer.nextToken();
        if (first.type() == TokenType.EOF || "query".compareToIgnoreCase(first.value()) != 0)
            throw new ParserException("Only query command is supported");

        try {
            queryExpression = parseQuery(lexer);
        } catch (LexerException e) {
            throw new ParserException(e.getMessage());
        }

        if (queryExpression.size() == 0)
            throw new ParserException("No query arguments provided");

    }

    /**
     * Checks whether query is a direct one
     *
     * @return true if it is
     */
    public boolean isDirectQuery() {
        if (queryExpression.size() == 1) {
            var expr = queryExpression.get(0);
            return expr.getFieldGetter() == FieldValueGetters.JMBAG
                    && expr.getComparisonOperator() == ComparisonOperators.EQUALS;
        }
        return false;
    }

    /**
     * Fetches the literal from the direct query
     *
     * @return literal
     * @throws IllegalStateException if query isn't a direct one
     */
    public String getQueriedJMBAG() {
        if (!isDirectQuery())
            throw new IllegalStateException();
        return queryExpression.get(0).getStringLiteral();
    }

    public List<ConditionalExpression> getQuery() {
        return queryExpression;
    }

    /**
     * Parses the input and consumes tokens
     *
     * @param lexer to extract tokens
     * @return list of ConditionalExpression elements
     * @throws ParserException if an error occurred
     */
    private static List<ConditionalExpression> parseQuery(Lexer lexer) {
        var expression = new ArrayList<ConditionalExpression>();
        for (var token = lexer.nextToken(); token.type() != TokenType.EOF; token = lexer.nextToken()) {
            var word = token.value().toLowerCase();
            if (!ATTRIBUTES_LOOKUP.containsKey(word)) {
                if ("and".compareToIgnoreCase(word) == 0)
                    continue;
                else
                    throw new ParserException(token.value() + " is not a valid attribute");
            }
            expression.add(ATTRIBUTES_LOOKUP.get(word).apply(lexer));
        }
        return expression;
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

    /**
     * Processes input tokens and constructs ConditionalExpression
     *
     * @param lexer  to consume tokens from
     * @param getter which to assign
     * @return ConditionalExpression
     * @throws ParserException if an error occurs
     */
    private static ConditionalExpression process(Lexer lexer, IFieldValueGetter getter) {
        var token = lexer.nextToken();
        if (token.type() != TokenType.OPERATOR && !OPERATORS_LOOKUP.containsKey(token.value()))
            throw new ParserException("Expected an operator: " + token.value());
        var operator = OPERATORS_LOOKUP.get(token.value());
        token = lexer.nextToken();
        if (token.type() != TokenType.QUOTED)
            throw new ParserException("Expected a literal");
        var literal = token.value().replace("\"", "");
        return new ConditionalExpression(getter, literal, operator);
    }
}
