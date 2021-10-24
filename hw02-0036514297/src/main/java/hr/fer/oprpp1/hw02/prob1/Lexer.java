package hr.fer.oprpp1.hw02.prob1;

import static hr.fer.oprpp1.hw02.prob1.LexerUtil.*;
import static java.util.Objects.requireNonNull;

public class Lexer {
	private final char[] data; // ulazni tekst
	private Token token; // trenutni token
	private int currentIndex; // indeks prvog neobrađenog znaka
	private LexerState state;

	// konstruktor prima ulazni tekst koji se tokenizira
	public Lexer(String text) {
		requireNonNull(text);
		data = text.toCharArray();
		state = LexerState.BASIC;
	}

	// generira i vraća sljedeći token
	// baca LexerException ako dođe do pogreške
	public Token nextToken() {
		searchNextToken();
		return token;
	}

	// vraća zadnji generirani token; može se pozivati
	// više puta; ne pokreće generiranje sljedećeg tokena
	public Token getToken() {
		return token;
	}

	public void setState(LexerState state) {
		requireNonNull(state);
		this.state = state;
	}

	private void searchNextToken() {
		throwIfAlreadyEOF();
		skipBlanks();
		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		extractWithContext();
	}

	private void throwIfAlreadyEOF() {
		if (token != null && token.getType().equals(TokenType.EOF))
			throw new LexerException();
	}

	private void skipBlanks() {
		while (currentIndex < data.length && isBlank(currentChar()))
			currentIndex++;
	}

	private void extractWithContext() {
		switch (state) {
		case BASIC -> extractBasic();
		case EXTENDED -> extractExtended();
		}
	}

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

	private void extractSymbol() {
		token = new Token(TokenType.SYMBOL, Character.valueOf(currentChar()));
		currentIndex++;
	}

	private char currentChar() {
		return data[currentIndex];
	}

}
