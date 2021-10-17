package hr.fer.oprpp1.custom.collections;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;

public class LinkedListIndexedCollection extends Collection {
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

	/**
	 * Constant indicating value isn't found
	 */
	private static final int VALUE_IS_NOT_FOUND = -1;

	/**
	 * Current size of collection
	 */
	private int size;
	/**
	 * Reference to the first node of the linked list
	 */
	private ListNode first;
	/**
	 * Reference to the last node of the linked list
	 */
	private ListNode last;

	/**
	 * Default constructor
	 */
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
		requireNonNull(other);
		addAll(other);
	}

	/**
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		requireNonNull(value);
		append(value);
	}

	/**
	 * Returns the object that is stored in linked list at position index. Valid
	 * indexes are 0 to <code>size-1</code>
	 * 
	 * @param index
	 * @return <code>Object</code> to be retrieved
	 * @throws IndexOutOfBoundsException if index is missused
	 */
	public Object get(int index) {
		checkIndex(index, size);
		return getNode(index).value;
	}

	@Override
	public void clear() {
		first = null;
		last = null;
		size = 0;
	}

	/**
	 * Inserts (does not overwrite) the given value at the given position in array.
	 * The legal positions are 0 to <code>size</code> (both are included)
	 * 
	 * @param value    to be insertred
	 * @param position to be inserted at
	 * @throws NullPointerException      if <code>value</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if <code>position</code> is missused
	 */
	public void insert(Object value, int position) {
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

	/**
	 * Searches the collection and returns the index of the first occurrence of the
	 * given value or -1 if the value is not found
	 * 
	 * @param value to be found
	 * @return index of the given value
	 */
	public int indexOf(Object value) {
		if (value != null) {
			var node = first;
			for (int i = 0; i < size; i++, node = node.next)
				if (node.value.equals(value))
					return i;
		}
		return VALUE_IS_NOT_FOUND;
	}

	/**
	 * Removes element at specified index from collection. Element that was
	 * previously at location index+1 after this operation is on location index ,
	 * etc. Legal indexes are 0 to <code>size-1</code>
	 * 
	 * @param index at which element is to be removed
	 * @throws IndexOutOfBoundsException if index is missused
	 */
	public void remove(int index) {
		checkIndex(index, size);
		removeNode(getNode(index));
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
		ListNode node = first;
		for (int i = 0; i < size; i++, node = node.next)
			array[i] = node.value;
		return array;
	}

	@Override
	public void forEach(Processor processor) {
		for (var node = first; node != null; node = node.next) {
			processor.process(node.value);
		}
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
	}
}
