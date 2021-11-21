package hr.fer.oprpp1.hw04.db;

@FunctionalInterface
public interface IFilter {
	boolean accepts(StudentRecord record);
}
