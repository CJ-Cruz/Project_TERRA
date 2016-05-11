package data.utils;

import java.util.Random;

public class Question{
	
	private Word[] question;
	private Answer[] answers;
	
	public Question(Word[] words, Answer[] ans) {
	
		question = words;
		answers = ans;
		
	}

	public Word[] getWords(){
		return question;
	}
	
	public String reply(){
		Random r = new Random();
		String rep = answers[r.nextInt(answers.length)].makeReply();
		System.out.println(rep);
		return rep;
	}
	
	public void setAnswers(Answer[] a){
		answers = a;
	}
	
	public void setWords(Word[] w){
		question = w;
	}
	
}