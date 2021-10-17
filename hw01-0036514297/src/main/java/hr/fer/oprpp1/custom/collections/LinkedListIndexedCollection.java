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

	private static final int VALUE_IS_NOT_FOUND = -1;

	private int size;
	private ListNode first;
	private ListNode last;

	public LinkedListIndexedCollection() {
		size = 0;
		first = null;
		last = null;
	}

	public LinkedListIndexedCollection(Collection other) {
		this();
		requireNonNull(other);
		addAll(other);
	}

	@Override
	public void add(Object value) {
		requireNonNull(value);
		append(value);
	}

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

	public int indexOf(Object value) {
		if (value != null) {
			var node = first;
			for (int i = 0; i < size; i++, node = node.next)
				if (node.value.equals(value))
					return i;
		}
		return VALUE_IS_NOT_FOUND;
	}

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
	 * @param value to prepend to the beginning
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

	private void insertInTheMiddle(Object value, int position) {
		var node = getNode(position);
		var inserted = new ListNode(node.previous, node, value);
		node.previous.next = inserted;
		node.previous = inserted;
		size++;
	}

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
