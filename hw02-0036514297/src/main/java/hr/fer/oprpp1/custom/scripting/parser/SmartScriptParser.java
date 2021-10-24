package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.lexer.Lexer;
import hr.fer.oprpp1.custom.scripting.nodes.Node;

public class SmartScriptParser {

	private final Lexer lexer;
	private Node programNode;
	
	public SmartScriptParser(String doucment) {
		lexer=new Lexer(doucment);
	}
	
	private void parse() {
		
	}
}
