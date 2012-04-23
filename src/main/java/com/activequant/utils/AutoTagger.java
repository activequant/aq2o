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
			
			
			// 
			l = br.readLine();
		}
	}
	
	public AutoTagger(){
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AutoTagger();
	}

}
