package hr.fer.oprpp1.custom.collections;

/** Interface that defines behavior of iterating over elements of collection */
public interface ElementsGetter {
	/**
	 * Method that checks is there more elements to iterate over
	 * 
	 * @return <code>boolean</code> defining statements truth
	 */
	boolean hasNextElement();

	/**
	 * Method that retrieves next element of collection
	 * 
	 * @return <code>Object</code> that is retrieved
	 * @throws <code>NoSuchElementException</code> if there is no element to
	 * retrieve
	 */
	Object getNextElement();

	/**
	 * Method that iterates over remaining elements and applies
	 * <code>Processor.process()</code> on it
	 * 
	 * @param p <code>Processor</code> for element processing
	 */
	default void processRemaining(Processor p) {
		while (hasNextElement())
			p.process(getNextElement());
	}
}
