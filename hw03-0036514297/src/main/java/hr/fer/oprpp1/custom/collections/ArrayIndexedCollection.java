package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

public class ArrayIndexedCollection<T> implements List<T> {

	/** Default size of internal array */
	private static final int DEFAULT_CAPACITY = 16;
	/** Minimal size of internal array */
	private static final int MIN_SIZE = 1;
	private static final String INIT_CAP_TOO_SMALL_MSG = "Initial capacity can't be less than " + MIN_SIZE;
	private static final String NULL_REF_COLLECTION_MSG = "Collection can't be a null reference";
	private static final String NULL_REF_VAL_MSG = "Value can't be null reference";

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
			throw new IllegalArgumentException(INIT_CAP_TOO_SMALL_MSG);
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
		requireNonNull(other, NULL_REF_COLLECTION_MSG);
		size = other.size();
		elements = (T[]) Arrays.copyOf(other.toArray(), Math.max(initialCapacity, size));
	}

	/** @throws NullPointerException if <code>value</code> is <code>null</code> */
	@Override
	public void add(T value) {
		insert(value, size);
	}

	@Override
	public T get(int index) {
		return elements[checkIndex(index, size)];
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
		requireNonNull(value, NULL_REF_VAL_MSG);
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
		return VALUE_NOT_FOUND;
	}

	@Override
	public void remove(int index) {
		checkIndex(index, size);
		shiftLeftFrom(index);
		size--;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) != VALUE_NOT_FOUND;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index == VALUE_NOT_FOUND)
			return false;
		remove(index);
		return true;
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
	private void checkSize() {
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
		checkSize();
		for (int i = size; i > position; i--)
			elements[i] = elements[i - 1];
		modificationCount++;
	}

	/**
	 * Shifts elements in the array to the left from the starting position to end
	 * 
	 * @param position starting position
	 */
	private void shiftLeftFrom(int position) {
		for (int i = position + 1; i < size; i++)
			elements[i - 1] = elements[i];
		elements[size - 1] = null;
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

		/** @throws ConcurrentModificationException if modification occurred */
		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != collection.modificationCount)
				throw new ConcurrentModificationException("Unexpected modification while iterating");
			return index < collection.size;
		}

		/**
		 * @throws ConcurrentModificationException if modification occurred
		 * @throws NoSuchElementException          if there is no next element
		 */
		@Override
		public T getNextElement() {
			if (!hasNextElement())
				throw new NoSuchElementException("No element to get");
			return collection.elements[index++];
		}
	}

}
