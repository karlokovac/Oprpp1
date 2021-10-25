package hr.fer.oprpp1.hw02.prob1;

/**
 * Thrown if something is misused during lexical analysis
 */
public class LexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LexerException() {
		super();
	}

	public LexerException(Throwable cause) {
		super(cause);
	}
}
