package hr.fer.oprpp1.hw04.db;

public class ConditionalExpression {
	private final IFieldValueGetter fieldGetter;
	private final String stringLiteral;
	private final IComparisonOperator comparisonOperator;

	/** Constructs the expression */
	public ConditionalExpression(IFieldValueGetter getter, String literal, IComparisonOperator operator) {
		this.fieldGetter = getter;
		this.stringLiteral = literal;
		this.comparisonOperator = operator;
	}

	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	public String getStringLiteral() {
		return stringLiteral;
	}

	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

}
