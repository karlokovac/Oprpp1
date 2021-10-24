package hr.fer.oprpp1.hw02.prob1;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public class Lexer {
	private final char[] data; // ulazni tekst
	private Token token; // trenutni token
	private int currentIndex; // indeks prvog neobrađenog znaka
	private State state;

	private static final char[] BLANKS;

	private final State basicState = () -> {
		if (isCandidateForWord(currentChar()))
			return searchWordWithEscapes();
		if (Character.isDigit(currentChar()))
			return searchNumber();
		return searchSymbol();
	};

	static {
		char[] blanks = { '\r', '\n', '\t', ' ' };
		Arrays.sort(blanks);
		BLANKS = blanks;
	}

	// konstruktor prima ulazni tekst koji se tokenizira
	public Lexer(String text) {
		requireNonNull(text);
		data = text.toCharArray();
		state = basicState;
	}

	// generira i vraća sljedeći token
	// baca LexerException ako dođe do pogreške
	public Token nextToken() {
		token = searchNextToken();
		return getToken();
	}

	// vraća zadnji generirani token; može se pozivati
	// više puta; ne pokreće generiranje sljedećeg tokena
	public Token getToken() {
		return token;
	}

	public void setState(LexerState state) {
		requireNonNull(state);
		switch (state) {
		case BASIC -> this.state = basicState;
		case EXTENDED -> this.state = null;
		}
	}

	private Token searchNextToken() {
		throwIfAlreadyEOF();
		skipBlanks();
		if (currentIndex >= data.length)
			return new Token(TokenType.EOF, null);
		return state.getNextToken();
	}

	private void throwIfAlreadyEOF() {
		if (token != null && token.getType().equals(TokenType.EOF))
			throw new LexerException();
	}

	private void skipBlanks() {
		while (currentIndex < data.length && isBlank(currentChar()))
			currentIndex++;
	}

	private Token searchWordWithEscapes() {
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
		return new Token(TokenType.WORD, builder.toString());
	}

	private Token searchNumber() {
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
		return new Token(TokenType.NUMBER, result);
	}

	private Token searchSymbol() {
		final var token = new Token(TokenType.SYMBOL, Character.valueOf(currentChar()));
		currentIndex++;
		return token;
	}

	private char currentChar() {
		return data[currentIndex];
	}

	private static boolean isBlank(char character) {
		return Arrays.binarySearch(BLANKS, character) >= 0;
	}

	private static boolean isEscape(char character) {
		return Character.compare('\\', character) == 0;
	}

	private static boolean isValidEscapeCharacter(char character) {
		return Character.isDigit(character) || isEscape(character);
	}

	private static boolean isCandidateForWord(char character) {
		return Character.isLetter(character) || isEscape(character);
	}
	
	@FunctionalInterface
	private interface State {
		Token getNextToken();
	}

}
