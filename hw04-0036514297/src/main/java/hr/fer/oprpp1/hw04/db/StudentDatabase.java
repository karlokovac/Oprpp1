package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDatabase {

	private static final String SAME_JMBAG_MSG = "Student with same JMBAG already exists ";

	private List<StudentRecord> internalList;
	private Map<String, StudentRecord> index;

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

	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}

	public List<StudentRecord> filter(IFilter filter) {
		return internalList.stream().filter(filter::accepts).collect(Collectors.toList());
	}

	private static class StudentRecordParser {
		public static final StudentRecord parseStudentRecord(String input) {
			String[] attributes = input.split("\t");
			return new StudentRecord(attributes[0], attributes[2], attributes[1], Integer.valueOf(attributes[3]));
		}
	}
}
