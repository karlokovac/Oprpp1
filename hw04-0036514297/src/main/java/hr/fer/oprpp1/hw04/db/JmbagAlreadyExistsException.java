package hr.fer.oprpp1.hw04.db;

public class JmbagAlreadyExistsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final String jmbag;

	public JmbagAlreadyExistsException(String message, String jmbag) {
		super(message);
		this.jmbag = jmbag;
	}
}
