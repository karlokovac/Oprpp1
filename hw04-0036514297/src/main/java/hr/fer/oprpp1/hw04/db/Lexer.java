package hr.fer.oprpp1.hw04.db;

import java.util.Arrays;
import java.util.function.Predicate;

public class Lexer {
    /**
     * Characters used in operators
     */
    private static final char[] OPERATOR_CHARS;

    static {
        char[] ops = {'<', '>', '=', '!'};
        Arrays.sort(ops);
        OPERATOR_CHARS = ops;
    }

    /**
     * Data given for lexical analysis
     */
    private final char[] data;
    /**
     * Current position of lexical analysis
     */
    private int currentIndex;
    // ** Last extracted QueryToken */
    private Token token;

    /**
     * Initializes QueryLexer on given String
     *
     * @param text to be analyzed
     */
    public Lexer(String text) {
        data = text.toCharArray();
    }

    /**
     * Generates and returns next <code>QueryToken</code>
     *
     * @return next <code>QueryToken</code>
     * @throws LexerException if error occurs
     */
    public Token nextToken() {
        extractNextToken();
        return token;
    }

    /**
     * Returns last generated <code>QueryToken</code>. Can be called multiple times.
     * Does not initiate next QueryToken generation
     *
     * @return last generated <code>QueryToken</code>
     */
    public Token getToken() {
        return token;
    }

    /**
     * Generates next QueryToken from input data
     *
     * @throws LexerException if error occurs
     */
    private void extractNextToken() {
        if (token != null && token.type().equals(TokenType.EOF))
            throw new LexerException("No characters to tokenize\n");

        skipBlanks();
        if (currentIndex >= data.length)
            token = new Token(TokenType.EOF, null);
        else if (Character.isLetter(currentChar()))
            extractWord();
        else if (currentChar() == '"')
            extractQuoted();
        else
            extractOperator();
    }

    /**
     * Extracts word into token
     */
    private void extractWord() {
        extractBlock(Character::isLetter, TokenType.WORD);
    }

    private void extractQuoted() {
        extractBlock(ch -> Character.isLetterOrDigit(ch) || ch == '"' || ch == '*', TokenType.QUOTED);
        if (token.value().indexOf('*') != token.value().lastIndexOf('*'))
            throw new MultipleWildcardException("Multiple wildcard symbols are forbidden");
    }

    /**
     * Extracts operator into token
     */
    private void extractOperator() {
        extractBlock(Lexer::isOperatorChar, TokenType.OPERATOR);
    }

    /**
     * Extracts characters while condition lasts and assigns the given type
     */
    private void extractBlock(Predicate<Character> condition, TokenType type) {
        final int startIndex = currentIndex++;
        progressWhile(condition);
        String word = String.valueOf(data, startIndex, currentIndex - startIndex);
        token = new Token(type, word);
    }

    /**
     * Skips blanks
     */
    private void skipBlanks() {
        progressWhile(Lexer::isBlank);
    }

    /**
     * Increments index while condition is true
     */
    private void progressWhile(Predicate<Character> condition) {
        while (currentIndex < data.length && condition.test(currentChar()))
            currentIndex++;
    }

    /**
     * Query for current char in data
     *
     * @return char on current index
     */
    private char currentChar() {
        return data[currentIndex];
    }

    /**
     * Checks if given character is valid operator character
     *
     * @param currentChar to check
     * @return true if it is valid
     */
    private static boolean isOperatorChar(char currentChar) {
        return Arrays.binarySearch(OPERATOR_CHARS, currentChar) >= 0;
    }

    /**
     * Checks if given character is a "blank" one
     *
     * @param currentChar to test
     * @return true if is "blank"
     */
    private static boolean isBlank(char currentChar) {
        return currentChar == ' ' || currentChar == '\t';
    }
}
