package hr.fer.oprpp1.hw04.db;

@FunctionalInterface
public interface IComparisonOperator {
	boolean satisfied(String value1, String value2);
}
