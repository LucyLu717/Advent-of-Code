import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
	The polymer is formed by smaller units which, when triggered, react with 
	each other such that two adjacent units of the same type and opposite 
	polarity are destroyed. Units' types are represented by letters; units' 
	polarity is represented by capitalization. For instance, r and R are units 
	with the same type but opposite polarity, whereas r and s are entirely 
	different types and do not react.
	
	For example:
	
	In aA, a and A react, leaving nothing behind.
	In abBA, bB destroys itself, leaving aA. As above, this then destroys 
	itself, leaving nothing.
	In abAB, no two adjacent units are of the same type, and so nothing happens.
	In aabAAB, even though aa and AA are of the same type, their polarities 
	match, and so nothing happens.
	Now, consider a larger example, dabAcCaCBAcCcaDA:
	
		dabAcCaCBAcCcaDA  The first 'cC' is removed.
		dabAaCBAcCcaDA    This creates 'Aa', which is removed.
		dabCBAcCcaDA      Either 'cC' or 'Cc' are removed (the result is the same).
		dabCBAcaDA        No further actions can be taken.
	
	After all possible reactions, the resulting polymer contains 10 units.
	
	How many units remain after fully reacting the polymer you scanned?
	
	==============================
	
	Time to improve the polymer.

	One of the unit types is causing problems; it's preventing the polymer 
	from collapsing as much as it should. Your goal is to figure out which 
	unit type is causing the most problems, remove all instances of it 
	(regardless of polarity), fully react the remaining polymer, and measure 
	its length.
	
	For example, again using the polymer dabAcCaCBAcCcaDA from above:
	
		Removing all A/a units produces dbcCCBcCcD. Fully reacting this polymer 
		produces dbCBcD, which has length 6.
		
		Removing all B/b units produces daAcCaCAcCcaDA. Fully reacting this 
		polymer produces daCAcaDA, which has length 8.
		
		Removing all C/c units produces dabAaBAaDA. Fully reacting this polymer 
		produces daDA, which has length 4.
		
		Removing all D/d units produces abAcCaCBAcCcaA. Fully reacting this 
		polymer produces abCBAc, which has length 6.
		
		In this example, removing all C/c units was best, producing the answer 4.
	
	What is the length of the shortest polymer you can produce by removing 
	all units of exactly one type and fully reacting the result?
 */

public class day5 {
	
	private static int position = 0;
	
	public static void main(String[] args) throws IOException {
		String filename = "files/input5.txt";
		String polymer = getPolymer(filename); // length 50000
		System.out.println(react(polymer));	
		System.out.println(optimize(polymer));
	}
	
	private static int optimize(String polymer) {
		int min = polymer.length();
		for (int i = 0; i < 26; i++) {
			String new_polymer = polymer.replace("" + (char)('a' + i), "");
			new_polymer = new_polymer.replace("" + (char)('A' + i), "");

			int length = react(new_polymer);
			if (length < min)
				min = length;
		}
		
		return min;
	}
	
	private static int react(String polymer) {
		String new_polymer = findReaction(polymer);
		while (!polymer.equals(new_polymer)) {
			polymer = new_polymer;
			new_polymer = findReaction(polymer);
		}
		
		return polymer.length();
	}
	
	private static String getPolymer(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		String polymer = br.readLine();
		
		br.close();
		ir.close();
		is.close();
		
		return polymer;
	}
	
	private static String findReaction(String polymer) {
		for (int i = position; i < polymer.length() - 1; i++) {
			if (Math.abs(polymer.charAt(i) - polymer.charAt(i+1)) == ('a' - 'A')) {
				if (position != 0) //IMPORTANT
					position = i-1;
				return polymer.substring(0, i) + polymer.substring(i+2);
			}
		}
		
		return polymer;
	}
	
}
