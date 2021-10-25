package hr.fer.oprpp1.custom.collections;

public interface Collection {

	/**
	 * Size constant which represents empty <code>Collection</code>
	 */
	int EMPTY = 0;

	/**
	 * Returns <code>true</code> if collection contains no objects and
	 * <code>false</code> otherwise
	 * 
	 * @return boolean state
	 */
	default boolean isEmpty() {
		return size() == EMPTY;
	}

	/**
	 * The number of currently stored objects in this collections
	 * 
	 * @return number of stored objects
	 */
	int size();

	/**
	 * Adds the given object into this collection
	 * 
	 * @param value to be added
	 */
	void add(Object value);

	/**
	 * Returns <code>true</code> only if the collection contains given value, as
	 * determined by <code>equals</code> method
	 * 
	 * @param value <code>Object</code> to be searched
	 * @return boolean indicating presence
	 */
	boolean contains(Object value);

	/**
	 * Returns <code>true</code> only if the collection contains given value as
	 * determined by <code>equals</code> method and removes one occurrence of it
	 * 
	 * @param value <code>Object</code> to remove
	 * @return boolean indicating removal of an <code>Object</code>
	 */
	boolean remove(Object value);

	/**
	 * Allocates new array with size equals to the size of this collections, fills
	 * it with collection content and returns the array. This method never returns
	 * <code>null</code>
	 * 
	 * @return New array of elements
	 */
	Object[] toArray();

	/**
	 * Method calls <code>processor.process()</code> for each element of this
	 * collection. The order in which elements will be sent is undefined in the
	 * class
	 * 
	 * @param processor <code>Processor</code> to be used
	 */
	default void forEach(Processor processor) {
		ElementsGetter getter = createElementsGetter();
		while (getter.hasNextElement())
			processor.process(getter.getNextElement());
	}

	/**
	 * Method adds into the current collection all elements from the given
	 * collection. This other collection remains unchanged
	 * 
	 * @param other <code>Collection</code> of elements to be added
	 */
	default void addAll(Collection other) {
		other.forEach((value) -> add(value));
	}

	/**
	 * Removes all elements from this collection
	 */
	void clear();

	/**
	 * Creates <code>ElementsGetter</code> instance over current collection
	 * 
	 * @return <code>ElementsGetter</code> instance
	 */
	ElementsGetter createElementsGetter();

	/**
	 * Adds all elements from the <code>Collection</code> to current collection that
	 * return <code>true</code> when being tested by <code>Tester</code>
	 * 
	 * @param col    <code>Collection</code> to add from
	 * @param tester <code>Tester</code> to test elements
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		col.forEach((value) -> {
			if (tester.test(value))
				add(value);
		});
	}
}
