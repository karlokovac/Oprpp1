package hr.fer.oprpp1.custom.collections;

public class Collection {

	protected Collection() {
	}

	/**
	 * Returns <code>true</code> if collection contains no objects and
	 * <code>false</code> otherwise
	 * 
	 * @return boolean state
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * The number of currently stored objects in this collections
	 * 
	 * @return number of stored objects
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds the given object into this collection
	 * 
	 * @param value to be added
	 */
	public void add(Object value) {
	}

	/**
	 * Returns <code>true</code> only if the collection contains given value, as
	 * determined by <code>equals</code> method
	 * 
	 * @param value <code>Object</code> to be searched
	 * @return boolean indicating presence
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Returns <code>true</code> only if the collection contains given value as
	 * determined by <code>equals</code> method and removes one occurrence of it
	 * 
	 * @param value <code>Object</code> to remove
	 * @return boolean indicating removal of an <code>Object</code>
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Allocates new array with size equals to the size of this collections, fills
	 * it with collection content and returns the array. This method never returns
	 * <code>null</code>
	 * 
	 * @return New array of elements
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Method calls <code>processor.process()</code> for each element of this
	 * collection. The order in which elements will be sent is undefined in the
	 * class
	 * 
	 * @param processor <code>Processor</code> to be used
	 */
	public void forEach(Processor processor) {
	}

	/**
	 * Method adds into the current collection all elements from the given
	 * collection. This other collection remains unchanged
	 * 
	 * @param other <code>Collection</code> of elements to be added
	 */
	public void addAll(Collection other) {
		class Adder extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		Processor adder = new Adder();
		other.forEach(adder);
	}

	/** Removes all elements from this collection */
	public void clear() {
	}
}
