package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class ListTestingUtil {

	protected List list;

	protected abstract List createInstance();

	@BeforeEach
	public void setup() {
		list = createInstance();
	}

	@Test
	public void testAdding() {
		list.add("Seat");
		assertTrue(list.contains("Seat"));
	}

	@Test
	public void testAddingNull() {
		assertThrows(NullPointerException.class, () -> list.add(null));
	}

	@Test
	public void testGet() {
		String expected = "Hyundai";
		list.add(expected);
		assertEquals(expected, list.get(0));
	}

	@Test
	public void testGetWrongIndex() {
		String expected = "Hyundai";
		list.add(expected);
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
	}

	@Test
	public void testClear() {
		list.add("Fiat");
		list.clear();
		assertTrue(list.isEmpty());
	}

	@Test
	public void testInsertWhenEmpty() {
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac };
		list.insert(rimac, 0);
		assertArrayEquals(expected, list.toArray());
	}

	@Test
	public void testFirstWhenNotEmpty() {
		var tesla = "Tesla";
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac, tesla };
		list.add(tesla);
		list.insert(rimac, 0);
		assertArrayEquals(expected, list.toArray());
	}

	@Test
	public void testLastWhenNotEmpty() {
		var tesla = "Tesla";
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac, tesla };
		list.add(rimac);
		list.insert(tesla, 1);
		assertArrayEquals(expected, list.toArray());
	}

	@Test
	public void testInsertInThMiddle() {
		var tesla = "Tesla";
		var porsche = "Porsche";
		var rimac = "Rimac";
		Object[] expected = new Object[] { rimac, tesla, porsche };
		list.add(rimac);
		list.add(porsche);
		list.insert(tesla, 1);
		assertArrayEquals(expected, list.toArray());
	}

	@Test
	public void testIndexOf() {
		var shelby = "Shelby";
		list.add("Chevy");
		list.add(shelby);
		assertEquals(1, list.indexOf(shelby));
	}

	@Test
	public void testRemove() {
		var tesla = "Tesla";
		var rimac = "Rimac";
		var porsche = "Porsche";
		Object[] expected = new Object[] { rimac, porsche };
		list.add(tesla);
		list.add(rimac);
		list.add(porsche);
		list.remove(0);
		assertArrayEquals(expected, list.toArray());
	}

	@Test
	public void testRemoveWhenFull() {
		Object[] expected = new Object[15];
		list.add(16);
		for (int i = 0; i < 15; i++) {
			list.add(i);
			expected[i] = i;
		}
		list.remove(0);
		assertArrayEquals(expected, list.toArray());
	}

}
