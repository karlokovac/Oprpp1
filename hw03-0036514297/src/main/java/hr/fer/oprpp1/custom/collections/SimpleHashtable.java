package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Data structure storing key-value pairs
 * 
 * @param <K> key data type
 * @param <V> value data type
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/** Word with every bit set to one */
	private final static int ALL_BITS_ONE = -1;
	/** Default capacity of the hash table */
	private final static int DEFAULT_CAPACITY = 16;
	/** Threshold for doubling the array */
	private final static float FULLNESS_THRESHOLD = 0.75f;

	/** Array storing <code>TableEntry</code> pairs */
	private TableEntry<K, V>[] table;
	/** Counts the number of present pairs */
	private int size;
	/** Counts modifications done on hash table */
	private long modificationCount = 0;

	/** Constructs the table with default capacity */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructs the table with the given capacity
	 * 
	 * @param capacity of the table
	 * @throws IllegalArgumentException if capacity is less then 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException();
		int twoExponent = ALL_BITS_ONE >>> Integer.numberOfLeadingZeros(capacity - 1);
		twoExponent = (twoExponent < 0) ? 1 : twoExponent + 1;
		table = (TableEntry<K, V>[]) new TableEntry[twoExponent];
	}

	/**
	 * Puts a key-value pair into dictionary. If key already exists it overwrites
	 * the old value with the new one and returns the old one
	 * 
	 * @param key   of the pair
	 * @param value of the pair
	 * @return old value of the pair, otherwise <code>null</code>
	 * @throws NullPointerException if the key is <code>null</code>
	 */
	public V put(K key, V value) {
		Objects.requireNonNull(key);
		V oldValue = insert(key, value);
		if (oldValue == null) {
			size++;
			modificationCount++;
			if ((float) size / table.length >= FULLNESS_THRESHOLD)
				reallocateTable();
		}
		return oldValue;
	}

	/**
	 * Finds the value of a Pair with the given key, if it exists
	 * 
	 * @param key of a Pair
	 * @return value if the key exists, <code>null</code> otherwise
	 */
	public V get(Object key) {
		if (key == null)
			return null;
		var entry = queryKey(key);
		if (entry != null)
			return entry.value;
		return null;
	}

	/**
	 * Queries the number of stored pairs
	 * 
	 * @return the number of stored pairs
	 */
	public int size() {
		return size;
	}

	/**
	 * Queries if the given key is present in the table
	 * 
	 * @param key to be queried
	 * @return true if key is present
	 */
	public boolean containsKey(Object key) {
		return queryKey(key) != null;
	}

	/**
	 * Queries if the given value is present in the table
	 * 
	 * @param value to be queried
	 * @return true if key is present
	 */
	public boolean containsValue(Object value) {
		for (var entry : table) {
			for (; entry != null; entry = entry.next)
				if (entry.value.equals(value))
					return true;
		}
		return false;
	}

	/**
	 * Removes the Pair with the given key, if it exists
	 * 
	 * @param key of the Pair
	 * @return value of removed Pair, <code>null</code> otherwise
	 */
	public V remove(Object key) {
		if (key == null)
			return null;
		return removeEntry(key);
	}

	/**
	 * Queries whether the table is empty
	 * 
	 * @return <code>true</code> if it is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the array containing all present key-value pairs
	 * 
	 * @return reference to the created array
	 */
	@SuppressWarnings("unchecked")
	public TableEntry<K, V>[] toArray() {
		TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[size];
		int index = 0;
		for (var entry : table) {
			for (; entry != null; entry = entry.next)
				array[index++] = entry;
		}
		return array;
	}

	/** Removes all present pairs in the table */
	public void clear() {
		Arrays.fill(table, null);
		size = 0;
		modificationCount++;
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new SimpleHashtableIterator();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (var entry : table)
			for (; entry != null; entry = entry.next)
				builder.append(entry.key).append("=").append(entry.value).append(", ");
		int lastIndex = builder.lastIndexOf(", ");
		if (lastIndex != -1)
			builder.delete(lastIndex, builder.length());
		return builder.append("]").toString();
	}

	/**
	 * Calculates the slot for a given key. Since the capacity is a power of two,
	 * modulo operation can be significantly more efficiently implemented by reading
	 * all the bits behind the one which is present in size
	 * 
	 * @param key witches slot is calculated
	 * @return slot where it is stored
	 */
	private int indexOf(Object key) {
		return key.hashCode() & (table.length - 1);
	}

	/**
	 * Searches the table for an entry containing the given key
	 * 
	 * @param key to be searched
	 * @return pair containing the key
	 */
	private TableEntry<K, V> queryKey(Object key) {
		if (key != null) {
			var entry = table[indexOf(key)];
			for (; entry != null; entry = entry.next) {
				if (entry.key.equals(key))
					return entry;
			}
		}
		return null;
	}

	/**
	 * Inserts the given key-value pair. If the key is present, it overwrites the
	 * old value with the new one
	 * 
	 * @param key   of the pair
	 * @param value of the pair
	 * @return old value if it was present, <code>null</code> otherwise
	 */
	private V insert(K key, V value) {
		var slot = indexOf(key);
		var entry = table[slot];
		if (entry == null) {
			table[slot] = new TableEntry<>(key, value, null);
			return null;
		}

		boolean equals;
		while ((equals = entry.key.equals(key)) || entry.next != null) {
			if (equals) {
				var oldValue = entry.value;
				entry.value = value;
				return oldValue;
			}
			entry = entry.next;
		}

		entry.next = new TableEntry<>(key, value, null);
		return null;
	}

	/** Reallocates the array, doubling the size */
	@SuppressWarnings("unchecked")
	private void reallocateTable() {
		TableEntry<K, V>[] content = toArray();
		table = (TableEntry<K, V>[]) new TableEntry[table.length << 1];
		for (var entry : content)
			insert(entry.key, entry.value);
	}

	/**
	 * Removes the pair containing given key, if such is present
	 * 
	 * @param key of the pair
	 * @return old value if it was present, <code>null</code> otherwise
	 */
	private V removeEntry(Object key) {
		int slot = indexOf(key);
		var entry = table[slot];
		if (entry.key.equals(key)) {
			V value = entry.value;
			table[slot] = entry.next;
			size--;
			modificationCount++;
			return value;
		}

		for (; entry.next != null; entry = entry.next) {
			if (entry.next.key.equals(key)) {
				var oldValue = entry.next.value;
				entry.next = entry.next.next;
				size--;
				modificationCount++;
				return oldValue;
			}
		}
		return null;
	}

	/** Implementation of the iterator */
	private class SimpleHashtableIterator implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		/** Modification count used to detect concurrent modification */
		private long savedModificationCount;
		/** Current index of the table array while iterating */
		private int index;
		/** Reference to the last returned <code>TableEntry</code> */
		private TableEntry<K, V> lastReturned;
		/** Next entry to be returned */
		private TableEntry<K, V> entry;

		/** Default constructor */
		public SimpleHashtableIterator() {
			savedModificationCount = modificationCount;
			index = -1;
			assignNextOccupiedSlot();
		}

		@Override
		public boolean hasNext() {
			checkModificationCount();
			return entry != null;
		}

		@Override
		public TableEntry<K, V> next() {
			if (!hasNext())
				throw new NoSuchElementException();
			lastReturned = entry;
			assignNextElement();
			return lastReturned;
		}

		@Override
		public void remove() {
			checkModificationCount();
			if (lastReturned == null || !containsKey(lastReturned.key))
				throw new IllegalStateException();
			removeEntry(lastReturned.key);
			savedModificationCount++;
		}

		/** Sets the entry field to the next pair in the table */
		private void assignNextElement() {
			if (lastReturned.next != null)
				entry = lastReturned.next;
			else
				assignNextOccupiedSlot();
		}

		/** Sets the entry field to the next head of occupied slot */
		private void assignNextOccupiedSlot() {
			for (index++; index < table.length; index++) {
				if (table[index] != null) {
					entry = table[index];
					return;
				}
			}
			entry = null;
		}

		/** Checks whether modification counts match */
		private void checkModificationCount() {
			if (savedModificationCount != modificationCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
	 * Model of the stored key-value pair
	 *
	 * @param <K> key type
	 * @param <V> value type
	 */
	public static class TableEntry<K, V> {
		private final K key;
		private V value;
		private TableEntry<K, V> next;

		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/** key getter */
		public K getKey() {
			return key;
		}

		/** value getter */
		public V getValue() {
			return value;
		}

		/** value setter */
		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.format("%s=%s", key, value);
		}
	}
}
