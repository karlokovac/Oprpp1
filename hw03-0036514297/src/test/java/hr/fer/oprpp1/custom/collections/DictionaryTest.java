package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DictionaryTest {

	Dictionary<String, String> dictionary;

	@BeforeEach
	public void setup() {
		dictionary = new Dictionary<>();
	}

	@Test
	public void testEmpty() {
		assertTrue(dictionary.isEmpty());
	}

	@Test
	public void testPutNull() {
		assertThrows(NullPointerException.class, () -> dictionary.put(null, null));
	}

	@Test
	public void testPut() {
		var value = "Value";
		var actual = dictionary.put("Key", value);
		assertEquals(null, actual);
		assertEquals(value, dictionary.get("Key"));
	}

	@Test
	public void testPutOverOld() {
		dictionary.put("Key", "OldValue");
		var value = "Value";
		dictionary.put("Key", value);
		assertEquals(value, dictionary.get("Key"));
	}

	@Test
	public void testRemove() {
		var value = "Value";
		dictionary.put("Key", value);
		dictionary.remove("Key");
		assertTrue(dictionary.isEmpty());
	}

	@Test
	public void testSize() {
		dictionary.put("Key", "Value");
		dictionary.put("Key", null);
		assertEquals(1, dictionary.size());
		dictionary.put("Ivica", "Marica");
		assertEquals(2, dictionary.size());
	}
}
