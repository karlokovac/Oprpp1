package hr.fer.oprpp1.hw04.db;

import java.util.List;

public record QueryFilter(List<ConditionalExpression> expressions) implements IFilter {

	/**
	 * Tests if record passes all ContitionalExpressions
	 *
	 * @param record to test
	 * @return true if satisfies all
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		return expressions.stream().allMatch(expr -> expr.getComparisonOperator()
				.satisfied(expr.getFieldGetter().get(record), expr.getStringLiteral()));
	}

}
