package utils;

import java.util.Hashtable;
import java.util.LinkedList;

import data.utils.Word;

public class Trigram {

	public Hashtable list;
	public boolean verbose = false;
	
	public Trigram(){
		list = new Hashtable();
	}

	public void collect(Word ... words){
		
		if(words.length >= 3){
			
			for(int i = 0; i < words.length-2; i++){
				
				Tri temp = new Tri(words[i].tag, words[i+1].tag, words[i+2].tag);
		
				Tri get = (Tri) list.get(temp);
								
				if(get == null)
					list.put(temp, temp);
				
			}
			
		}
		
	}
	
	public void collect(int ... tags){
		
		if(tags.length >= 3){
			
			for(int i = 0; i < tags.length-2; i++){
				
				Tri temp = new Tri(tags[i], tags[i+1], tags[i+2]);
				
				Tri get = (Tri) list.get(temp);
				
				if(get == null)
					list.put(temp, temp);
				
			}
			
		}
		
	}

	public Word[] findBest(Word[] ... words){
		
		//Compute total combinations of words with different tags
		int total = 1;
		for(Word[] w:words)
			total *= w.length;
		
		//list of all combinations of words
		Word[][] list = new Word[total][words.length];	
		int cut = total;
		for(int i = 0; i < words.length; i++){
			
			int cnum = 0;
			cut /= words[i].length;
			for(int j = 0; j < total; j++){
				
				if(j > 0 && words[i].length > 1){
					if(j%cut == 0){
						cnum++;
						if(cnum == words[i].length)
							cnum = 0;
					}
					
					list[j][i] = words[i][cnum];
					
				}
				else
					list[j][i] = words[i][0];
				
				
			}
				
		}
		
		if(verbose){
			System.out.println("\nTag Sequences:");
		
			for(int i = 0; i < list.length; i++){
				
				System.out.print("["+(i+1)+"]: ");
				for(int j = 0; j < list[0].length; j++){
					
					System.out.print(list[i][j].tag+", ");
					
				}
				System.out.println();
				
			}
		
			System.out.println("\nScores:");
		}
		TrigramResult[] test = new TrigramResult[total];
		int firsthighest = 0;
		int score = 0;
		for(int i = 0; i < total; i++){
			if(verbose)
			System.out.print("["+(i+1)+"]: ");
			test[i] = new TrigramResult(list[i], this.list, verbose);
			if(test[i].score > score){
				firsthighest = i;
				score = test[i].score;
			}
			
		}
		
		if(verbose){
			System.out.println("\nFirst Best Tag Sequence:");
			for(Word w: test[firsthighest].sequence)
				System.out.print(w.token + " ");
			
			System.out.println();
		}
		return test[firsthighest].sequence;
		
	}
	
	public boolean check(Word ... words){
		
		Tri temp = new Tri(words[0].tag, words[1].tag, words[2].tag);
		
		for(int j = 0; j < list.size(); j++)
			if(list.get(j).equals(temp))
				return true;
			
		return false;
		
	}
	
}

class Tri{
	
	public int[] tags;
	public int occurrence;
	
	public Tri(int ... t){
		
		occurrence = 0;
		tags = new int[3];
		for(int i = 0; i < 3; i++)
			tags[i] = t[i];
		
	};
	
	public boolean equals(Tri temp){
		
		for(int i = 0; i < 3; i++){
			if(this.tags[i] != temp.tags[i])
				return false;
		}
		
		return true;
		
	}
	
}

class TrigramResult{
	
	public int score = 0;
	public Word[] sequence;
	
	public TrigramResult(Word[] s, Hashtable list, boolean verbose){
		sequence = s;
		compute(list, verbose);
		
	}
	
	private int compute(Hashtable list, boolean verbose){
	
		Tri[] test = new Tri[sequence.length-2];
		
		for(int i = 0; i < sequence.length-2; i++){
			test[i] = new Tri(sequence[i].tag, sequence[i+1].tag, sequence[i+2].tag);
		}
		
		for(Tri t: test)
			if(list.get(t)!=null);
				score++;
		
		if(verbose)
		System.out.println(score);
		return score;
		
	}
	
	
	
}