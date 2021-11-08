package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleHashtableTest {

	private SimpleHashtable<String, String> table;

	@BeforeEach
	public void setup() {
		table = new SimpleHashtable<>();
	}

	@Test
	public void testConstructorWrongArg() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<>(0));
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
		table = new SimpleHashtable<>(1);
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
		table = new SimpleHashtable<>(1);
		fillTable();
		assertEquals("Haso", table.remove("Mujo"));
		assertEquals(2, table.size());
		assertEquals(null, table.get("Mujo"));
		assertEquals("Donald", table.get("Knuth"));
	}

	@Test
	public void testRemoveLast() {
		table = new SimpleHashtable<>(1);
		fillTable();
		assertEquals("Donald", table.remove("Knuth"));
		assertEquals(2, table.size());
		assertEquals(null, table.get("Knuth"));
	}

	@Test
	public void testToStringEmpty() {
		assertEquals("[]", table.toString());
	}

	@Test
	public void testToString1() {
		table.put("Key", "Value");
		assertEquals("[Key=Value]", table.toString());
	}

	@Test
	public void checkMultipleRemove() {
		fillTable();
		var it = table.iterator();
		it.next();
		it.remove();
		assertThrows(IllegalStateException.class, () -> it.remove());
	}

	@Test
	public void checkModificationCount() {
		fillTable();
		var it = table.iterator();
		it.next();
		table.clear();
		assertThrows(ConcurrentModificationException.class, () -> it.next());
		assertThrows(ConcurrentModificationException.class, () -> it.hasNext());
		assertThrows(ConcurrentModificationException.class, () -> it.remove());
	}

	@Test
	public void checkMultipleIteratorsModifying() {
		fillTable();
		var it1 = table.iterator();
		var it2 = table.iterator();
		it1.next();
		it1.remove();
		assertThrows(ConcurrentModificationException.class, () -> it2.next());
		assertThrows(ConcurrentModificationException.class, () -> it2.hasNext());
		assertThrows(ConcurrentModificationException.class, () -> it2.remove());
	}

	private void fillTable() {
		table.put("Key", "Value");
		table.put("Mujo", "Haso");
		table.put("Knuth", "Donald");
		System.out.println(table);
		// System.out.println(Arrays.toString(table.toArray()));
	}
}
