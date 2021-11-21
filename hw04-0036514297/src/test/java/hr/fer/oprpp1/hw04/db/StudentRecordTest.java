package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class StudentRecordTest {

	@Test
	public void testFinalGradeOutOfRange() {
		assertThrows(FinalGradeException.class, () -> new StudentRecord("0", "name", "surname", 0));
	}
}
