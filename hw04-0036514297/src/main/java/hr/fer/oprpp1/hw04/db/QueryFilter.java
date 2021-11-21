package hr.fer.oprpp1.hw04.db;

import java.util.List;

public class QueryFilter implements IFilter {

	private final List<ConditionalExpression> expressions;

	public QueryFilter(List<ConditionalExpression> expressions) {
		this.expressions = expressions;
	}

	@Override
	public boolean accepts(StudentRecord record) {
		return expressions.stream().allMatch(expr -> 
			expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record), expr.getStringLiteral()
			)
		);
	}

}
