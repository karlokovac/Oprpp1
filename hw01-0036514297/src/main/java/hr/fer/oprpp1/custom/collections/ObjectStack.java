package hr.fer.oprpp1.custom.collections;

public class ObjectStack {
	private ArrayIndexedCollection internal;

	public ObjectStack() {
		internal = new ArrayIndexedCollection();
	}

	public boolean isEmpty() {
		return internal.isEmpty();
	}

	public int size() {
		return internal.size();
	}

	public void push(Object value) {
		internal.add(value);
	}

	public Object pop() {
		Object element = peek();
		internal.remove(lastInternal());
		return element;
	}

	public Object peek() {
		if (isEmpty())
			throw new EmptyStackException();
		return internal.get(lastInternal());

	}

	public void clear() {
		internal.clear();
	}

	private int lastInternal() {
		return internal.size() - 1;
	}
}
