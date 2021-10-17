package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest {

	@Test
	public void testDefaultConstructor() {
		var collection = new LinkedListIndexedCollection();
		assertTrue(collection.isEmpty());
	}

	@Test
	public void testCollectionConstructor() {
		var col = new LinkedListIndexedCollection();
		var volkswagen = "Volkswagen";
		var mercedes = "Mercedes";
		var bmw = "BMW";
		Object[] expected = new Object[] { volkswagen, mercedes, bmw };
		col.add(volkswagen);
		col.add(mercedes);
		col.add(bmw);
		var collection = new LinkedListIndexedCollection(col);
		assertArrayEquals(expected, collection.toArray());
	}

	@Test
	public void testCollectionConstructorShouldThrow() {
		assertThrows(NullPointerException.class, () -> {
			new LinkedListIndexedCollection(null);
		});
	}

	@Test
	public void testAdding() {
		var collection = new LinkedListIndexedCollection();
		collection.add("Seat");
		assertTrue(collection.contains("Seat"));
	}

	@Test
	public void testAddingNull() {
		var collection = new LinkedListIndexedCollection();
		assertThrows(NullPointerException.class, () -> collection.add(null));
	}

	@Test
	public void testGet() {
		var collection = new LinkedListIndexedCollection();
		String expected = "Hyundai";
		collection.add(expected);
		assertEquals(expected, collection.get(0));
	}

	@Test
	public void testGetWrongIndex() {
		var collection = new LinkedListIndexedCollection();
		String expected = "Hyundai";
		collection.add(expected);
		assertThrows(IndexOutOfBoundsException.class, () -> collection.get(1));
	}

	@Test
	public void testClear() {
		var collection = new LinkedListIndexedCollection();
		collection.add("Fiat");
		collection.clear();
		assertTrue(collection.isEmpty());
	}

	@Test
	public void testInsertOne() {
		var collection = new LinkedListIndexedCollection();
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac };
		collection.insert(rimac, 0);
		assertArrayEquals(expected, collection.toArray());
	}

	@Test
	public void testInsertInThMiddle() {
		var collection = new LinkedListIndexedCollection();
		var tesla = "Tesla";
		var porsche = "Porsche";
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac, tesla, porsche };
		collection.add(rimac);
		collection.add(porsche);
		collection.insert(tesla, 1);
		assertArrayEquals(expected, collection.toArray());
	}
	
	@Test
	public void testIndexOf() {
		var collection = new LinkedListIndexedCollection();
		var shelby = "Shelby";
		collection.add("Chevy");
		collection.add(shelby);
		assertEquals(1, collection.indexOf(shelby));
	}

	@Test
	public void testRemove() {
		var collection = new LinkedListIndexedCollection();
		var tesla = "Tesla";
		var rimac = "Rimac";
		var porsche = "Porsche";
		Object[] expected = new Object[] { rimac, porsche };
		collection.add(tesla);
		collection.add(rimac);
		collection.add(porsche);
		collection.remove(0);
		assertArrayEquals(expected, collection.toArray());
	}

}
