package hr.fer.oprpp1.custom.collections;

/**
 * The <code>Processor</code> is a model of an object capable of performing some
 * operation on the passed object. For this reason, each <code>Processor</code>
 * must have the <code>process(Object value)</code> method. Class
 * <code>Processor</code> here represents an conceptual contract between clients
 * which will have objects to be processed, and each concrete
 * <code>Processor</code> which knows how to perform the selected operation.
 * Each concrete <code>Processor</code> will be defined as a new class which
 * inherits from the <code>class Processor</code>
 */
@FunctionalInterface
public interface Processor {
	void process(Object value);
}
