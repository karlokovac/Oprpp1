package hr.fer.oprpp1.hw02.prob1;

import java.util.Arrays;
import java.util.Objects;

public class Lexer {
	private final char[] data; // ulazni tekst
	private Token token; // trenutni token
	private int currentIndex; // indeks prvog neobrađenog znaka

	private static final char[] BLANKS;

	static {
		char[] blanks = { '\r', '\n', '\t', ' ' };
		Arrays.sort(blanks);
		BLANKS = blanks;
	}

	// konstruktor prima ulazni tekst koji se tokenizira
	public Lexer(String text) {
		Objects.requireNonNull(text);
		data = text.toCharArray();
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

	private Token searchNextToken() {
		if (token != null && token.getType().equals(TokenType.EOF))
			throw new LexerException();

		skipBlanks();

		if (currentIndex >= data.length)
			return new Token(TokenType.EOF, null);

		return null;
	}

	private void skipBlanks() {
		while (currentIndex < data.length && isBlank(data[currentIndex]))
			currentIndex++;
	}

	private boolean isBlank(char character) {
		return Arrays.binarySearch(BLANKS, character) >= 0;
	}
}
