package hr.fer.oprpp1.hw04.db;

@FunctionalInterface
public interface IComparisonOperator {
	/**
	 * Checks if values satisfy the operator
	 * 
	 * @param value1 first operand
	 * @param value2 second operand
	 * @return true if operator is satisfied
	 */
	boolean satisfied(String value1, String value2);
}
