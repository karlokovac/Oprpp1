package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArrayIndexedCollectionTest {

	private ArrayIndexedCollection<Object> collection;
	
	@BeforeEach
	public void setup() {
		collection = new ArrayIndexedCollection<>();
	}
	
	@Test
	public void testDefaultConstructor() {
		var collection = new ArrayIndexedCollection<>();
		assertTrue(collection.isEmpty());
	}

	@Test
	public void testCapacityConstructor() {
		var collection = new ArrayIndexedCollection<>(2);
		assertTrue(collection.isEmpty());
	}

	@Test
	public void testCapacityConstructorShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ArrayIndexedCollection<>(0);
		});
	}

	@Test
	public void testCollectionConstructor() {
		var volkswagen = "Volkswagen";
		var mercedes = "Mercedes";
		var bmw = "BMW";
		Object[] expected = new Object[] { volkswagen, mercedes, bmw };
		collection.add(volkswagen);
		collection.add(mercedes);
		collection.add(bmw);
		var col = new ArrayIndexedCollection<>(collection);
		assertArrayEquals(expected, col.toArray());
	}

	@Test
	public void testCollectionConstructorShouldThrow() {
		assertThrows(NullPointerException.class, () -> {
			new ArrayIndexedCollection<Object>(null);
		});
	}
	
	@Test
	public void testCollectionAndCapacityConstructor() {
		var col = new ArrayIndexedCollection<Object>();
		var volkswagen = "Volkswagen";
		var mercedes = "Mercedes";
		var bmw = "BMW";
		Object[] expected = new Object[] { volkswagen, mercedes, bmw };
		col.add(volkswagen);
		col.add(mercedes);
		col.add(bmw);
		var collection = new ArrayIndexedCollection<>(col,0);
		assertArrayEquals(expected, collection.toArray());
	}

	@Test
	public void testAdding() {
		collection.add("Seat");
		assertTrue(collection.contains("Seat"));
	}

	@Test
	public void testAddingNull() {
		assertThrows(NullPointerException.class, () -> collection.add(null));
	}

	@Test
	public void testGet() {
		String expected = "Hyundai";
		collection.add(expected);
		assertEquals(expected, collection.get(0));
	}

	@Test
	public void testGetWrongIndex() {
		String expected = "Hyundai";
		collection.add(expected);
		assertThrows(IndexOutOfBoundsException.class, () -> collection.get(1));
	}

	@Test
	public void testGetWhenReallocated() {
		String expected = "Hyundai";
		collection.add("Mazda");
		collection.add("Honda");
		collection.add("Toyota");
		collection.add(expected);
		assertEquals(expected, collection.get(3));
	}

	@Test
	public void testClear() {
		collection.add("Fiat");
		collection.clear();
		assertTrue(collection.isEmpty());
	}

	@Test
	public void testInsertOne() {
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac };
		collection.insert(rimac, 0);
		assertArrayEquals(expected, collection.toArray());
	}

	@Test
	public void testInsertInThMiddle() {
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
		var shelby = "Shelby";
		collection.add("Chevy");
		collection.add(shelby);
		assertEquals(1, collection.indexOf(shelby));
	}

	@Test
	public void testRemove() {
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
