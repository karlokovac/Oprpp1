package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArrayIndexedCollectionTest extends ListTestingUtil {

	@Override
	public List<Object> createInstance() {
		return new ArrayIndexedCollection<>();
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
		list.add(volkswagen);
		list.add(mercedes);
		list.add(bmw);
		var col = new ArrayIndexedCollection<>(list);
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
		var volkswagen = "Volkswagen";
		var mercedes = "Mercedes";
		var bmw = "BMW";
		Object[] expected = new Object[] { volkswagen, mercedes, bmw };
		list.add(volkswagen);
		list.add(mercedes);
		list.add(bmw);
		var col = new ArrayIndexedCollection<>(list, 0);
		assertArrayEquals(expected, col.toArray());
	}

	@Test
	public void testGetWhenReallocated() {
		String expected = "Hyundai";
		list.add("Mazda");
		list.add("Honda");
		list.add("Toyota");
		list.add(expected);
		assertEquals(expected, list.get(3));
	}

}
