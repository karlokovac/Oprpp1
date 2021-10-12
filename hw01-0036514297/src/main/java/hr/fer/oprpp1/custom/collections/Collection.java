package hr.fer.oprpp1.custom.collections;

public class Collection {

	protected Collection() {
	}

	/**
	 * @return <code>true</code> if collection contains no objects and
	 *         <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * @return The number of currently stored objects in this collections
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds the given object into this collection
	 */
	public void add(Object value) {
	}

	/**
	 * @return <code>true</code> only if the collection contains given value, as
	 *         determined by equals method
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Returns true only if the collection contains given value as determined by
	 * equals method and removes one occurence of it
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Allocates new array with size equals to the size of this collections, fills
	 * it with collection content and returns the array. This method never returns
	 * null
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
	 */
	public void forEach(Processor processor) {
	}

	/**
	 * Method adds into the current collection all elements from the given
	 * collection. This other collection remains unchanged
	 */
	public void addAll(Collection other) {
		class Adder extends Processor {
			public void process(Object value) {
				add(value);
			}
		}
		forEach(new Adder());
	}

	/**
	 * Removes all elements from this collection
	 */
	public void clear() {
	}
}
