package hr.fer.oprpp1.custom.collections;

public interface List extends Collection {
	/**
	 * Returns the object that is stored at position index. Valid indexes are 0 to
	 * size-1
	 * 
	 * @param index of the position
	 * @return <code>Object</code> to be returned
	 * @throws IndexOutOfBoundsException if <code>index</code> is misused
	 */
	Object get(int index);

	/**
	 * Inserts (does not overwrite) the given value at the given position. The legal
	 * positions are 0 to <code>size</code> (both are included)
	 * 
	 * @param value    to be inserted
	 * @param position to be placed at
	 * @throws NullPointerException      if <code>value</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if position is misused
	 */
	void insert(Object value, int position);

	/**
	 * Searches the collection and returns the index of the first occurrence of the
	 * given value or -1 if the value is not found
	 * 
	 * @param value to be searched for
	 * @return position of the element
	 */
	int indexOf(Object value);

	/**
	 * Removes element at specified index from collection. Element that was
	 * previously at location <code>index+1</code> after this operation is on
	 * location index , etc. Legal indexes are 0 to <code>size-1</code>
	 * 
	 * @param index position of the element
	 * @throws IndexOutOfBoundsException if <code>index</code> is misused
	 */
	void remove(int index);
}
