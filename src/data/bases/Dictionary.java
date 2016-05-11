package data.bases;

import java.util.LinkedList;

import data.utils.Lexeme;
import data.utils.Pair;
import data.utils.Trie;
import data.utils.Word;
import main.TERRA;
import utils.Rules;
import utils.SymSpell;

public class Dictionary implements TERRA{

	public Trie[] list = new Trie[2];
	public SymSpell checker;
	public Word[] unknown;
	public boolean verbose = false;
	
	final static int ENGLISH = 0;
	final static int TAGALOG = 1;
	
	public Dictionary(){

		//english
		list[0] = new Trie('\n');
		//tagalog
		list[1] = new Trie('\n');
		
		unknown = new Word[1];
		unknown[0] = new Word("UNK_WORD", 0);
		
	}
	
	public static int getTag(String tag){
		
		switch(tag){
		case "FW":  return 0;			//Unknown/Foreign Word
		case "CC":  return 1;			//Coordinating Conjunction
		case "CD":  return 2;			//Cardinal Number
		case "DT":  return 3;			//Determiner/Article
		case "EX":  return 4;			//Existential 'there'
		case "IN":  return 5;			//Preposition/Subordinating Conjunction
		case "JJ":  return 6;			//Adjective
		case "JJR":  return 7;			//Adjective (Comparative)
		case "JJS":  return 8;			//Adjective (Superlative)
		case "MD":  return 10;			//Modal
		case "NN":  return 11;			//Noun
		case "NNS":  return 12;			//Noun Plural
		case "NP":  return 13;			//Noun Proper
		case "NPS":  return 14;			//Noun Proper Plural
		case "PD":  return 15;			//Predeterminer
		case "POS":  return 16;			//Possessive ending
		case "PP":  return 17;			//Personal pronoun
		case "PP$":  return 18;			//Possessive pronoun
		case "RB":  return 19;			//Adverb
		case "RR":  return 20;			//Adverb (Comparative)
		case "RS":  return 21;			//Adverb (Superlative)
		case "SYM":  return 23;			//Symbol
		case "TO":  return 24;			//To
		case "UH":  return 25;			//Interjection
		case "VB":  return 26;			//Verb (Base)
		case "VBD":  return 27;			//Verb (Past)
		case "VBG":  return 28;			//Verb (PrP/Gerund)
		case "VBN":  return 29;			//Verb (PaP)
		case "VBP":  return 30;			//Verb (Present)
		case "VBZ":  return 31;			//Verb (Present, 3rd Person)
		case "WDT":  return 32;			//WH-Determiner
		case "WP":  return 33;			//WH-Pronoun
		case "WP$":  return 34;			//WH-Possessive Pronoun
		case "WRB":  return 35;			//WH-Adverb
		case ",":  return 36;			//Comma
		case ".":  return 37;			//Sentence ender
		case "UNL":  return 41;			//for separated unlapi when using english
		case "VBF":  return 43;				//Future tense for tagalog words
		
		}
		
		return -1;
		
	}
	
	public static final String name(int tag){	//names of tags
		
		switch(tag){
		
		case 0:  return "FW";			//Unknown/Foreign Word
		case 1:  return "CC";			//Coordinating Conjunction
		case 2:  return "CD";			//Cardinal Number
		case 3:  return "DT";			//Determiner/Article
		case 4:  return "EX";			//Existential 'there'
		case 5:  return "IN";			//Preposition/Subordinating Conjunction
		case 6:  return "JJ";			//Adjective
		case 7:  return "JJR";			//Adjective (Comparative)
		case 8:  return "JJS";			//Adjective (Superlative)
		case 10:  return "MD";			//Modal
		case 11:  return "NN";			//Noun
		case 12:  return "NNS";			//Noun Plural
		case 13:  return "NP";			//Noun Proper
		case 14:  return "NPS";			//Noun Proper Plural
		case 15:  return "PD";			//Predeterminer
		case 16:  return "POS";			//Possessive ending
		case 17:  return "PP";			//Personal pronoun
		case 18:  return "PP$";			//Possessive pronoun
		case 19:  return "RB";			//Adverb
		case 20:  return "RR";			//Adverb (Comparative)
		case 21:  return "RS";			//Adverb (Superlative)
		case 23:  return "SYM";			//Symbol
		case 24:  return "TO";			//To
		case 25:  return "UH";			//Interjection
		case 26:  return "VB";			//Verb (Base)
		case 27:  return "VBD";			//Verb (Past)
		case 28:  return "VBG";			//Verb (PrP/Gerund)
		case 29:  return "VBN";			//Verb (PaP)
		case 30:  return "VBP";			//Verb (Present)
		case 31:  return "VBZ";			//Verb (Present, 3rd Person)
		case 32:  return "WDT";			//WH-Determiner
		case 33:  return "WP";			//WH-Pronoun
		case 34:  return "WP$";			//WH-Possessive Pronoun
		case 35:  return "WRB";			//WH-Adverb
		case 36:  return ",";			//Comma
		case 37: return ".";			//Sentence ender
		case 41:  return "UNL";			//for separated unlapi when using english
		case 43:  return "VBF";				//Future tense for tagalog words
		
		}
		
		return "UNK";
		
	}
	
	public void setupSC(){
		checker = new SymSpell(getTokens());
	}
	
	public Word search(int language, Lexeme word){
		
		return this.list[language].find(word);
		
	}

	public Word search(Lexeme word){
		
		Word temp;
		temp = this.list[ENGLISH].find(word);
		if(temp == null){
			temp = this.list[TAGALOG].find(word);
			return temp;
		}
		else
			return temp;

	}
	
	public Word[] get(String word){
		
		LinkedList<Word> out = new LinkedList<Word>();
		Word[] ENG;
		Word[] TAG;
		ENG = this.list[ENGLISH].get(word);
		TAG = this.list[TAGALOG].get(word);

		if(ENG != null)
			for(Word e:ENG)
				out.add(e);
		if(TAG != null)
			for(Word t:TAG){
				boolean add = true;
				for(Word w: out)
					if(t.token.equals(w.token) && t.tag==w.tag){
						add = false;
						break;
					}
					else
						add = true;

				if(add)
					out.add(t);
			
			}
		
		if(out.size() == 0)
			return null;
		
		return out.toArray(new Word[out.size()]);
		
	}
	
	public int Occurrences(String word){
		
		return this.list[ENGLISH].occurrence(word) + this.list[TAGALOG].occurrence(word);
		
	}
	
	//Adds the Word to the dictionary. Overwrites if same token and tag.
	public void add(int language, Word word){
		
			this.list[language].add(word);
		
	}

	public boolean has(int language, String word) {
		return list[language].has(word);
	}
	
	public boolean has(String word) {
		if(list[ENGLISH].has(word))
			return true;
		else
			if(list[TAGALOG].has(word))
				return true;
		return false;
	}

	public String[] getTokens() {
		
		LinkedList<String> all = list[ENGLISH].getTokens();
		all.addAll(list[TAGALOG].getTokens());
		return all.toArray(new String[all.size()]);
		
		
	}
	
	public String correct(String query){
		
		String[] candidates = checker.Correct(query);
		int n = candidates.length;
		
		if(n == 0)
			return "no similar";
		if(n == 1)
			return candidates[0];
		
		int max = 0;
		int maxdex = 0;
		for(int i = 0; i < n; i++){
			int temp = Occurrences(candidates[i]);
			if(temp > max){
				max = temp;
				maxdex = i;
			}
		}
		
		return candidates[maxdex];
		
	}
	
	public Word[][] lexicalAnalysis(String query){
		
		String[] qs = separate(fix(query.toLowerCase())).split(" ");
		LinkedList<String> qst = new LinkedList<String>();
		for(String q:qs){
			qst.add(fixTaglish(q));
		}
		query = "";
		for(String q:qst)
			if(!q.equals(" ") && !q.isEmpty())
				query += q + " ";
		
		qs = fix(query).split(" ");
		
		for(int i = 0; i < qs.length; i++){
			qs[i] = qs[i];
			if(!qs[i].matches("^-?\\d+(\\.\\d+)?$"))
				qs[i] = this.correct(qs[i]);
		}
		
		Word[][] words = new Word[qs.length][];
		for(int i = 0; i < qs.length; i++){
			
			//Checks if a cardinal number
			if(qs[i].matches("^-?\\d+(\\.\\d+)?$")){
				Word num = new Word(qs[i], Dictionary.getTag("CD"));
				Word[] temp = {num};
				words[i] = temp;
				continue;
			}
			
			Word[] collected = this.get(qs[i]);
			if(collected == null)
				words[i] = this.unknown;
			else
				words[i] = collected;
		
		}
		
		if(verbose){
			System.out.println("Lexical Analysis output:");
			for(Word[] w:words)
				System.out.print(w[0].token + " ");
			System.out.println();
			
		}

		return words;
		
	}
	
	//Separates symbols from words
	public static String separate(String q){
		
		LinkedList<Character> chars = new LinkedList<Character>();
		for(int i = 0; i < q.length(); i++){
		
			char c = q.charAt(i);
			int ci = (int) c;
			boolean sym = false;
			boolean isdec = false;
			
			if(i > 0 && i < q.length()-1){
				isdec = c == '.';
				if((q.charAt(i-1) < 48 || q.charAt(i-1) > 57 || q.charAt(i+1) < 48 || q.charAt(i+1) > 57 ))
						isdec = false;
			}
			if(!isdec && c != '\'' && c!='_' && ((ci <= 47 && ci >= 33) || (ci >= 58 && ci <= 63))){
				chars.add(' ');
				if(c != '@' && c != '#')
					sym = true;
			}
			
			chars.add(c);
		
			if(sym)
				chars.add(' ');
			
		}
		
		char[] s = new char[chars.size()];
		for(int i = 0; chars.size() > 0; i++)
			s[i] = chars.pop();
		
		return new String(s);
		
	}
	
	//Create a method that will split contracted words
	public static String fix(String sentence){
		
		String[] tok = sentence.split(" ");
		String nohash = "";
		for(String t:tok)
			nohash += (t.startsWith("#")||t.startsWith("@"))?(""):(t + " ");
		tok = nohash.split(" ");
		
		Pair<String,String> cont = Rules.conversions;
		for(int i = 0; i < tok.length; i++){
			for(int j = 0; j < cont.size(); j++){
				if(tok[i].equals(cont.getK(j))){
					tok[i] = (String) cont.getV(j);
				}
			}
		}
		
		String complete = "";
		for(String t:tok)
			complete += t + " ";
		
		tok = complete.split(" ");
		
		complete = "";
		for(String t:tok){
			if(!Rules.isFiller(t)){
				
				complete += changeNum(t) + " ";
			
			}
		}

		return aposSep(Rules.clearFillerPhrases(complete));
		
	}
	
	//for separating 's (ownership)
	private static String aposSep(String corpus){
		
		return corpus.replaceAll("\'s", " 's");
		
	}
	
	//converts one-digit number words into numbers
	private static String changeNum(String dig){
		
		switch(dig){

		case "zero": return "0";
		case "one": return "1";
		case "two": return "2";
		case "three": return "3";
		case "four": return "4";
		case "five": return "5";
		case "six": return "6";
		case "seven": return "7";
		case "eight": return "8";
		case "nine": return "9";
		case "ten": return "10";
		
		default: return dig;
		
		}
		
	}
	
	//For fixing unlapi + base verb
	private String fixTaglish(String word){
		
		if(word == "i"||this.has(TAGALOG, word))
			return word;
		
		if(word.startsWith("ma"))
			word = spliceCheck("ma", word);
		else if(word.startsWith("na"))
			word = spliceCheck("na", word);
		
		if(word.startsWith("maka"))
			word = spliceCheck("maka", word);
		else if(word.startsWith("naka"))
			word = spliceCheck("naka", word);
		
		if(word.startsWith("makaka"))
			word = spliceCheck("makaka", word);
		if(word.startsWith("nakaka"))
			word = spliceCheck("nakaka", word);
			
		if(word.startsWith("nakakapag"))
			word = spliceCheck("nakakapag", word);
		else if(word.startsWith("makakapag"))
			word = spliceCheck("makakapag", word);
		else if(word.startsWith("nakapag"))
			word = spliceCheck("nakapag", word);
		else if(word.startsWith("makapag"))
			word = spliceCheck("makapag", word);
		else if(word.startsWith("pan"))
			word = spliceCheck("pan", word);
		else if(word.startsWith("pam"))
			word = spliceCheck("pam", word);
		else if(word.startsWith("pang"))
			word = spliceCheck("pang", word);
		else if(word.startsWith("i"))
			word = spliceCheck("i", word);
		else if(word.startsWith("nag"))
			word = spliceCheck("nag", word);
		else if(word.startsWith("mag"))
			word = spliceCheck("mag", word);
		
		return word;
		
	}

	private String spliceCheck(String unlapi, String word){
		
		String temp = word.substring(unlapi.length());
		if(this.has(ENGLISH, temp))
			word = unlapi + " " + temp;
		
		if(verbose)
		System.out.println(word);
		
		return word;
			
	}

	public Word[][] answerCheck(String answer){
	
		answer = separate(answer);
		if(verbose)
		System.out.println(answer);
		
		String[] qs = answer.split(" ");
		
		Word[][] words = new Word[qs.length][];
		for(int i = 0; i < qs.length; i++){
			
			//Checks if a cardinal number
			if(qs[i].matches("^-?\\d+(\\.\\d+)?$")){
				Word num = new Word(qs[i], Dictionary.getTag("CD"));
				Word[] temp = {num};
				words[i] = temp;
				continue;
			}
			
			Word[] collected = this.get(qs[i].toLowerCase());
			if(collected == null){
				Word[] neo = new Word[1];
				neo[0] = new Word(qs[i], 0);
				words[i] = neo;
			}
			else{
				Word[] dupe = new Word[collected.length];
				int di = 0;
				for(Word w:collected){
					dupe[di] = new Word(w);
					dupe[di++].token = qs[i];
				}
				words[i] = dupe;
			}
		}
		
		return words;
	
	}
	
	
}
