package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

public class ArrayIndexedCollection extends Collection {
	private int size;
	private Object[] elements;

	private static final int VALUE_IS_NOT_FOUND = -1;
	private static final int DEFAULT_CAPACITY = 16;
	private static final int MIN_SIZE = 1;

	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < MIN_SIZE)
			throw new IllegalArgumentException();
		elements = new Object[initialCapacity];
	}

	public ArrayIndexedCollection(Collection other) {
		this(other, 0);
	}

	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		requireNonNull(other);
		elements = Arrays.copyOf(other.toArray(), other.size() > initialCapacity ? other.size() : initialCapacity);
	}

	@Override
	public void add(Object value) {
		requireNonNull(value);
		reallocateIfFull();
		addToFirstEmpty(value);
		size++;
	}

	public Object get(int index) {
		checkIndex(index, size);
		return elements[index];
	}

	@Override
	public void clear() {
		Arrays.fill(elements, null);
		size = 0;
	}

	public void insert(Object value, int position) {
		requireNonNull(value);
		checkIndex(position, size + 1);
		reallocateIfFull();
		shiftRightFrom(position);
		elements[position] = value;
		size++;
	}

	public int indexOf(Object value) {
		if (value != null)
			for (int i = 0; i < elements.length; i++)
				if (value.equals(elements[i]))
					return i;
		return VALUE_IS_NOT_FOUND;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index != VALUE_IS_NOT_FOUND) {
			remove(index);
			return true;
		}
		return false;
	}

	public void remove(int index) {
		elements[index] = null;
		shiftLeftFrom(index);
		size--;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) != VALUE_IS_NOT_FOUND;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	@Override
	public void forEach(Processor processor) {
	}

	private void reallocateIfFull() {
		if (size == elements.length)
			elements = Arrays.copyOf(elements, elements.length * 2);
	}

	private void addToFirstEmpty(Object value) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null) {
				elements[i] = value;
				return;
			}
		}
	}

	private void shiftRightFrom(int position) {
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
	}

	private void shiftLeftFrom(int position) {
		for (int i = position; i < size; i++) {
			elements[i] = elements[i + 1];
		}
	}
}
