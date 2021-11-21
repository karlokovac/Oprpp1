package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ComparisonOperatorsTest {

	@Test
	public void testLess() {
		assertTrue(ComparisonOperators.LESS.satisfied("Ana", "Jasna"));
		assertFalse(ComparisonOperators.LESS.satisfied("Anamarija", "Ana"));
	}

	@Test
	public void testLessOrEquals() {
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Ana", "Ana"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Ana", "Anamarija"));
		assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("Anamarija", "Ana"));
	}

	@Test
	public void testGreater() {
		assertTrue(ComparisonOperators.GREATER.satisfied("Zagreb", "Ankara"));
		assertFalse(ComparisonOperators.GREATER.satisfied("Zabok", "Zagreb"));
	}

	@Test
	public void testGreaterOrEquals() {
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Zagreb", "Ankara"));
		assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Zabok", "Zagreb"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Zagreb", "Zagreb"));
	}

	@Test
	public void testEquals() {
		assertTrue(ComparisonOperators.EQUALS.satisfied("Dubrovnik", "Dubrovnik"));
		assertFalse(ComparisonOperators.EQUALS.satisfied("Dubrovnik", "Dubrovnikk"));
	}

	@Test
	public void testLike() {
		assertFalse(ComparisonOperators.LIKE.satisfied("Zagreb", "Aba*"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Ban", "B*"));
		assertTrue(ComparisonOperators.LIKE.satisfied("0035", "0*5"));
	}
}
