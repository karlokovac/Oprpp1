package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;

public class Node {

	private List internalList;
	
	public void addChildNode(Node child) {
		if(internalList==null)
			internalList = new ArrayIndexedCollection(4);
		internalList.add(child);
	}
	
	public int numberOfChildren() {
		return internalList.size();
	}
	
	public Node getChild(int index) {
		return (Node) internalList.get(index);
	}
	
}
