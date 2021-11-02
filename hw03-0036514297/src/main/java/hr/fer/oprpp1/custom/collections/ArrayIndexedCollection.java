package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

public class ArrayIndexedCollection<T> implements List<T> {

	/** Constant indicating no presence of value */
	private static final int VALUE_IS_NOT_FOUND = -1;
	/** Default size of internal array */
	private static final int DEFAULT_CAPACITY = 16;
	/** Minimal size of internal array */
	private static final int MIN_SIZE = 1;

	/** Current size of collection */
	private int size;

	/** An array of object references storing elements */
	private T[] elements;

	private long modificationCount;

	/** Default constructor setting capacity to default size */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructor with arbitrary size
	 * 
	 * @param initialCapacity of the array
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < MIN_SIZE)
			throw new IllegalArgumentException("Initial capacity can't be less than " + MIN_SIZE);
		elements = (T[]) new Object[initialCapacity];
	}

	/**
	 * Constructor for supplying another <code>Collection</code> to be added
	 * 
	 * @param other <code>Collection</code> to be added
	 */
	public ArrayIndexedCollection(Collection<? extends T> other) {
		this(other, MIN_SIZE);
	}

	/**
	 * Constructor for supplying another <code>Collection</code> along with initial
	 * capacity to be used for memory allocation
	 * 
	 * @param other           <code>Collection</code> to be added
	 * @param initialCapacity of the array
	 * @throws NullPointerException if <code>other</code> is null
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
		requireNonNull(other, "Reference to other collection must not be null");
		size = other.size();
		elements = (T[]) Arrays.copyOf(other.toArray(), Math.max(initialCapacity, size));
	}

	/** @throws NullPointerException if <code>value</code> is <code>null</code> */
	@Override
	public void add(T value) {
		requireNonNull(value, "Can't add null");
		reallocateIfFull();
		elements[size++] = value;
	}

	@Override
	public T get(int index) {
		checkIndex(index, size);
		return elements[index];
	}

	@Override
	public void clear() {
		Arrays.fill(elements, null);
		size = 0;
		modificationCount++;
	}

	@Override
	public void insert(T value, int position) {
		checkIndex(position, size + 1);
		requireNonNull(value, "Can't insert null");
		reallocateIfFull();
		shiftRightFrom(position);
		elements[position] = value;
		size++;
	}

	@Override
	public int indexOf(Object value) {
		if (value != null)
			for (int i = 0; i < size; i++)
				if (elements[i].equals(value))
					return i;
		return VALUE_IS_NOT_FOUND;
	}

	@Override
	public void remove(int index) {
		checkIndex(index, size);
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
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index != VALUE_IS_NOT_FOUND) {
			remove(index);
			return true;
		}
		return false;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	@Override
	public void forEach(Processor<? super T> processor) {
		for (int i = 0; i < size; i++)
			processor.process(elements[i]);
	}

	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new Getter<T>(this);
	}

	/** Duplicates backing array if it's full */
	private void reallocateIfFull() {
		if (size == elements.length) {
			elements = Arrays.copyOf(elements, elements.length * 2);
			modificationCount++;
		}
	}

	/**
	 * Shifts elements in the array to the right from the starting position to end
	 * 
	 * @param position starting position
	 */
	private void shiftRightFrom(int position) {
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		modificationCount++;
	}

	/**
	 * Shifts elements in the array to the left from the starting position to end
	 * 
	 * @param position starting position
	 */
	private void shiftLeftFrom(int position) {
		for (int i = position; i < size; i++) {
			elements[i] = elements[i + 1];
		}
		modificationCount++;
	}

	/**
	 * Implementation of <code>ElementsGetter</code> for
	 * <code>ArrayIndexedCollection</code>
	 */
	private static class Getter<T> implements ElementsGetter<T> {
		private int index;
		private final long savedModificationCount;
		private final ArrayIndexedCollection<T> collection;

		/**
		 * Constructor using the <code>ArrayIndexedCollection</code> reference to
		 * instance <code>ElementsGetter</code> for it
		 * 
		 * @param collection <code>ArrayIndexedCollection</code> of which
		 *                   <code>Getter</code> is instantiated
		 */
		public Getter(ArrayIndexedCollection<T> collection) {
			this.collection = collection;
			this.savedModificationCount = collection.modificationCount;
		}

		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != collection.modificationCount)
				throw new ConcurrentModificationException("Unexpected modification while iterating");
			return index < collection.size;
		}

		@Override
		public T getNextElement() {
			if (!hasNextElement())
				throw new NoSuchElementException("No element to get");
			return collection.elements[index++];
		}
	}

}
