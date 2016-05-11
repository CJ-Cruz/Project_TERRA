package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

import data.utils.Pair;

public class Rules {

	public static Pair<String, String> conversions = new Pair<String,String>();
	public static String[] fillers;
	public static String[] fillerPhrases;
	
	public static void setup(){
		
		loadConversions("conversions.rules");
		loadFillers("fillers.rules");
		loadFillerPhrases("filler_phrases.rules");
		
	}
	
	private static void loadFillerPhrases(String filename) {
		
		LinkedList<String> temp = new LinkedList<String>();
		
		FileReader fr;
		try {
			fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
		
			String line = "";
			while((line = br.readLine())!=null){
				temp.add(line.trim());
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fillerPhrases = temp.toArray(new String[temp.size()]);
		
	}

	public static void main(String[] arg){
		Rules r = new Rules();
	}
	
	public static void loadConversions(String filename){
		conversions.clear();
		
		FileReader fr;
		try {
			fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
		
			String line = "";
			while((line = br.readLine())!=null){
				String[] tok = line.split("=");
				conversions.add(tok[0].trim(), tok[1].trim());
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int s = conversions.size();
		for(int i = 0; i < s; i++){
			conversions.add(((String)conversions.getK(i)).replace("'", ""), conversions.getV(i));
		}
		
	}
	
	private static void loadFillers(String filename){
		
		LinkedList<String> temp = new LinkedList<String>();
		
		FileReader fr;
		try {
			fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
		
			String line = "";
			while((line = br.readLine())!=null){
				temp.add(line.trim());
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fillers = temp.toArray(new String[temp.size()]);
		
	}
	
	public static boolean isFiller(String s){
		
		for(String f:fillers)
			if(s.equals(f))
				return true;
		
		return false;
		
	}
	
	public static String clearFillerPhrases(String corpus){
		
		for(String fp:fillerPhrases){
			
			corpus = corpus.replaceAll(fp, "");
			
		}
		
		return corpus;
		
	}
	
}
