package hr.fer.oprpp1.custom.collections;

public class WriteIfEvenHashProcessor extends Processor{

	public void process(Object value) {
		if (value.hashCode() % 2 == 0) {
			System.out.println("Got it!");
		}
	}

	public static void main(String[] args) {
		Object[] data = new Object[] { 
				Integer.valueOf(20),
				Integer.valueOf(25),
				"Ivana",
				"Stjepan",
				"B",
				Integer.valueOf(30),
				Boolean.valueOf(false)
		};
		
		Processor p = new WriteIfEvenHashProcessor();
		for (int i = 0; i < data.length; i++) {
			p.process(data[i]);
		}
	}
}
