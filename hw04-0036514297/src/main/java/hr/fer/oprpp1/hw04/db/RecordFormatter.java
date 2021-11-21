package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

public class RecordFormatter {
	private final static int JMBAG_LENGTH = 10;
	private final static int FINAL_GRADE_LENGTH = 1;

	private final static String JMBAG_DASHES = "=".repeat(JMBAG_LENGTH + 2);
	private final static String FINAL_GRADE_DASHES = "=".repeat(FINAL_GRADE_LENGTH + 2);

	public static List<String> format(List<StudentRecord> records) {
		if (records.size() == 0)
			return List.of("Records selected: 0");
		final var longest = new LongestNames();
		records.stream().forEach((rec) -> longest.update(rec));
		var output = new ArrayList<String>();
		final var firstNameDashes = "=".repeat(longest.firstName + 2);
		final var lastNameDashes = "=".repeat(longest.lastName + 2);
		final var border = String.format("+%s+%s+%s+%s+", JMBAG_DASHES, lastNameDashes, firstNameDashes,
				FINAL_GRADE_DASHES);
		output.add(border);
		var outputFormat = "| %-" + JMBAG_LENGTH + "s | %-" + longest.lastName + "s | %-" + longest.firstName + "s | %-"
				+ FINAL_GRADE_LENGTH + "s |";
		for (var rec : records)
			output.add(String.format(outputFormat, rec.getJmbag(), rec.getLastName(), rec.getFirstName(),
					rec.getFinalGrade()));
		output.add(border);
		output.add("Records selected: " + records.size());
		return output;
	}

	private static class LongestNames {
		public int firstName = 0;
		public int lastName = 0;

		public void update(StudentRecord rec) {
			firstName = Math.max(firstName, rec.getFirstName().length());
			lastName = Math.max(lastName, rec.getLastName().length());
		}
	}
}
