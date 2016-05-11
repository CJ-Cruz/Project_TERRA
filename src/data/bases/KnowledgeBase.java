package data.bases;

import java.util.LinkedList;
import java.util.Random;

import data.utils.Answer;
import data.utils.Question;
import data.utils.Word;
import main.TERRA;

public class KnowledgeBase implements TERRA{

	private Question[] questionbase = new Question[0];
	
	public void addQuestions(Question ... qs){
		
		LinkedList<Question> qlist = new LinkedList<Question>();
		for(Question q:questionbase)
			qlist.addLast(q);
		for(Question q:qs)
			qlist.addLast(q);
		
		questionbase = qlist.toArray(new Question[qlist.size()]);
		
	}
	
	public String generateReply(Word[] query){
		
		System.out.println("Understood Grammar:");
		for(Word q:query)			
			System.out.print(q.token + "(" + Dictionary.name(q.tag) + ") ");
		System.out.println();
		
		double max = 0;
		int highestkeys = 0;
		MScore top = new MScore(0,0);
		for(int i = 0; i < questionbase.length; i++){
			
			MScore match = match(questionbase[i].getWords(), query, i);
			
			if(match.keys >= highestkeys){
				if(match.keys > highestkeys)
					max = 0;
				highestkeys = match.keys;
				if(match.score > max){
					top.score = match.score;
					top.keys = match.keys;
					max = match.score;
					top.index = i;
				}
				
			}
			
		
		}
		
		System.out.println("Highest Matching Question: ("+top.score+"|"+top.keys+")");
		Word[] q = questionbase[top.index].getWords();
		for(Word w:q)
			System.out.print(w.token + " ");
		System.out.println("\n\n\n");
		
		if(top.score < THRESHOLD_SCORE || top.keys < THRESHOLD_KEYS){
			if(TERRA.ALWAYSREPLY){
				System.out.println(TERRA.REPLY_NO_MATCHED);
				return TERRA.REPLY_NO_MATCHED;
			}
			else{
				System.out.println("<NO REPLY>");
				return "<NO REPLY>";
			}
		}
		
		System.out.print("TERRA: ");
		String reply = questionbase[top.index].reply().trim();
		if(reply.isEmpty())
			if(TERRA.ALWAYSREPLY){
				System.out.println(TERRA.REPLY_NO_MATCHED);
				return TERRA.REPLY_NO_MATCHED;
			}
			else{
				System.out.println("<NO REPLY>");
				return "<NO REPLY>";
			}
		
		return reply;
		
	}

	public MScore match(Word[] keywords, Word[] tokens, int i){
		
		MScore match = new MScore(i, keywords.length);
		LinkedList<Word> toks = new LinkedList<Word>();
		
		for(Word t: tokens)
			toks.add(t);
			
		double denom = keywords.length*2;	//key and tag
		match.score = 0;
		
		//Algorithm removes lexemes after occurrences of tags or token
		for(Word k:keywords){
			
			boolean matched = false;
			int iter = 0;
			for(Word t:toks){
				if(t.token.equals(k.token)){
					match.score++;
				}
				if(t.tag == k.tag){
					match.score++;
					matched = true;
				}
				if(t.tag == k.tag && t.token.equals(k.token))
					match.keys++;
				
				if(matched)
					break;
				
				iter++;
				
			}
			
			if(matched)
				toks.remove(iter);
			
		}
			
		
		match.score /= denom;
		
		return match;
		
	}

	
}

class MScore{
	
	double score;
	int keys;
	int index;
	int length;
	
	public MScore(int i, int l){
		index = i;
		length = l;
	}
	
}
