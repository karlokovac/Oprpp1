package hr.fer.oprpp1.hw02.prob1;

import java.util.Arrays;

public final class LexerUtil {
	private static final char[] BLANKS;
	private static final Token POUND_TOKEN = new Token(TokenType.SYMBOL, Character.valueOf('#'));

	static {
		char[] blanks = { '\r', '\n', '\t', ' ' };
		Arrays.sort(blanks);
		BLANKS = blanks;
	}
	
	public static boolean isBlank(char character) {
		return Arrays.binarySearch(BLANKS, character) >= 0;
	}

	public static boolean isValidEscapeCharacter(char character) {
		return Character.isDigit(character) || isEscape(character);
	}

	public static boolean isCandidateForWord(char character) {
		return Character.isLetter(character) || isEscape(character);
	}
	
	public static boolean isEscape(char character) {
		return Character.compare('\\', character) == 0;
	}
	
	public static boolean isPoundToken(Token token) {
		return token.equals(POUND_TOKEN);
	}
}
