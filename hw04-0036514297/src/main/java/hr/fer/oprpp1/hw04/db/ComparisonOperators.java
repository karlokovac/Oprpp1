package hr.fer.oprpp1.hw04.db;

public class ComparisonOperators {

    public static final IComparisonOperator LESS = (l1, l2) -> l1.compareTo(l2) < 0;
    public static final IComparisonOperator LESS_OR_EQUALS = (l1, l2) -> l1.compareTo(l2) <= 0;
    public static final IComparisonOperator GREATER = (l1, l2) -> !LESS_OR_EQUALS.satisfied(l1, l2);
    public static final IComparisonOperator GREATER_OR_EQUALS = (l1, l2) -> !LESS.satisfied(l1, l2);
    public static final IComparisonOperator EQUALS = String::equals;
    public static final IComparisonOperator NOT_EQUALS = (l1, l2) -> !EQUALS.satisfied(l1, l2);
    public static final IComparisonOperator LIKE = (l1, l2) -> l1
            .matches(l2.replaceFirst("[*]", "[a-zA-Z0-9čćšđžČĆŠĐŽ]*"));

}
