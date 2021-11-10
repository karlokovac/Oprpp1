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
	/** Minimum capacity of the table */
	private static final int MIN_CAPACITY = 1;

	private static final String INIT_CAP_TOO_SMALL_MSG = "Initial capacity can't be less than " + MIN_CAPACITY;
	private static final String NULL_REF_KEY_MSG = "Key must not be null reference";

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
		if (capacity < MIN_CAPACITY)
			throw new IllegalArgumentException(INIT_CAP_TOO_SMALL_MSG);
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
		Objects.requireNonNull(key, NULL_REF_KEY_MSG);
		V oldValue = insert(key, value);
		if (oldValue == null) {
			size++;
			if ((float) size / table.length >= FULLNESS_THRESHOLD)
				reallocateTable();
		}
		modificationCount++;
		return oldValue;
	}

	/**
	 * Finds the value of a Pair with the given key, if it exists
	 * 
	 * @param key of a Pair
	 * @return value if the key exists, <code>null</code> otherwise
	 */
	public V get(Object key) {
		var entry = queryKey(key);
		return entry == null ? null : entry.value;
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
		for (var entry : this)
			if (entry.value.equals(value))
				return true;
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

		var entry = table[slotFor(key)];
		var prevEntry = entry;
		while (entry != null && !entry.key.equals(key)) {
			prevEntry = entry;
			entry = entry.next;
		}

		if (entry == null)
			return null;

		var oldValue = entry.value;
		if (entry != prevEntry)
			prevEntry.next = entry.next;
		else
			table[slotFor(key)] = entry.next;
		size--;
		modificationCount++;
		return oldValue;
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
		for (var entry : this)
			array[index++] = entry;
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
		if (isEmpty())
			return "[]";
		final StringBuilder builder = new StringBuilder().append("[");
		final var iterator = iterator();
		builder.append(iterator.next());
		iterator.forEachRemaining((e) -> builder.append(", ").append(e.key).append("=").append(e.value));
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
	private int slotFor(Object key) {
		return key.hashCode() & (table.length - 1);
	}

	/**
	 * Searches the table for an entry containing the given key
	 * 
	 * @param key to be searched
	 * @return pair containing the key
	 */
	private TableEntry<K, V> queryKey(Object key) {
		if (key != null)
			for (var entry = table[slotFor(key)]; entry != null; entry = entry.next)
				if (entry.key.equals(key))
					return entry;
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
		var entry = table[slotFor(key)];
		if (entry == null) {
			table[slotFor(key)] = new TableEntry<>(key, value, null);
			return null;
		}
		var prevEntry = entry;
		while (entry != null && !entry.key.equals(key)) {
			prevEntry = entry;
			entry = entry.next;
		}

		if (entry != null) {
			var oldValue = entry.value;
			entry.value = value;
			return oldValue;
		}

		prevEntry.next = new TableEntry<>(key, value, null);
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

	/** Implementation of the iterator */
	private class SimpleHashtableIterator implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		private static final String ILLEGAL_REMOVE = "Illegal remove call while iterating";
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

		/** @throws ConcurrentModificationException unexpected modification occurred */
		@Override
		public boolean hasNext() {
			checkModificationCount();
			return entry != null;
		}

		/**
		 * @throws ConcurrentModificationException unexpected modification occurred
		 * @throws NoSuchElementException          if there is no next element
		 */
		@Override
		public TableEntry<K, V> next() {
			if (!hasNext())
				throw new NoSuchElementException("No next element for iteration");
			lastReturned = entry;
			assignNextElement();
			return lastReturned;
		}

		/**
		 * @throws IllegalStateException           if remove was inappropriately called
		 * @throws ConcurrentModificationException if unexpected modification occurred
		 */
		@Override
		public void remove() {
			checkModificationCount();
			if (lastReturned == null || !containsKey(lastReturned.key))
				throw new IllegalStateException(ILLEGAL_REMOVE);
			SimpleHashtable.this.remove(lastReturned.key);
			savedModificationCount++;
		}

		/**
		 * Checks whether modification counts match
		 * 
		 * @throws ConcurrentModificationException if unexpected modification occurred
		 */
		private void checkModificationCount() {
			if (savedModificationCount != modificationCount)
				throw new ConcurrentModificationException("Unexpected modification occured while iterating");
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
			do {
				index++;
			} while (index < table.length && table[index] == null);
			if (index < table.length)
				entry = table[index];
			else
				entry = null;
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
