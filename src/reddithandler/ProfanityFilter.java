package reddithandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ProfanityFilter {
	
    static Map<String, String[]> words = new HashMap<>();
	    
	static int largestWordLength = 0;
	
	public static void loadProfanityList() {
		 try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://docs.google.com/spreadsheets/d/1hIEi2YG3ydav1E06Bzf2mQbGZ12kh2fe4ISgLg_UBuM/export?format=csv").openConnection().getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] content = null;
                try {
                    content = line.split(",");
                    if(content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    String[] ignore_in_combination_with_words = new String[]{};
                    if(content.length > 1) {
                        ignore_in_combination_with_words = content[1].split("_");
                    }

                    if(word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

                } catch(Exception e) {
                    e.printStackTrace();
                }
	        }
	      } 
		  catch (IOException e) {
	            e.printStackTrace();
	      }

	}
	
	public static String sanitizeText(String input) {
		Iterator<Entry<String, String[]>> itr = words.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, String[]> swearWordEntry = itr.next();
			String swearWord = swearWordEntry.getKey();
			String replacementString = "";
			//Replace every letter after the first in the swear word with asterisks
			for(int i=1;i<swearWord.length();i++) {
				replacementString+="*";
			}
			//This whole loop is to find swear words that have different cases
			while(input.toLowerCase().contains(swearWord)) {
				int indexOfActualWord = input.toLowerCase().indexOf(swearWord);
				String actualWordCase = input.substring(indexOfActualWord,indexOfActualWord+swearWord.length());
				//Get the actual first letter's case and then put the asterisks after it
				input = input.replace(actualWordCase, actualWordCase.charAt(0)+replacementString);
			}
		}
		return input;
	}

}
