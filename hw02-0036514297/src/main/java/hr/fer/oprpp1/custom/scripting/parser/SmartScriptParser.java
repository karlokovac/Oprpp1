package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.lexer.ScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.ScriptTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;

public class SmartScriptParser {

	private final ScriptLexer lexer;
	private final DocumentNode documentNode;

	public SmartScriptParser(String doucment) {
		lexer = new ScriptLexer(doucment);
		documentNode = new DocumentNode();
		parse();
	}

	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	private void parse() {
		lexer.nextToken();
		while (!isTokenOfType(ScriptTokenType.EOF)) {
			if (isTokenOfType(ScriptTokenType.WORD)) {
				documentNode.addChildNode(new TextNode(lexer.getToken().toString()));
			}
		}
	}

	private boolean isTokenOfType(ScriptTokenType type) {
		return lexer.getToken().getType().equals(type);
	}
}
