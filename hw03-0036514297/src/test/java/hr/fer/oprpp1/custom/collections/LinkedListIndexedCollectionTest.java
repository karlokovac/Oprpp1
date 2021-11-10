package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest extends ListTestingUtil {

	protected List<Object> createInstance() {
		return new LinkedListIndexedCollection<>();
	}

	@Test
	public void testDefaultConstructor() {
		var collection = new LinkedListIndexedCollection<>();
		assertTrue(collection.isEmpty());
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
		var col = new LinkedListIndexedCollection<>(list);
		assertArrayEquals(expected, col.toArray());
	}

	@Test
	public void testCollectionConstructorShouldThrow() {
		assertThrows(NullPointerException.class, () -> {
			new LinkedListIndexedCollection<>(null);
		});
	}

}
