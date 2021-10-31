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
		var volkswagen = "Volkswagen";
		var mercedes = "Mercedes";
		var bmw = "BMW";
		Object[] expected = new Object[] { volkswagen, mercedes, bmw };
		collection.add(volkswagen);
		collection.add(mercedes);
		collection.add(bmw);
		var col = new ArrayIndexedCollection<>(collection, 0);
		assertArrayEquals(expected, col.toArray());
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

}
