package hr.fer.oprpp1.custom.scripting.nodes;

public class TextNode extends Node {
	private final String text;

	public TextNode(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
