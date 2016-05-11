package main;

public interface TERRA {

	// File Sources
	final String XML_DICTIONARY = "dictionary.xml";
	final String XML_GRAMMAR = "grammar.xml";
	final String XML_KNOWLEDGE = "knowledge/";	//file directory of knowledge xml files
	final String[] DEFAULT_KNOWLEDGE = {"base", 
										"facts",
										"s1_offline", 
										"cancelfeat_off", 
										"regdates_on",
										"stragglers_off",
										"zerounits",
										"finalize_off"
																};
	
	// Lexical Analysis
	final int SPELLDISTANCE = 5;
	
	// Keyword Analysis
	final double THRESHOLD_SCORE = 0.75;
	final int THRESHOLD_KEYS = 2;

	//	Reply Generation
	final boolean ALWAYSREPLY = false;
	final String REPLY_NO_MATCHED = "Please clarify in private message.";
	
}
