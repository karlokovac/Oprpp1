package hr.fer.oprpp1.custom.collections;

import java.util.Objects;
import java.util.function.Function;

/** Data structure for storing key-value pairs */
public class Dictionary<K, V> {

	/** List adapted for internal storage */
	private final List<Pair<K, V>> internalList;

	/** Default constructor */
	public Dictionary() {
		internalList = new ArrayIndexedCollection<>();
	}

	/**
	 * Checks whether dictionary is empty
	 * 
	 * @return <code>true</code> if empty, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return internalList.isEmpty();
	}

	/**
	 * Queries the size of dictionary
	 * 
	 * @return number of stored elements
	 */
	public int size() {
		return internalList.size();
	}

	/** Destroys all stored data */
	public void clear() {
		internalList.clear();
	}

	/**
	 * Puts a key-value pair into dictionary. If key already exists it overwrites
	 * the old value with the new one and returns the old one
	 * 
	 * @param key   of Pair
	 * @param value of Pair
	 * @return old value if it was present, <code>null</code> otherwise
	 * @throws NullPointerException if key is null
	 */
	public V put(K key, V value) {
		Objects.requireNonNull(key, "Key musn't be null");
		var overWritten = findKeyAndPreform(key, (pair) -> pair.setValue(value));
		if (overWritten != null)
			return overWritten;
		internalList.add(new Pair<K, V>(key, value));
		return null;
	}

	/**
	 * Finds the value of a Pair with the given key, if it exists
	 * 
	 * @param key of a Pair
	 * @return value if the key exists, <code>null</code> otherwise
	 */
	public V get(Object key) {
		return findKeyAndPreform(key, (pair) -> pair.value);
	}

	/**
	 * Removes the Pair with the given key, if it exists
	 * 
	 * @param key of the Pair
	 * @return value of removed Pair, <code>null</code> otherwise
	 */
	public V remove(K key) {
		return findKeyAndPreform(key, (pair) -> {
			final V value = pair.value;
			internalList.remove(pair);
			return value;
		});
	}

	/**
	 * Iterates over stored pairs until it finds the one with the matching key.If it
	 * finds the matching key then it performs the action on it. Otherwise it
	 * returns <code>null</code>
	 * 
	 * @param key    to be searched
	 * @param action to be performed
	 * @return result of the performed function
	 */
	private V findKeyAndPreform(Object key, Function<Pair<K, V>, V> action) {
		if (key == null)
			return null;
		var eg = internalList.createElementsGetter();
		while (eg.hasNextElement()) {
			var pair = eg.getNextElement();
			if (pair.key.equals(key))
				return action.apply(pair);
		}
		return null;
	}

	/** Data structure representing an entry */
	private static class Pair<K, V> {
		private final K key;
		private V value;

		/**
		 * Constructor with Pair elements
		 * 
		 * @param key   of Pair
		 * @param value of Pair
		 */
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * value setter function
		 * 
		 * @param newValue to be set
		 * @return old value being overwritten
		 */
		public V setValue(V newValue) {
			final V oldValue = value;
			value = newValue;
			return oldValue;
		}

	}
}
