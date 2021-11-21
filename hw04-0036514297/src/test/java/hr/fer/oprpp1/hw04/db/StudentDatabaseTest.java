package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class StudentDatabaseTest {

	StudentDatabase db;

	public StudentDatabaseTest() {
		db = new StudentDatabase(List.of("0000000001	Akšamović	Marin	2", "0000000002	Bakamović	Petra	3",
				"0000000003	Bosnić	Andrea	4", "0000000004	Božić	Marin	5"));
	}

	@Test
	public void testFilterTrue() {
		assertEquals(4, db.filter((record) -> true).size());
	}

	@Test
	public void testFilterFalse() {
		assertEquals(0, db.filter((record) -> false).size());
	}

	@Test
	public void testForJmbag() {
		assertEquals("Andrea", db.forJMBAG("0000000003").getFirstName());
	}
}
