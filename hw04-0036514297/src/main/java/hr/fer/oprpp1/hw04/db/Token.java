package hr.fer.oprpp1.hw04.db;

/** Models token to be generated during lexical analysis of text */
public record Token(TokenType type,String value) {
}
		