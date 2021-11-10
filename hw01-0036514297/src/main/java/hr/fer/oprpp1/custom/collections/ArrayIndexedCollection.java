package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

/** Data structure for storying Objects in array */
public class ArrayIndexedCollection extends Collection {

	/** Constant indicating no presence of value */
	private static final int VALUE_IS_NOT_FOUND = -1;
	/** Default size of internal array */
	private static final int DEFAULT_CAPACITY = 16;
	/** Minimal size of internal array */
	private static final int MIN_SIZE = 1;

	private static final String INIT_CAP_TOO_SMALL_MSG = "Initial capacity can't be less than " + MIN_SIZE;
	private static final String NULL_REF_COLLECTION_MSG = "Collection can't be a null reference";
	private static final String NULL_REF_VAL_MSG = "Value can't be null reference";

	/** Current size of collection */
	private int size;
	/** An array of object references */
	private Object[] elements;

	/** Default constructor setting capacity to default size */
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
			throw new IllegalArgumentException(INIT_CAP_TOO_SMALL_MSG);
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
		requireNonNull(other, NULL_REF_COLLECTION_MSG);
		size = other.size();
		elements = Arrays.copyOf(other.toArray(), Math.max(initialCapacity, size));
	}

	/** @throws NullPointerException if <code>value</code> is <code>null</code> */
	@Override
	public void add(Object value) {
		insert(value, size);
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
		return elements[checkIndex(index, size)];
	}

	@Override
	public void clear() {
		Arrays.fill(elements, null);
		size = 0;
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
		requireNonNull(value, NULL_REF_VAL_MSG);
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
		if (index == VALUE_IS_NOT_FOUND)
			return false;
		remove(index);
		return true;
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

	/** Duplicates backing array if it's full */
	private void checkSize() {
		if (size == elements.length)
			elements = Arrays.copyOf(elements, elements.length * 2);
	}

	/**
	 * Shifts elements in the array to the right from the starting position to end.
	 * If <code>elements</code> is full it duplicates the array so it wouldn't
	 * assign out of bounds. This is ok since it is always used when filling array
	 * 
	 * @param position starting position
	 */
	private void shiftRightFrom(int position) {
		checkSize();
		for (int i = size; i > position; i--)
			elements[i] = elements[i - 1];
	}

	/**
	 * Shifts elements in the array to the left from the starting position to end.
	 * <b>Overwrites</b> element at <code>position</code>
	 * 
	 * @param position starting position
	 */
	private void shiftLeftFrom(int position) {
		for (int i = position + 1; i < size; i++)
			elements[i - 1] = elements[i];
		elements[size - 1] = null;
	}

}
