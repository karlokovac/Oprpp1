package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Thrown if something is misused during lexical analysis
 */
public class ScriptLexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ScriptLexerException() {
		super();
	}

	public ScriptLexerException(Throwable cause) {
		super(cause);
	}
}
