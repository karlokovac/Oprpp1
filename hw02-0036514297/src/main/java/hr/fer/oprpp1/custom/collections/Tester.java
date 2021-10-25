package hr.fer.oprpp1.custom.collections;

/**
 * Models objects which test the provided <code>Object</code>
 */
@FunctionalInterface
public interface Tester {

	/**
	 * Tests the provided <code>Object</code>
	 * 
	 * @param obj <code>Object</code> to be tested
	 * @return test result
	 */
	boolean test(Object obj);
}
