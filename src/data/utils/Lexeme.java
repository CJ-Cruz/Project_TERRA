package data.utils;

public class Lexeme {

	public String token;
	public int tag;
	
	public Lexeme(String tok, int tag){
		token = tok;
		this.tag = tag;
	}
	
	@Override
	public String toString(){
		
		return token + " | " + tag;
		
	}
	
}
