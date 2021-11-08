package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class ListTestingUtil {

	protected List<Object> collection;

	protected abstract List<Object> createInstance();

	@BeforeEach
	public void setup() {
		collection = createInstance();
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
	public void testClear() {
		collection.add("Fiat");
		collection.clear();
		assertTrue(collection.isEmpty());
	}

	@Test
	public void testInsertWhenEmpty() {
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac };
		collection.insert(rimac, 0);
		assertArrayEquals(expected, collection.toArray());
	}

	@Test
	public void testFirstWhenNotEmpty() {
		var tesla = "Tesla";
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac, tesla };
		collection.add(tesla);
		collection.insert(rimac, 0);
		assertArrayEquals(expected, collection.toArray());
	}

	@Test
	public void testLastWhenNotEmpty() {
		var tesla = "Tesla";
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac, tesla };
		collection.add(rimac);
		collection.insert(tesla, 1);
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
