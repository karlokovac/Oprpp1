package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

public class ArrayIndexedCollection implements List {

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
	 * Current size of collection
	 */
	private int size;

	/**
	 * An array of object references which length determines its current capacity
	 */
	private Object[] elements;

	private long modificationCount;

	/**
	 * Default constructor setting capacity to default size
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructor with arbitrary size
	 * 
	 * @param initialCapacity of the array
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < MIN_SIZE)
			throw new IllegalArgumentException();
		elements = new Object[initialCapacity];
	}

	/**
	 * Constructor for supplying another <code>Collection</code> to be added
	 * 
	 * @param other <code>Collection</code> to be added
	 */
	public ArrayIndexedCollection(Collection other) {
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
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		requireNonNull(other);
		size = other.size();
		elements = Arrays.copyOf(other.toArray(), Math.max(initialCapacity, size));
	}

	/**
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		requireNonNull(value);
		reallocateIfFull();
		elements[size++] = value;
	}

	/**
	 * Returns the object that is stored in backing array at position index. Valid
	 * indexes are 0 to size-1
	 * 
	 * @param index of the position
	 * @return <code>Object</code> to be returned
	 * @throws IndexOutOfBoundsException if <code>index</code> is misused
	 */
	public Object get(int index) {
		checkIndex(index, size);
		return elements[index];
	}

	@Override
	public void clear() {
		Arrays.fill(elements, null);
		size = 0;
		modificationCount++;
	}

	/**
	 * Inserts (does not overwrite) the given value at the given position in array.
	 * The legal positions are 0 to <code>size</code> (both are included)
	 * 
	 * @param value    to be inserted
	 * @param position to be placed at
	 * @throws NullPointerException      if <code>value</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if position is misused
	 */
	public void insert(Object value, int position) {
		checkIndex(position, size + 1);
		requireNonNull(value);
		reallocateIfFull();
		shiftRightFrom(position);
		elements[position] = value;
		size++;
	}

	/**
	 * Searches the collection and returns the index of the first occurrence of the
	 * given value or -1 if the value is not found
	 * 
	 * @param value to be searched for
	 * @return position of the element
	 */
	public int indexOf(Object value) {
		if (value != null)
			for (int i = 0; i < elements.length; i++)
				if (value.equals(elements[i]))
					return i;
		return VALUE_IS_NOT_FOUND;
	}

	/**
	 * Removes element at specified index from collection. Element that was
	 * previously at location <code>index+1</code> after this operation is on
	 * location index , etc. Legal indexes are 0 to <code>size-1</code>
	 * 
	 * @param index position of the element
	 * @throws IndexOutOfBoundsException if <code>index</code> is misused
	 */
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
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++)
			processor.process(elements[i]);
	}

	@Override
	public ElementsGetter createElementsGetter() {
		return new Getter(this);
	}

	/**
	 * Duplicates backing array if it's full
	 */
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

	private static class Getter implements ElementsGetter {
		private int index;
		private final long savedModificationCount;
		private final ArrayIndexedCollection collection;

		public Getter(ArrayIndexedCollection collection) {
			this.collection = collection;
			this.savedModificationCount = collection.modificationCount;
		}

		@Override
		public boolean hasNextElement() {
			return index < collection.size;
		}

		@Override
		public Object getNextElement() {
			checkConcurrentModification();
			checkNextElement();
			return collection.elements[index++];
		}

		private void checkNextElement() {
			if (!hasNextElement())
				throw new NoSuchElementException("No element to get");
		}

		private void checkConcurrentModification() {
			if (savedModificationCount != collection.modificationCount)
				throw new ConcurrentModificationException();
		}
	}

}
