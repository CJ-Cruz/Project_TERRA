package data.utils;

import java.util.Random;

public class Answer{
	
	private Word[] reply;
	
	public Answer(Word[] r){
		reply = r;
	}
	
	public String makeReply(){
		
		Random rand = new Random();
		String replyString = "";
		for(Word r: reply)
			if(r.synonyms.size() == 0)
				replyString += r.token + " ";
			else
				replyString += (rand.nextBoolean()?r.token:r.getRandomSyn()) + " ";

		replyString = replyString.replaceAll(" \\.", ".");
		replyString = replyString.replaceAll(" \\,", ",");
		replyString = replyString.replaceAll(" \\?", "?");
		
		return replyString;
		
	}
	
}
