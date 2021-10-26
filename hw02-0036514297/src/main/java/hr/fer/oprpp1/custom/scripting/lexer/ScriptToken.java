package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Models token to be generated during lexical analysis of text
 */
public class ScriptToken {
	/**
	 * Type of token
	 */
	private final ScriptTokenType type;
	/**
	 * value stored in token
	 */
	private final Object value;

	/**
	 * Initializes <code>ScriptToken</code> with provided parameters
	 * 
	 * @param type  of token
	 * @param value of token
	 */
	public ScriptToken(ScriptTokenType type, Object value) {
		if (type == null)
			throw new IllegalArgumentException("Token type can not be null.");
		this.type = type;
		this.value = value;
	}

	/**
	 * Retrieves value of <code>ScriptToken</code>
	 * 
	 * @return <code>Token</code> value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Retrieves type of the <code>ScriptToken</code>
	 * 
	 * @return type of the <code>ScriptToken</code>
	 */
	public ScriptTokenType getType() {
		return type;
	}
}
