package com.activequant.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AutoTagger {

	class TagDesc{
		String tableName; 
		String pattern; 
		String[] tagValues;
	}
	
	private String tagDescriptionFile = "tags.def";
	private List<TagDesc> tagDescs = new ArrayList<TagDesc>();
	
	private void parseTagDescFile() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(AutoTagger.class.getResourceAsStream(tagDescriptionFile)));
		String l = br.readLine();
		while(l!=null){
			// 
			if(!l.startsWith("#"))
			{
				String[] p = l.split(";");
				// ok, parse it. 
				String tableName = p[0];
				String pattern = p[1];
				String[] tags = p[2].split(" ");
				TagDesc td = new TagDesc();
				td.tableName = tableName;
				td.pattern = pattern; 
				td.tagValues = tags; 
				tagDescs.add(td);
			}
			// 
			l = br.readLine();
		}
		
	}
	
	public AutoTagger() throws IOException{
		parseTagDescFile();
		for(TagDesc td : tagDescs){
			process(td);
		}
	}
	
	private void process(TagDesc td){
		// find all IDs that match
		
		
		
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new AutoTagger();
	}

}
