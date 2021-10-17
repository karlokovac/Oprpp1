package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

public class ArrayIndexedCollection extends Collection {

	/**
	 * Current size of collection
	 */
	private int size;

	/**
	 * An array of object references which length determines its current capacity
	 */
	private Object[] elements;

	/**
	 * Constant indicating no presence of value
	 */
	private static final int VALUE_IS_NOT_FOUND = -1;
	/**
	 * Default size of internal array 
	 */
	private static final int DEFAULT_CAPACITY = 16;
	/**
	 * Minimal size of internal array
	 */
	private static final int MIN_SIZE = 1;

	/**
	 * Default constructor setting capacity to default size
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructor with arbitrary size
	 * @param initialCapacity of the array
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < MIN_SIZE)
			throw new IllegalArgumentException();
		elements = new Object[initialCapacity];
	}

	public ArrayIndexedCollection(Collection other) {
		this(other, MIN_SIZE);
	}

	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		requireNonNull(other);
		size = other.size();
		elements = Arrays.copyOf(other.toArray(), Math.max(initialCapacity, size));
	}

	@Override
	public void add(Object value) {
		requireNonNull(value);
		reallocateIfFull();
		elements[size++] = value;
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
		checkIndex(position, size + 1);
		requireNonNull(value);
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
		for (int i = 0; i < size; i++)
			processor.process(elements[i]);
	}

	private void reallocateIfFull() {
		if (size == elements.length)
			elements = Arrays.copyOf(elements, elements.length * 2);
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
