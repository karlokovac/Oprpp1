package hr.fer.oprpp1.custom.scripting.elems;

public class ElementsString extends Element {
	private final String value;

	public ElementsString(String value) {
		super();
		this.value = value;
	}

	@Override
	public String asText() {
		return value;
	}

	public String getValue() {
		return value;
	}
}
