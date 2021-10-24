package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

public class EchoNode extends Node {
	private final Element[] elements;

	public EchoNode(Element[] elements) {
		super();
		this.elements = elements;
	}

	public Element[] getElements() {
		return elements;
	}

}
