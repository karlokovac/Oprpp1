package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/** Data structure for storing key-value pairs */
public class Dictionary<K, V> {

	/** List adapted for internal storage */
	private List<Pair> internal;

	/** Default constructor */
	public Dictionary() {
		internal = new ArrayIndexedCollection<>();
	}

	/**
	 * Checks whether dictionary is empty
	 * 
	 * @return <code>true</code> if empty, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return internal.size() == 0;
	}

	/**
	 * Queries the size of dictionary
	 * 
	 * @return number of stored elements
	 */
	public int size() {
		return internal.size();
	}

	/** Destroys all stored data */
	public void clear() {
		internal.clear();
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
		int index = internal.indexOf(key);
		if (index != -1) {
			return internal.get(index).setValue(value);
		}
		internal.add(new Pair(key, value));
		return null;
	}

	/**
	 * Finds the value of a Pair with the given key, if it exists
	 * 
	 * @param key of a Pair
	 * @return value if the key exists, <code>null</code> otherwise
	 */
	public V get(Object key) {
		int index = internal.indexOf(key);
		if (index != -1)
			return internal.get(index).value;
		return null;
	}

	/**
	 * Removes the Pair with the given key, if it exists
	 * 
	 * @param key of the Pair
	 * @return value of removed Pair, <code>null</code> otherwise
	 */
	public V remove(K key) {
		int index = internal.indexOf(key);
		if (index != -1) {
			V stored = internal.get(index).value;
			internal.remove(index);
			return stored;
		}
		return null;
	}

	/** Data structure representing an entry */
	private class Pair {
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
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (key.getClass() != obj.getClass())
				return false;
			return Objects.equals(key, obj);
		}

	}
}
