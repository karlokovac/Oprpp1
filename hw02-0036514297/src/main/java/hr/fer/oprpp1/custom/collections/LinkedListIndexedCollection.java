package hr.fer.oprpp1.custom.collections;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class LinkedListIndexedCollection implements List {

	private static final String NULL_REF_COLLECTION_MSG = "Collection can't be a null reference";
	private static final String NULL_REF_VAL_MSG = "Value can't be null reference";

	/** Current size of collection */
	private int size;
	/** Reference to the first node of the linked list */
	private ListNode first;
	/** Reference to the last node of the linked list */
	private ListNode last;

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
	public LinkedListIndexedCollection(Collection other) {
		this();
		requireNonNull(other, NULL_REF_COLLECTION_MSG);
		addAll(other);
	}

	/** @throws NullPointerException if <code>value</code> is <code>null</code> */
	@Override
	public void add(Object value) {
		requireNonNull(value, NULL_REF_VAL_MSG);
		append(value);
	}

	@Override
	public Object get(int index) {
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
	public void insert(Object value, int position) {
		requireNonNull(value, NULL_REF_VAL_MSG);
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
		return VALUE_NOT_FOUND;
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
		Object[] array = new Object[size];
		ListNode node = first;
		for (int i = 0; i < size; i++, node = node.next)
			array[i] = node.value;
		return array;
	}

	@Override
	public void forEach(Processor processor) {
		for (var node = first; node != null; node = node.next)
			processor.process(node.value);
	}

	@Override
	public ElementsGetter createElementsGetter() {
		return new Getter(this);
	}

	/**
	 * Add element to the end of linked list
	 * 
	 * @param value to be added
	 */
	private void append(Object value) {
		ListNode element = new ListNode(last, null, value);
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
	private void prepend(Object value) {
		ListNode element = new ListNode(null, first, value);
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
	private void insertInTheMiddle(Object value, int position) {
		var node = getNode(position);
		var inserted = new ListNode(node.previous, node, value);
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
	private ListNode getNode(int index) {
		ListNode node;
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
	private void removeNode(ListNode node) {
		if (size == 1) {
			clear();
			return;
		}

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
		modificationCount++;
	}

	/** Implementation of ElementsGetter for {@link LinkedListIndexedCollection} */
	private static class Getter implements ElementsGetter {

		private ListNode node;
		private final long savedModificationCount;
		private final LinkedListIndexedCollection collection;

		public Getter(LinkedListIndexedCollection collection) {
			this.collection = collection;
			this.node = collection.first;
			this.savedModificationCount = collection.modificationCount;

		}

		/** @throws ConcurrentModificationException if modification occured */
		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != collection.modificationCount)
				throw new ConcurrentModificationException();
			return node != null;
		}

		/** @throws NoSuchElementException if there is no next element */
		@Override
		public Object getNextElement() {
			if (!hasNextElement())
				throw new NoSuchElementException();
			var next = node;
			node = node.next;
			return next.value;
		}
	}

	/** Element of linked list */
	private static class ListNode {
		private ListNode previous;
		private ListNode next;
		private Object value;

		public ListNode(ListNode prev, ListNode next, Object value) {
			this.previous = prev;
			this.next = next;
			this.value = value;
		}
	}
}
