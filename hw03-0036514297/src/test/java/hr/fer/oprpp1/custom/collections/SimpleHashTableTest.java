package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleHashTableTest {

	private SimpleHashTable<String, String> table;

	@BeforeEach
	public void setup() {
		table = new SimpleHashTable<>();
	}

	@Test
	public void testConstructorWrongArg() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashTable<>(0));
	}

	@Test
	public void testPutting() {
		table.put("Key", "Value");
		assertEquals(1, table.size());
		assertEquals("Value", table.get("Key"));
	}

	@Test
	public void testPuttingOver() {
		table.put("Key", "Value");
		table.put("Key", "Val");
		assertEquals(1, table.size());
		assertEquals("Val", table.get("Key"));
	}

	@Test
	public void testPuttingMultipleInSameSlot() {
		table = new SimpleHashTable<>(1);
		fillTable();
		assertEquals(3, table.size());
		assertEquals("Donald", table.get("Knuth"));
	}

	@Test
	public void testContainsKey() {
		fillTable();
		assertTrue(table.containsKey("Knuth"));
	}

	@Test
	public void testContainsValue() {
		fillTable();
		assertTrue(table.containsValue("Donald"));
	}

	@Test
	public void testRemoveFirst() {
		fillTable();
		assertEquals("Value", table.remove("Key"));
		assertEquals(2, table.size());
	}

	@Test
	public void testRemoveMiddle() {
		table = new SimpleHashTable<>(1);
		fillTable();
		assertEquals("Haso", table.remove("Mujo"));
		assertEquals(2, table.size());
		assertEquals(null, table.get("Mujo"));
		assertEquals("Donald", table.get("Knuth"));
	}

	@Test
	public void testRemoveLast() {
		table = new SimpleHashTable<>(1);
		fillTable();
		assertEquals("Donald", table.remove("Knuth"));
		assertEquals(2, table.size());
		assertEquals(null, table.get("Knuth"));
	}

	@Test
	public void testToStringEmpty() {
		assertEquals("[]", table.toString());
	}

	private void fillTable() {
		table.put("Key", "Value");
		table.put("Mujo", "Haso");
		table.put("Knuth", "Donald");
	}
}
