package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArrayIndexedCollectionTest {

	@Test
	public void testIfNewEmpty() {
		var collection = new ArrayIndexedCollection();
		assertTrue(collection.isEmpty());
	}

	@Test
	public void testAdding() {
		var collection = new ArrayIndexedCollection();
		collection.add("Seat");
		assertTrue(collection.contains("Seat"));
	}

	@Test
	public void testAddingNull() {
		var collection = new ArrayIndexedCollection();
		assertThrows(NullPointerException.class, () -> collection.add(null));
	}

	@Test
	public void testGet() {
		var collection = new ArrayIndexedCollection();
		String expected = "Hyundai";
		collection.add(expected);
		assertEquals(expected, collection.get(0));
	}

	@Test
	public void testGetWrongIndex() {
		var collection = new ArrayIndexedCollection();
		String expected = "Hyundai";
		collection.add(expected);
		assertThrows(IndexOutOfBoundsException.class, () -> collection.get(1));
	}

	@Test
	public void testGetWhenReallocated() {
		var collection = new ArrayIndexedCollection(2);
		String expected = "Hyundai";
		collection.add("Mazda");
		collection.add("Honda");
		collection.add("Toyota");
		collection.add(expected);
		assertEquals(expected, collection.get(3));
	}

	@Test
	public void testClear() {
		var collection = new ArrayIndexedCollection();
		collection.add("Fiat");
		collection.clear();
		assertTrue(collection.isEmpty());
	}
	
	@Test
	public void testInsert() {
		var collection = new ArrayIndexedCollection();
		var expected = "Rimac";
		collection.add(expected);
		collection.insert("Tesla", 0);
		assertEquals(expected,collection.get(1));
	}
}
