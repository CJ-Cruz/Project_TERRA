package data.utils;

import java.util.LinkedList;
import java.util.Random;

public class Word {

	public String token;
	public int tag;
	public int occurrence = 0;
	public LinkedList<String> synonyms;
	public LinkedList<Word> translations;
	
	public Word(String tok, int t){
		
		token = tok;
		tag = t;
		translations = new LinkedList<Word>();
		synonyms = new LinkedList<String>();
		
	}
	
	public Word(Word w) {
		
		this.token = w.token;
		this.tag = w.tag;
		this.occurrence = w.occurrence;
		this.translations = new LinkedList<Word>();
		this.synonyms = new LinkedList<String>();
		
		for(int i = 0; i < w.synonyms.size(); i++)
			this.synonyms.add(w.synonyms.get(i));
		
	}

	@Override
	public String toString(){
		
		return token + " | " + tag;
		
	}

	public static void print(Word[] toprint) {

		System.out.println("Word["+toprint.length+"]:{");
		for(Word t:toprint)
			System.out.println("  "+t+ ",");
		System.out.println("}");
		
	}

	public String getRandomSyn() {
	
		Random r = new Random();
		
		return synonyms.get(r.nextInt(synonyms.size()));
	
	}
	
}