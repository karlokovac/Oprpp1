package hr.fer.oprpp1.custom.collections;

public class ObjectStack {
	/** Array adapted for internal storage of stack */
	private ArrayIndexedCollection<Object> internal;

	/** Default constructor */
	public ObjectStack() {
		internal = new ArrayIndexedCollection<>();
	}

	/**
	 * Returns true if collection contains no objects and false otherwise
	 * 
	 * @return boolean state
	 */
	public boolean isEmpty() {
		return internal.isEmpty();
	}

	/**
	 * The number of currently stored objects in this collections
	 * 
	 * @return number of stored objects
	 */
	public int size() {
		return internal.size();
	}

	/**
	 * Pushes given value on the stack
	 * 
	 * @param value to be pushed
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	public void push(Object value) {
		internal.add(value);
	}

	/**
	 * Removes last value pushed on stack from stack and returns it
	 * 
	 * @return value from the top of the stack
	 * @throws EmptyStackException if used when stack is empty
	 */
	public Object pop() {
		Object element = peek();
		internal.remove(lastInternal());
		return element;
	}

	/**
	 * Returns last element placed on stack but does not delete it from stack
	 * 
	 * @return value from the top of the stack
	 * @throws EmptyStackException if used when stack is empty
	 */
	public Object peek() {
		if (isEmpty())
			throw new EmptyStackException();
		return internal.get(lastInternal());

	}

	/** Removes all elements from stack */
	public void clear() {
		internal.clear();
	}

	/**
	 * Returns the last element index of the internal storage
	 * 
	 * @return last element index
	 */
	private int lastInternal() {
		return internal.size() - 1;
	}
}
