package hr.fer.oprpp1.hw04.db;

@FunctionalInterface
public interface IFilter {
	/**
	 * Tests if record is accepted
	 * 
	 * @param record to test
	 * @return true if it is accepted
	 */
	boolean accepts(StudentRecord record);
}
