package hr.fer.oprpp1.custom.collections;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class LinkedListIndexedCollection<T> implements List<T> {

	/** Constant indicating value isn't found */
	private static final int VALUE_IS_NOT_FOUND = -1;
	/** Current size of collection */
	private int size;
	/** Reference to the first node of the linked list */
	private ListNode<T> first;
	/** Reference to the last node of the linked list */
	private ListNode<T> last;
	/** Keeps track of modifications preformed */
	private long modificationCount;

	/** Default constructor */
	public LinkedListIndexedCollection() {
		size = 0;
		first = null;
		last = null;
	}

	/**
	 * Constructor with <code>Collection</code> to add elements from
	 * 
	 * @param other <code>Collection</code> to add elements from
	 * @throws NullPointerException if <code>other</code> is null
	 */
	public LinkedListIndexedCollection(Collection<? extends T> other) {
		this();
		requireNonNull(other, "Collection reference musn't be null");
		addAll(other);
	}

	/** @throws NullPointerException if <code>value</code> is <code>null</code> */
	@Override
	public void add(T value) {
		requireNonNull(value, "Can't add null");
		append(value);
	}

	@Override
	public T get(int index) {
		return getNode(checkIndex(index, size)).value;
	}

	@Override
	public void clear() {
		first = null;
		last = null;
		size = 0;
		modificationCount++;
	}

	@Override
	public void insert(T value, int position) {
		requireNonNull(value);
		checkIndex(position, size + 1);

		if (position == size)
			append(value);
		else if (position == 0) {
			prepend(value);
		} else {
			insertInTheMiddle(value, position);
		}
	}

	@Override
	public int indexOf(Object value) {
		if (value != null) {
			var node = first;
			for (int i = 0; i < size; i++, node = node.next)
				if (node.value.equals(value))
					return i;
		}
		return VALUE_IS_NOT_FOUND;
	}

	@Override
	public void remove(int index) {
		removeNode(getNode(checkIndex(index, size)));
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
		Object[] array = new Object[size];
		ListNode<T> node = first;
		for (int i = 0; i < size; i++, node = node.next)
			array[i] = node.value;
		return array;
	}

	@Override
	public void forEach(Processor<? super T> processor) {
		for (var node = first; node != null; node = node.next) {
			processor.process(node.value);
		}
	}

	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new Getter<T>(this);
	}

	/**
	 * Add element to the end of linked list
	 * 
	 * @param value to be added
	 */
	private void append(T value) {
		var element = new ListNode<T>(last, null, value);
		if (isEmpty())
			first = element;
		else
			last.next = element;
		last = element;
		size++;
		modificationCount++;
	}

	/**
	 * Add element to the beginning of linked list
	 * 
	 * @param value to be added
	 */
	private void prepend(T value) {
		ListNode<T> element = new ListNode<>(null, first, value);
		if (isEmpty())
			last = element;
		else
			first.previous = element;
		first = element;
		size++;
		modificationCount++;
	}

	/**
	 * Add element in the middle of two other existing elements. There <b>must</b>
	 * an element before and after it
	 * 
	 * @param value    to be inserted
	 * @param position to be inserted at
	 */
	private void insertInTheMiddle(T value, int position) {
		var node = getNode(position);
		var inserted = new ListNode<>(node.previous, node, value);
		node.previous.next = inserted;
		node.previous = inserted;
		size++;
		modificationCount++;
	}

	/**
	 * Retrieve the node at given index
	 * 
	 * @param index at which node is found
	 * @return node to be retrieved
	 */
	private ListNode<T> getNode(int index) {
		ListNode<T> node;
		if (index <= size / 2) {
			node = first;
			for (int i = 0; i < index; i++)
				node = node.next;
		} else {
			node = last;
			for (int i = size; i > index; i--)
				node = node.previous;
		}
		return node;
	}

	/**
	 * Remove node from linked list
	 * 
	 * @param node to be removed
	 */
	private void removeNode(ListNode<T> node) {
		if (size == 1)
			clear();
		else {
			if (node == first) {
				node.next.previous = null;
				first = node.next;
			} else if (node == last) {
				node.previous.next = null;
				last = node.previous;
			} else {
				node.previous.next = node.next;
				node.next.previous = node.previous;
			}
			size--;
		}
		modificationCount++;
	}

	/** Element of linked list */
	private static class ListNode<T> {
		private ListNode<T> previous;
		private ListNode<T> next;
		private T value;

		public ListNode(ListNode<T> prev, ListNode<T> next, T value) {
			this.previous = prev;
			this.next = next;
			this.value = value;
		}
	}

	/** Implementation of ElementsGetter for {@link LinkedListIndexedCollection} */
	private static class Getter<T> implements ElementsGetter<T> {

		private ListNode<T> node;
		private final long savedModificationCount;
		private final LinkedListIndexedCollection<T> collection;

		public Getter(LinkedListIndexedCollection<T> collection) {
			this.collection = collection;
			this.node = collection.first;
			this.savedModificationCount = collection.modificationCount;

		}

		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != collection.modificationCount)
				throw new ConcurrentModificationException("Unexpected modification during iteration");
			return node != null;
		}

		@Override
		public T getNextElement() {
			if (!hasNextElement())
				throw new NoSuchElementException("No next element to iterate");
			var next = node;
			node = node.next;
			return next.value;
		}

	}

}
