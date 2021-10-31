package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

public class Dictionary<K, V> {

	private List<Pair> internal;

	public Dictionary() {
		internal = new ArrayIndexedCollection<>();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return internal.size();
	}

	public void clear() {
		internal.clear();
	}

	public V put(K key, V value) {
		Objects.requireNonNull(key);
		int index = internal.indexOf(key);
		if (index != -1) {
			Pair stored = internal.get(index);
			V oldValue = stored.value;
			stored.setValue(value);
			return oldValue;
		}
		internal.add(new Pair(key, value));
		return null;
	}

	public V get(Object key) {
		int index = internal.indexOf(key);
		if (index != -1)
			return internal.get(index).value;
		return null;
	}

	public V remove(K key) {
		int index = internal.indexOf(key);
		if (index != -1) {
			V stored = internal.get(index).value;
			internal.remove(index);
			return stored;
		}
		return null;
	}

	private class Pair {
		private final K key;
		private V value;

		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

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
