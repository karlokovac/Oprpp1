package hr.fer.oprpp1.hw02.prob1;

import java.util.Arrays;

/**
 * Contains utilities for lexical analysis of text
 */
public final class LexerUtil {
	/**
	 * Array of blank characters
	 */
	private static final char[] BLANKS;

	static {
		char[] blanks = { '\r', '\n', '\t', ' ' };
		Arrays.sort(blanks);
		BLANKS = blanks;
	}

	/**
	 * Checks whether <code>char</code> is any of defined blank characters
	 * 
	 * @param character to be checked
	 * @return truth of the statement
	 */
	public static boolean isBlank(char character) {
		return Arrays.binarySearch(BLANKS, character) >= 0;
	}

	/**
	 * Checks whether <code>char</code> is valid for escaping
	 * 
	 * @param character to be checked
	 * @return truth of the statement
	 */
	public static boolean isValidEscapeCharacter(char character) {
		return Character.isDigit(character) || isEscape(character);
	}

	/***
	 * Checks whether <code>char</code> is possible beginning of a word
	 * 
	 * @param character to be checked
	 * @return truth of the statement
	 */
	public static boolean isCandidateForWord(char character) {
		return Character.isLetter(character) || isEscape(character);
	}

	/**
	 * Checks whether <code>char</code> is escape character
	 * 
	 * @param character to be checked
	 * @return truth of the statement
	 */
	public static boolean isEscape(char character) {
		return Character.compare('\\', character) == 0;
	}

}
