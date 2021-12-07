package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDatabase {

	private static final String SAME_JMBAG_MSG = "Student with same JMBAG already exists ";

	/** List of all stored records */
	private final List<StudentRecord> internalList;
	/** Structure for fast record retrieving */
	private final Map<String, StudentRecord> index;

	/** Initializes the database with given entry strings */
	public StudentDatabase(List<String> databaseEntries) {
		internalList = new ArrayList<>(databaseEntries.size());
		index = new HashMap<>();
		for (String line : databaseEntries) {
			StudentRecord rec = StudentRecordParser.parseStudentRecord(line);
			if (index.containsKey(rec.getJmbag()))
				throw new JmbagAlreadyExistsException(SAME_JMBAG_MSG, rec.getJmbag());
			internalList.add(rec);
			index.put(rec.getJmbag(), rec);
		}
	}

	/**
	 * Retrieves the records using index
	 * 
	 * @param jmbag to search
	 * @return record containing given jmbag
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}

	/**
	 * Returns all records that pass the filter
	 * 
	 * @param filter to test records
	 * @return list of records
	 */
	public List<StudentRecord> filter(IFilter filter) {
		return internalList.stream().filter(filter::accepts).collect(Collectors.toList());
	}

	private static class StudentRecordParser {
		/** Constructs StudentRecords from given string entry */
		public static StudentRecord parseStudentRecord(String input) {
			String[] attributes = input.split("\t");
			return new StudentRecord(attributes[0], attributes[2], attributes[1], Integer.parseInt(attributes[3]));
		}
	}
}
