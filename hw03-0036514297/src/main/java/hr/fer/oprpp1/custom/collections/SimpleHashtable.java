package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	private static final int ALL_BITS_ONE = -1;
	private final static int DEFAULT_CAPACITY = 16;
	private final static float FULLNESS_THRESHOLD = 0.75f;

	private TableEntry<K, V>[] table;
	private int size;
	private long modificationCount = 0;

	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException();
		int twoExponent = ALL_BITS_ONE >>> Integer.numberOfLeadingZeros(capacity - 1);
		twoExponent = (twoExponent < 0) ? 1 : twoExponent + 1;
		table = (TableEntry<K, V>[]) new TableEntry[twoExponent];
	}

	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		Objects.requireNonNull(key);
		V oldValue = insert(key, value);
		if (oldValue == null) {
			size++;
			modificationCount++;
			if ((float) size / table.length >= FULLNESS_THRESHOLD) {
				TableEntry<K, V>[] content = toArray();
				table = (TableEntry<K, V>[]) new TableEntry[table.length << 1];
				for (var entry : content)
					insert(entry.key, entry.value);
			}
		}
		return oldValue;
	}

	public V get(Object key) {
		if (key == null)
			return null;
		var entry = queryKey(key);
		if (entry != null)
			return entry.value;
		return null;
	}

	public int size() {
		return size;
	}

	public boolean containsKey(Object key) {
		return queryKey(key) != null;
	}

	public boolean containsValue(Object value) {
		for (var entry : table) {
			for (; entry != null; entry = entry.next)
				if (entry.value.equals(value))
					return true;
		}
		return false;
	}

	public V remove(Object key) {
		if (key == null)
			return null;
		return removeEntry(key);
	}

	public boolean isEmpty() {
		return size == 0;
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

	public void clear() {
		Arrays.fill(table, null);
		size = 0;
		modificationCount++;
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new SimpleHashtableIterator();
	}

	private int indexOf(Object key) {
		return key.hashCode() & (table.length - 1);
	}

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

	private class SimpleHashtableIterator implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		private long savedModificationCount;
		private int index;
		private TableEntry<K, V> lastReturned, entry;

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

		private void assignNextElement() {
			if (lastReturned.next != null)
				entry = lastReturned.next;
			else
				assignNextOccupiedSlot();
		}

		private void assignNextOccupiedSlot() {
			for (index++; index < table.length; index++) {
				if (table[index] != null) {
					entry = table[index];
					return;
				}
			}
			entry = null;
		}

		private void checkModificationCount() {
			if (savedModificationCount != modificationCount)
				throw new ConcurrentModificationException();
		}
	}

	public static class TableEntry<K, V> {
		private final K key;
		private V value;
		private TableEntry<K, V> next;

		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public String toString() {
			return String.format("%s=%s", key, value);
		}
	}
}
