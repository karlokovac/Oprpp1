package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

public class StudentRecord {
	private static final String FINAL_GRADE_ERROR_MSG = "Final grade must be a value between 1 and 5,both included ";

	private final String jmbag;
	private final String firstName;
	private final String lastName;
	private final int finalGrade;

	/**
	 * Constructs the StudentRecord
	 * 
	 * @param jmbag
	 * @param firstName
	 * @param lastName
	 * @param finalGrade
	 * @throws FinalGradeException if finalGrade is invalid
	 */
	public StudentRecord(String jmbag, String firstName, String lastName, int finalGrade) {
		super();
		this.jmbag = jmbag;
		this.firstName = firstName;
		this.lastName = lastName;
		this.finalGrade = finalGrade;

		if (finalGrade < 1 || finalGrade > 5)
			throw new FinalGradeException(FINAL_GRADE_ERROR_MSG, this.jmbag, this.finalGrade);
	}

	public String getJmbag() {
		return jmbag;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getFinalGrade() {
		return finalGrade;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return Objects.equals(jmbag, ((StudentRecord) obj).jmbag);
	}

	@Override
	public String toString() {
		return "StudentRecord [jmbag=" + jmbag + ", firstName=" + firstName + ", lastName=" + lastName + ", finalGrade="
				+ finalGrade + "]";
	}

}
