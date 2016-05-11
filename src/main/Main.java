package main;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

import data.bases.Dictionary;
import data.bases.GrammarBase;
import data.bases.KnowledgeBase;
import data.utils.Lexeme;
import data.utils.Word;
import utils.Rules;
import utils.SymSpell;
import utils.XMLParser;

public class Main implements TERRA{

	public Dictionary dictionary;
	public GrammarBase grammarbase;
	public KnowledgeBase knowledgebase;
	
	public static void main(String[] Args){
		
		Main m = new Main();
		
		Scanner s = new Scanner(System.in);
		m.prepare();
		
		DemoUI demo = new DemoUI(m);
		Admin admin = new Admin(m);
		
		while(true){
			
			admin.read(s.nextLine());
			
		}
		
	}
	
	public String query(String q){
		
		return knowledgebase.generateReply(
				grammarbase.syntaxAnalysis(
						dictionary.lexicalAnalysis(q)
				)
		);
		
	}
	
	private void prepare(){
		
		Rules.setup();
		setupDictionary();
		setupGrammarBase();
		setupKnowledgeBase();
		
		grammarbase.trigram.verbose = true;
		dictionary.verbose = true;
		
	}
	
	private void setupDictionary(){
		
		dictionary = XMLParser.parseDict(TERRA.XML_DICTIONARY);
		dictionary.setupSC();
		System.out.println("Finished building dictionary");
		
	}

	private void setupGrammarBase(){
		
		grammarbase = XMLParser.parseGB(TERRA.XML_GRAMMAR);
		System.out.println("Finished collecting trigrams");
		
	}

	private void setupKnowledgeBase(){
		
		knowledgebase = new KnowledgeBase();
		for(String knowledge:TERRA.DEFAULT_KNOWLEDGE)
			knowledgebase = XMLParser.parseKB(knowledge.concat(".xml"), dictionary, grammarbase, knowledgebase);
		System.out.println("Finished building knowledge base");
		
	}
	
}

class Admin{
	
	Main bot;
	String[] allkbxml;
	LinkedList<String> kbxml;
	public Admin(Main b) {
		bot = b;
		
		File s = new File("knowledge/");
		allkbxml = s.list();
		for(int i = 0; i < allkbxml.length; i++)
			allkbxml[i] = allkbxml[i].substring(0, allkbxml[i].length()-4);
		
		kbxml = new LinkedList<String>();
		for(String i: TERRA.DEFAULT_KNOWLEDGE)
			kbxml.push(i);
		
	}

	public void read(String input){
		
		if(input.equals("list")){
			listKB();
			return;
		}
		else if(input.equals("rdict")){
			resetDict();
			return;
		}
		else if(input.equals("rgram")){
			resetGram();
			return;
		}
		else if(input.equals("rrule")){
			resetRule();
			return;
		}
		else if(input.equals("rknow")){
			resetKB();
			return;
		}
		else if(input.equals("reset")){
			resetRule();
			resetDict();
			resetGram();
			resetKB();
			return;
		}
		else if(input.equals("add")){
			int c = selectIndex("add", true);
			addKB(allkbxml[c]);
			return;
		}
		else if(input.equals("remove")){
			int c = selectIndex("remove", false);
			String removed = kbxml.get(c);
			kbxml.remove(c);
			System.out.println("Removed " + removed + "!\nPlease call the 'reset' command to make changes.");
			return;
		}
		else if(input.equals("update")){
			
			int r = selectIndex("be replaced", false);
			String replaced = kbxml.get(r);
			int a = selectIndex("replace with", true);
			kbxml.set(r, allkbxml[a]);
			System.out.println("Updated " + replaced + " to " + allkbxml[a] + "!\nPlease call the 'reset' command to make changes.");
			return;
			
		}
		else
			System.err.println("Error in command!");
		
	}
	
	private void resetRule() {
		Rules.setup();
		System.out.println("Rules Reset!");
	}

	private void resetGram() {
		bot.grammarbase = XMLParser.parseGB(TERRA.XML_GRAMMAR);
		System.out.println("Trigrams Reset!");
	}

	private void resetDict() {
		
		bot.dictionary = XMLParser.parseDict(TERRA.XML_DICTIONARY);
		bot.dictionary.setupSC();
		System.out.println("Dictionary Reset!");
			
		
	}

	public void listKB(){
		
		int i = 0;
		for(String k:kbxml){
			
			System.out.println("["+(i++)+"] - " + k);
			
		}
		System.out.println("================");
		
	}
	
	public int selectIndex(String action, boolean listAll){
		
		Scanner s = new Scanner(System.in);
		int c = -1;
		int cmax = listAll?allkbxml.length-1:kbxml.size()-1;
		do{
			System.out.println("Please input index of knowledge to " + action);
			if(listAll)
				listAllKB();
			else
				listKB();
			
			try{
				c = Integer.parseInt(s.nextLine());
			}catch(Exception e){System.err.println("Please enter a number");}
			
		}while(c < 0 || c > cmax);
		
		return c;
		
	}
	
	private void listAllKB() {
		
		int i = 0;
		for(String k:allkbxml){
			
			System.out.println("["+(i++)+"] - " + k);
			
		}
		System.out.println("================");
		
	}

	public void resetKB(){

		bot.dictionary.verbose = false;
		bot.grammarbase.trigram.verbose = false;
		
		bot.knowledgebase = new KnowledgeBase();
		for(String knowledge:kbxml)
			bot.knowledgebase = XMLParser.parseKB(knowledge.concat(".xml"), bot.dictionary, bot.grammarbase, bot.knowledgebase);
		
		bot.dictionary.verbose = true;
		bot.grammarbase.trigram.verbose = true;
		
		System.out.println("KnowledgeBase Questions Reset!");
		
	}
	
	public void addKB(String knowledge){
		
		kbxml.add(knowledge);
		bot.knowledgebase = XMLParser.parseKB(knowledge.concat(".xml"), bot.dictionary, bot.grammarbase, bot.knowledgebase);
		System.out.println("Added "+knowledge+"!\n No need to call the 'reset' command to make changes.");
		
	}
	
	
}