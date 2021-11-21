package hr.fer.oprpp1.hw04.db;

/** Models token to be generated during lexical analysis of text */
public class Token {
	/** Type of token */
	private final TokenType type;
	/** value stored in token */
	private final String value;

	/**
	 * Initializes <code>Token</code> with provided parameters
	 * 
	 * @param type  of token
	 * @param value of token
	 */
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Retrieves value of <code>Token</code>
	 * 
	 * @return <code>Token</code> value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Retrieves type of the <code>Token</code>
	 * 
	 * @return type of the <code>Token</code>
	 */
	public TokenType getType() {
		return type;
	}
}
