package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

public class SimpleHashTable<K, V> {

	private static final int ALL_BITS_ONE = -1;
	private final static int DEFAULT_CAPACITY = 16;

	private final TableEntry<K, V>[] table;
	private int size;

	public SimpleHashTable() {
		this(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public SimpleHashTable(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException();
		int twoExponent = ALL_BITS_ONE >>> Integer.numberOfLeadingZeros(capacity - 1);
		twoExponent = (twoExponent < 0) ? 1 : twoExponent + 1;
		table = (TableEntry<K, V>[]) new TableEntry[twoExponent];
	}

	public V put(K key, V value) {
		Objects.requireNonNull(key);
		var slot = indexOf(key);
		var entry = table[slot];
		if (entry == null) {
			table[slot] = new TableEntry<>(key, value, null);
			size++;
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
		size++;
		return null;
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

		int slot = indexOf(key);
		var entry = table[slot];
		if (entry.key.equals(key)) {
			V value = entry.value;
			table[slot] = entry.next;
			size--;
			return value;
		}

		for (; entry.next != null; entry = entry.next) {
			if (entry.next.key.equals(key)) {
				var oldValue = entry.next.value;
				entry.next = entry.next.next;
				size--;
				return oldValue;
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (var entry : table) {
			for (; entry != null; entry = entry.next)
				builder.append(entry.key).append("=").append(entry.value).append(", ");
		}
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
	}
}
