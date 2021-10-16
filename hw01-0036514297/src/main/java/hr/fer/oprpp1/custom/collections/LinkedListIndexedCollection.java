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

	private int size;
	private ListNode first;
	private ListNode last;

	public LinkedListIndexedCollection() {
		size = 0;
		first = null;
		last = null;
	}

	public LinkedListIndexedCollection(Collection other) {

	}

	@Override
	public void add(Object value) {
		requireNonNull(value);
		ListNode element = new ListNode(last, null, value);
		if (isEmpty())
			first = element;
		else
			last.next = element;
		last = element;
		size++;
	}

	public Object get(int index) {
		checkIndex(index, size);
		ListNode node;
		if(index<=size/2) {
			node = first;
			for(int i=0;i<index;i++)
				node=node.next;
		}else {
			node = last;
			for(int i=size;i>index;i--)
				node=node.previous;
		}
		return node.value;
	}

	@Override
	public void clear() {
		first=null;
		last=null;
		size=0;
	}

	public void insert(Object value, int position) {

	}

	public int indexOf(Object value) {
		return 0;
	}

	public void remove(int index) {

	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object value) {
		return false;
	}

	@Override
	public boolean remove(Object value) {
		return false;
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void forEach(Processor processor) {
	}
}
