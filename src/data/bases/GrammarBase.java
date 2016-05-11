package data.bases;

import data.utils.Word;
import utils.Trigram;

public class GrammarBase {

	public Trigram trigram = new Trigram();
	
	public void add(int[] tags){
		
		for(int t: tags){
			System.out.print(t + ", ");
		}
		System.out.println();
		
		trigram.collect(tags);
		
	}
	
	public Word[] answerAnalysis(Word[][] words){
		
		if(words.length < 3){
			
			
			Word[] out = new Word[words.length];
			for(int i = 0; i < words.length; i++)
				out[i] = words[i][0];

			return out;
		
		}
		
		return trigram.findBest(words);
		
	}
	
	public Word[] syntaxAnalysis(Word[][] words){
		
		//If not a trigram, do not analyze
		if(words.length < 3){
			
			
			Word[] out = new Word[words.length];
			for(int i = 0; i < words.length; i++)
				out[i] = words[i][0];
			
			Word[] out2 = new Word[out.length+1];
			for(int i = 0; i < out.length; i++)
				out2[i] = out[i];
			out2[out.length] = new Word("?",Dictionary.getTag("."));
			
			return out2;
			
		}
		
		return trigram.findBest(words);
		
	}
	
}