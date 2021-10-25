package hr.fer.oprpp1.hw02.prob1;

import static hr.fer.oprpp1.hw02.prob1.LexerUtil.*;
import static java.util.Objects.requireNonNull;

/** Performs lexical analysis on provided <code>String</code> */
public class Lexer {
	/** Data given for lexical analysis */
	private final char[] data;
	// ** Last extracted Token */
	private Token token;
	/** Current position of lexical analysis */
	private int currentIndex;
	/** Current state of lexical analysis */
	private LexerState state;

	/**
	 * Initializes Lexer on given String
	 * 
	 * @param text to be analyzed
	 * @throws NullPointerException if null is passed
	 */
	public Lexer(String text) {
		requireNonNull(text);
		data = text.toCharArray();
		state = LexerState.BASIC;
	}

	/**
	 * Generates and returns next <code>Token</code>
	 * 
	 * @return next <code>Token</code>
	 * @throws LexerException if error occurs
	 */
	public Token nextToken() {
		extractNextToken();
		return token;
	}

	/**
	 * Returns last generated <code>Token</code>. Can be called multiple times. Does
	 * not initiate next token generation
	 * 
	 * @return last generated <code>Token</code>
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Sets the state of the <code>Lexer</code>
	 * 
	 * @param state
	 */
	public void setState(LexerState state) {
		requireNonNull(state);
		this.state = state;
	}

	/**
	 * Generates next token from input data
	 * 
	 * @throws LexerException if error occurs
	 */
	private void extractNextToken() {
		checkWetherAllWereGenerated();
		skipBlanks();
		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		extractWithContext();
	}

	/**
	 * Checks if all token generation was done
	 * 
	 * @throws LexerException if all tokens were already generated
	 */
	private void checkWetherAllWereGenerated() {
		if (token != null && token.getType().equals(TokenType.EOF))
			throw new LexerException();
	}

	/**
	 * Skips blanks
	 */
	private void skipBlanks() {
		while (currentIndex < data.length && isBlank(currentChar()))
			currentIndex++;
	}

	/**
	 * Extracts <code>Token</code> based on current state of <code>Lexer</code>
	 */
	private void extractWithContext() {
		switch (state) {
		case BASIC -> extractBasic();
		case EXTENDED -> extractExtended();
		}
	}

	/**
	 * Extracts next <code>Token</code> in basic mode
	 */
	private void extractBasic() {
		if (isCandidateForWord(currentChar())) {
			extractWordWithEscapes();
			return;
		}
		if (Character.isDigit(currentChar())) {
			extractNumber();
			return;
		}
		extractSymbol();
	}

	/**
	 * Extracts next <code>Token</code> in extended state
	 */
	private void extractExtended() {
		if (currentChar() == '#') {
			extractSymbol();
			return;
		}
		int start = currentIndex;
		do {
			currentIndex++;
		} while (currentIndex < data.length && !isBlank(currentChar()) && currentChar() != '#');
		token = new Token(TokenType.WORD, String.valueOf(data, start, currentIndex - start));
	}

	/**
	 * Extracts word with possible escaped characters into <code>Token</code>
	 */
	private void extractWordWithEscapes() {
		final StringBuilder builder = new StringBuilder();
		do {
			if (isEscape(currentChar())) {
				currentIndex++;
				if (currentIndex == data.length || !isValidEscapeCharacter(currentChar()))
					throw new LexerException();
			}
			builder.append(currentChar());
			currentIndex++;
		} while (currentIndex < data.length && isCandidateForWord(currentChar()));
		token = new Token(TokenType.WORD, builder.toString());
	}

	/**
	 * Extracts number into <code>Token</code>
	 */
	private void extractNumber() {
		final int start = currentIndex;
		do {
			currentIndex++;
		} while (currentIndex < data.length && Character.isDigit(currentChar()));
		Long result;
		try {
			result = Long.valueOf(String.valueOf(data, start, currentIndex - start));
		} catch (NumberFormatException exc) {
			throw new LexerException(exc);
		}
		token = new Token(TokenType.NUMBER, result);
	}

	/**
	 * Extracts symbol into <code>Token</code>
	 */
	private void extractSymbol() {
		token = new Token(TokenType.SYMBOL, Character.valueOf(currentChar()));
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

}
