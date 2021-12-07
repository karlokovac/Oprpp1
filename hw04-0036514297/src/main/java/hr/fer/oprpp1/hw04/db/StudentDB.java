package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/** Entering program */
public class StudentDB {

	/** Reference to database instance */
	private static StudentDatabase database;

	public static void main(String[] args) {
		loadDatabase(loadFileLines());
		var sc = new Scanner(System.in);
		for (var line = readFromConsole(sc); !"exit".equalsIgnoreCase(line); line = readFromConsole(sc)) {
			QueryParser parser = parse(line);
			if (parser != null)
				queryAndOutput(parser);
		}
		sc.close();
		System.out.println("Goodbye!");
	}

	/** Prompts the user to enter commands */
	private static String readFromConsole(Scanner sc) {
		System.out.print("> ");
		return sc.nextLine();
	}

	/** Takes input string and parses it */
	private static QueryParser parse(String line) {
		try {
			return new QueryParser(line);
		} catch (ParserException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	/**
	 * Queries the database and outputs result to console
	 * 
	 * @param parser to extract query from
	 */
	private static void queryAndOutput(QueryParser parser) {
		List<StudentRecord> records;
		if (parser.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");
			records = List.of(database.forJMBAG(parser.getQueriedJMBAG()));
		} else
			records = database.filter(new QueryFilter(parser.getQuery()));
		RecordFormatter.format(records).forEach(System.out::println);
	}

	/**
	 * Reads data from database file and parses it to a list
	 * 
	 * @return string list
	 */
	private static List<String> loadFileLines() {
		try {
			return Files.readAllLines(Paths.get("./database.txt"), StandardCharsets.UTF_8);
		} catch (IOException | SecurityException ex) {
			System.out.println("An error occured while reading the file");
			System.exit(1);
		}
		return null;
	}

	/**
	 * Inputs list elements into the database
	 * 
	 * @param inputFileLines lines from the input file
	 */
	private static void loadDatabase(List<String> inputFileLines) {
		try {
			database = new StudentDatabase(inputFileLines);
		} catch (JmbagAlreadyExistsException ex) {
			System.out.println(ex.getMessage() + " " + ex.jmbag);
			System.exit(2);
		} catch (FinalGradeException ex) {
			System.out.println(ex.getMessage() + " " + ex.jmbag + " " + ex.finalGrade);
			System.exit(3);
		}
	}
}
