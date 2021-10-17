package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

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

	private static int evaluatePostfix(String[] tokens) {
		final ObjectStack stack = new ObjectStack();
		for (String token : tokens) {
			if (isOperator(token)) {
				final int op2 = Integer.parseInt((String) stack.pop());
				final int op1 = Integer.parseInt((String) stack.pop());
				stack.push(String.valueOf(preformOp(op1, op2, token)));
			} else
				stack.push(token);

		}
		if (stack.size() != 1)
			throw new InvalidExpressionException();
		return Integer.parseInt((String) stack.pop());
	}

	private static String[] tokenize(String input) {
		return input.trim().replaceAll(" +", " ").split(" ");
	}

	private static void checkInput(String[] args) {
		if (args.length != 1)
			throw new IllegalArgumentException("Only one argument can be passed");
	}

	public static boolean isOperator(String str) {
		return str.matches("[+-/*%]");
	}

	private static int preformOp(int op1, int op2, String operator) {
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
			return 0;
		}
	}

}
