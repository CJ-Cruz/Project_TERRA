package utils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import data.bases.Dictionary;
import data.bases.GrammarBase;
import data.bases.KnowledgeBase;
import data.utils.Answer;
import data.utils.Question;
import data.utils.Word;
import main.TERRA;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.util.LinkedList;

public class XMLParser implements TERRA{
	
	public static final String kb_dir = TERRA.XML_KNOWLEDGE;
	
	public static Dictionary parseDict(String data){
		
		Dictionary out = new Dictionary();
		
		File xmlC = new File(data);
		DocumentBuilderFactory dbF= DocumentBuilderFactory.newInstance();
		DocumentBuilder dB;
		try {
			dB = dbF.newDocumentBuilder();
			Document dE = dB.parse(xmlC);
			dE.getDocumentElement().normalize();
			
			NodeList wlist = dE.getElementsByTagName("word");
			int wll = wlist.getLength();
			for(int wi = 0; wi < wll; wi++){
				
				Node word = wlist.item(wi);
				
				if(word.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				Element eword = (Element) word;
				
//				System.out.println("Word: "+eword.getAttribute("token"));
				
				NodeList elist = word.getChildNodes();
				int ell = elist.getLength();
				for(int ei = 0; ei < ell; ei++){
					
					Node entry = elist.item(ei);
					
					if(entry.getNodeType() != Node.ELEMENT_NODE)
						continue;
						
					Element eentry = (Element) entry;
					
//					System.out.println("Tag: "+eentry.getAttribute("tag"));
					Word temp = new Word(eword.getAttribute("token"), Dictionary.getTag(eentry.getAttribute("tag")));
					String l = eentry.getAttribute("lang");
					if(l == ""){
						out.list[0].add(temp);
						out.list[1].add(temp);
					}
					else{
						int lang = Integer.parseInt(l)-1;
						if(lang != -1){
							out.list[lang].add(temp);
						}else System.exit(0);
					}
					NodeList clist = entry.getChildNodes();
					int cll = clist.getLength();
					for(int ci = 0; ci < cll; ci++){
						
						Node child = clist.item(ci);
						
						if(child.getNodeType() != Node.ELEMENT_NODE)
							continue;
						
						Element echild = (Element) child;
						
						if(echild.getTagName() == "translations"){
							
							NodeList tlist = child.getChildNodes();
							int tll = tlist.getLength();
							for(int ti = 0; ti < tll; ti++){
								
								Node tentry = tlist.item(ti);
								
								if(tentry.getNodeType() != Node.ELEMENT_NODE)
									continue;
								
								Element etentry = (Element) tentry;
								
								Word trans = new Word(tentry.getTextContent(), Dictionary.getTag(etentry.getAttribute("tag")));
								System.out.println("Trans: "+tentry.getTextContent()+" ("+etentry.getAttribute("tag")+")");
								temp.translations.add(trans);
								
							}

						}
						else if(echild.getTagName() == "synonyms"){
							
							String syns = echild.getTextContent();

							String[] syn = syns.split(",");
							
							for(String s:syn)
								if(!temp.synonyms.contains(s))
									temp.synonyms.add(s);
							
						}
						
					}
					
				}
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}

	public static GrammarBase parseGB(String data){

		GrammarBase out = new GrammarBase();
		
		File xmlC = new File(data);
		DocumentBuilderFactory dbF= DocumentBuilderFactory.newInstance();
		DocumentBuilder dB;
		try {
			dB = dbF.newDocumentBuilder();
			Document dE = dB.parse(xmlC);
			dE.getDocumentElement().normalize();
			
			NodeList elist = dE.getElementsByTagName("entry");
			int ell = elist.getLength();
			for(int ei = 0; ei < ell; ei++){
				
				Node entry = elist.item(ei);
				
				if(entry.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				Element eentry = (Element) entry;
				
				System.out.println("Sentence: "+eentry.getAttribute("sentence"));
				NodeList llist = entry.getChildNodes();
				int lll = llist.getLength();
				LinkedList<Integer> tags = new LinkedList<Integer>();
				for(int li = 0; li < lll; li++){
					
					Node lex = llist.item(li);
					
					if(lex.getNodeType() != Node.ELEMENT_NODE)
						continue;
						
					Element lexeme = (Element) lex;
					
					System.out.println("Tag: "+lexeme.getAttribute("tag"));
					tags.add(Dictionary.getTag(lexeme.getAttribute("tag")));
					
				}
				
				int[] ts = new int[tags.size()];
				for(int i = 0; i < tags.size(); i++)
					ts[i] = tags.get(i).intValue();
				out.add(ts);
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	public static KnowledgeBase parseKB(String data, Dictionary dict, GrammarBase gram, KnowledgeBase kb){

		KnowledgeBase out = kb;
		
		File xmlC = new File(kb_dir+data);
		DocumentBuilderFactory dbF= DocumentBuilderFactory.newInstance();
		DocumentBuilder dB;
		try {
			dB = dbF.newDocumentBuilder();
			Document dE = dB.parse(xmlC);
			dE.getDocumentElement().normalize();
			
			NodeList elist = dE.getElementsByTagName("entry");
			int ell = elist.getLength();
			LinkedList<Question> qmain = new LinkedList<Question>();
			for(int ei = 0; ei < ell; ei++){
				
				Node entry = elist.item(ei);
				
				if(entry.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				Element eentry = (Element) entry;
				
				
				NodeList llist = entry.getChildNodes();
				int lll = llist.getLength();
				LinkedList<String> qtext = new LinkedList<String>();
				LinkedList<Answer> answers = new LinkedList<Answer>();
				for(int li = 0; li < lll; li++){
					
					Node sub = llist.item(li);
					
					if(sub.getNodeType() != Node.ELEMENT_NODE)
						continue;
						
					Element child = (Element) sub;
					
					if(child.getTagName() == "answer"){
						
						NodeList alist = child.getChildNodes();
						int all = alist.getLength();
						for(int ai = 0; ai < all; ai++){
							
							Node a = alist.item(ai);
							
							if(a.getNodeType() != Node.ELEMENT_NODE)
								continue;
							
							Element answer = (Element) a;
							
							System.out.println("answer: "+a.getTextContent());
							
							answers.add(new Answer(gram.answerAnalysis(dict.answerCheck(a.getTextContent().trim()))));
							
						}
						
					}
					else if(child.getTagName() == "question"){
						
						NodeList qlist = child.getChildNodes();
						int qll = qlist.getLength();
						for(int qi = 0; qi < qll; qi++){
							
							Node q = qlist.item(qi);
							
							if(q.getNodeType() != Node.ELEMENT_NODE)
								continue;
							
							Element question = (Element) q;
							
							System.out.println("question: "+q.getTextContent());
							qtext.add(q.getTextContent().trim());
							
						}
						
						
					}
					
				}
				
				Answer[] ans = answers.toArray(new Answer[answers.size()]);
				LinkedList<Question> questions = new LinkedList<Question>();
				for(String qt:qtext){
					questions.add(new Question(gram.syntaxAnalysis(dict.lexicalAnalysis(qt)), ans));
				}
				qmain.addAll(questions);
				
			}
		
			out.addQuestions(qmain.toArray(new Question[qmain.size()]));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	
	}
	
}
