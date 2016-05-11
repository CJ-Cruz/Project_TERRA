package data.utils;

import java.util.Comparator;
import java.util.LinkedList;

public class Trie implements Comparator{

	public Trie parent;
	public LinkedList<Trie> children = new LinkedList<Trie>();
	public char key;
	public Word[] value;
	
	public Trie(char k){
		this(k, new Word[0]);
	}
	
	public Trie(char k, Word[] v){
		parent = null;
		key = k;
		value = v;
	}
	
	public void setParent(Trie p){
		parent = p;
	}
	
	public boolean isEmpty(){
		return value.length == 0;
	}
	
	public Word find(Lexeme word){
		return find(word, 0);
	}
	
	private Word find(Lexeme word, int next){
		
		if(word.token.length() > next){
			char n = word.token.charAt(next);
			
			for(Trie child:children)
				if(child.key == n)
					return child.find(word, next+1);
		
		}
		else if(word.token.equals(value[0].token)){
				for(int i = 0; i < value.length; i++){
//					System.out.println(value[i] + " =?= " + word);
					if(value[i].tag == word.tag)
						return value[i];
				}
		}
		
		return null;
		
	}
	
	public Word[] get(String word){
		return get(word, 0);
	}
	
	private Word[] get(String token, int next){
		
		if(token.length() > next){
			char n = token.charAt(next);
			
			for(Trie child:children)
				if(child.key == n)
					return child.get(token, next+1);
		
		}
		else if(!isEmpty() && token.equals(value[0].token)){
				return value;
		}
		
		return null;
		
	}
	
	public boolean has(String word){
		return has(word, 0);
	}
	
	private boolean has(String word, int next){
		
		if(word.length() > next){
			char n = word.charAt(next);
			
			for(Trie child:children)
				if(child.key == n)
					return child.has(word, next+1);
		
		}
		else if(!isEmpty() && word.equals(value[0].token)){
				return true;
		}
		
		return false;
		
	}
	
	//Checks if word exists, adds if not, gets if it does
	public Word seek(Word word){
		return seek(word, 0);
	}
	
	private Word seek(Word word, int next){
		
		if(word.token.length() > next){
			char n = word.token.charAt(next);
			
			for(Trie child:children)
				if(child.key == n)
					return child.seek(word, next+1);
		
		}
		else if(!isEmpty() && word.equals(value[0].token)){
				
			boolean match = false;
			for(Word v:value){
				
				if(word.tag == v.tag){
					match = true;
					word = v;
					break;
				}
				
			}
			
			if(match)
				return word;
			else{
				push(word);
				return null;
			}
			
		}
		
		push(word);
		return null;
		
	}
	
	public String printAll(){
		
		String out = "";
		
		if(!isEmpty()){
			out+=value[0].token+"\n";
		}
		for(Trie child:children)
			out+=child.printAll();
			
		return out;
		
	}

	public void add(Word word) {
		add(word, word.token);
	}
	
	private void add(Word word, String token){
		if(token.isEmpty()){
			boolean added=false;
			for(int i = 0; i < value.length; i++){
				
				if(value[i].tag == word.tag){
					value[i] = word;
					added = true;
				}
				
			}
			if(!added){
				push(word);
			}
		}else{
			boolean hasChild = false;
			for(Trie child:children){
				if(child.key == token.charAt(0)){
					child.add(word, token.substring(1));
					hasChild = true;
				}	
			}
			if(!hasChild){
				add(word, token, this);
			}
		}
	}
	
	private void push(Word word){
		
		Word[] nvals = new Word[value.length+1];
		int i;
		for(i = 0; i < value.length; i++){
			nvals[i] = value[i];
		}
		nvals[i] = word;
		value = nvals;
		
		System.out.println("Added " + value[value.length-1]);
		
	}
	
	private Trie newChild(char c){
		
		Trie node = new Trie(c);
		node.parent = this;
		this.children.add(node);
		
		children.sort(this);
		
		return node;
		
	}
	
	private void add(Word word, String token, Trie parent){
		Trie current = parent;
		while(!token.isEmpty()){
			
			char next = token.charAt(0);
			token = token.substring(1);
			current = current.newChild(next);
			
		}
		current.push(word);
		
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		return String.valueOf(((Trie)arg0).key).compareTo(String.valueOf(((Trie)arg1).key)); 
	}

	public Integer occurrence(String word) {
		
		Word[] words = get(word);
		
		if(words == null)
			return 0;
		
		int total = 0;
		for(Word w:words)
			total += w.occurrence;
		
		return total;
	}
	
	public int longest(){
		
		int max = 0;
		
		if(!isEmpty()){
			max=value[0].token.length();
		}
		for(Trie child:children)
			max=Math.max(max, child.longest());
			
		return max;
		
	}
	
	public LinkedList<String> getTokens(){
		
		LinkedList<String> out = new LinkedList<String>();
		
		if(!isEmpty()){
			out.push(value[0].token);
		}
		for(Trie child:children)
			out.addAll(child.getTokens());
			
		return out;
		
	}
	
}
