package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Demo of stack usage
 */
public class StackDemo {
	public static void main(String[] args) {
		checkInput(args);
		try {
			System.out.println(evaluatePostfix(tokenize(args[0])));
		} catch (ArithmeticException e) {
			System.out.println("Division by zero");
		} catch (NumberFormatException | EmptyStackException e) {
			System.out.println("Expression invalid");
		} catch (InvalidExpressionException e) {
			System.out.println("Calculation fail");
		}
	}

	/**
	 * Method for evaluating postfix notation arithmetics
	 * 
	 * @param tokens to be parsed
	 * @return result of the calculation
	 */
	private static int evaluatePostfix(String[] tokens) {
		final ObjectStack stack = new ObjectStack();
		for (String token : tokens) {
			if (isOperator(token)) {
				final int op2 = Integer.parseInt((String) stack.pop());
				final int op1 = Integer.parseInt((String) stack.pop());
				stack.push(String.valueOf(preformOperation(op1, op2, token)));
			} else
				stack.push(token);

		}
		if (stack.size() != 1)
			throw new InvalidExpressionException();
		return Integer.parseInt((String) stack.pop());
	}

	/**
	 * Splits the input into tokens for parsing
	 * 
	 * @param input original string
	 * @return tokens of a string
	 */
	private static String[] tokenize(String input) {
		return input.trim().replaceAll(" +", " ").split(" ");
	}

	/**
	 * Checks if argument is valid
	 * 
	 * @param args
	 */
	private static void checkInput(String[] input) {
		if (input.length != 1)
			throw new IllegalArgumentException("Only one argument can be passed");
	}

	/**
	 * Checks if input is a valid operator
	 * 
	 * @param input to be checked
	 * @return <code>boolean</code> indicating truth of the statement
	 */
	public static boolean isOperator(String input) {
		return input.matches("[+-/*%]");
	}

	/**
	 * Calculates the operation on both operands
	 * 
	 * @param op1      operand 1
	 * @param op2      operand 2
	 * @param operator operator for the calculation
	 * @return result of the calculation
	 */
	private static int preformOperation(int op1, int op2, String operator) {
		switch (operator) {
		case "+":
			return op1 + op2;
		case "-":
			return op1 - op2;
		case "/":
			return op1 / op2;
		case "*":
			return op1 * op2;
		case "%":
			return op1 % op2;
		default:
			throw new InvalidExpressionException();
		}
	}

}
