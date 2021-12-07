package hr.fer.oprpp1.hw04.db;

public class FinalGradeException extends RuntimeException {

	public final String jmbag;
	public final int finalGrade;

	public FinalGradeException(String message, String jmbag, int finalGrade) {
		super(message);
		this.jmbag = jmbag;
		this.finalGrade = finalGrade;
	}
}
