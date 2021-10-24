package hr.fer.oprpp1.custom.scripting.lexer;

public class Token {
	private final TokenType type;
	private final Object value;

	public Token(TokenType type, Object value) {
		if (type == null)
			throw new IllegalArgumentException("Token type can not be null.");
		this.type = type;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public TokenType getType() {
		return type;
	}
}
