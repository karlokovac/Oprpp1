package hr.fer.oprpp1.hw04.db;

@FunctionalInterface
public interface IFieldValueGetter {
	String get(StudentRecord record);
}
