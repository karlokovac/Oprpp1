package hr.fer.oprpp1.hw04.db;

@FunctionalInterface
public interface IFieldValueGetter {
	/**
	 * Fetches the field from StudentRecord
	 * 
	 * @param record to fetch field from
	 * @return field from record
	 */
	String get(StudentRecord record);
}
