import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
/*
 	The pots are numbered, with 0 in front of you. To the left, the pots are 
 	numbered -1, -2, -3, and so on; to the right, 1, 2, 3.... Your puzzle 
 	input contains a list of pots from 0 to the right and whether they do (#) 
 	or do not (.) currently contain a plant, the initial state. (No other pots 
 	currently contain plants.) For example, an initial state of #..##.... 
 	indicates that pots 0, 3, and 4 currently contain plants.

	Your puzzle input also contains some notes you find on a nearby table: 
	someone has been trying to figure out how these plants spread to nearby 
	pots. Based on the notes, for each generation of plants, a given pot has 
	or does not have a plant based on whether that pot (and the two pots on 
	either side of it) had a plant in the last generation. These are written 
	as LLCRR => N, where L are pots to the left, C is the current pot being 
	considered, R are the pots to the right, and N is whether the current pot 
	will have a plant in the next generation. For example:
	
	A note like ..#.. => . means that a pot that contains a plant but with no 
		plants within two pots of it will not have a plant in it during the 
		next generation.
	A note like ##.## => . means that an empty pot with two plants on each 
		side of it will remain empty in the next generation.
	A note like .##.# => # means that a pot has a plant in a given generation 
		if, in the previous generation, there were plants in that pot, the one 
		immediately to the left, and the one two pots to the right, but not in 
		the ones immediately to the right and two to the left.
	It's not clear what these plants are for, but you're sure it's important, 
		so you'd like to make sure the current configuration of plants is 
		sustainable by determining what will happen after 20 generations.
	
	In this example, after 20 generations, the pots shown as # contain plants, 
	the furthest left of which is pot -2, and the furthest right of which is 
	pot 34. Adding up all the numbers of plant-containing pots after the 20th 
	generation produces 325.
	
	After 20 generations, what is the sum of the numbers of all pots which 
	contain a plant?
		
	======================
	
	You realize that 20 generations aren't enough. After all, these plants will 
	need to last another 1500 years to even reach your time line, not to mention 
	your future.
	
	After fifty billion (50000000000) generations, what is the sum of the 
	numbers of all pots which contain a plant?
*/

/*
GOOD PRACTICE:
	- indexOf + 1 in substring to get rid of the symbol character
	- need to pad before and after the string to include possibilities at the 
		beginning and the end 
*/

public class day12 {
	
	// pattern does not change from generation 97
	// sum is 8898 at generation 100, increase 81 each generation
	public static final long GEN = 100;
	
	public static void main(String[] args) throws Exception {
//		(8898 + (50000000000 - 100) * 81) = 4050000000798
		
		String filename = "files/input12.txt";
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);

		String line = br.readLine();
		String initial = 
				"..." + line.substring(line.indexOf(":") + 1).trim() + "..";
		
		HashSet<String> yes = new HashSet<String>();
		HashSet<String> no = new HashSet<String>();
		
		line = br.readLine();
		line = br.readLine();
		while (line != null) {
			int index = line.indexOf('>');
			char pot = line.substring(index + 1).trim().charAt(0);
			if (pot == '#')
				yes.add(line.substring(0, index - 1).trim());
			else if (pot == '.')
				no.add(line.substring(0, index - 1).trim());
			else 
				throw new Exception("Unrecognized Pot Category: " + pot);
			line = br.readLine();
		}
		br.close();
		ir.close();
		is.close();
		
		transfer(initial, yes, no);
	}

	private static void transfer 
	(String initial, HashSet<String> yes, HashSet<String> no) {
		int gen = 0;
		int length = initial.length();			

		while (gen < GEN) {
			char[] new_init = initializeCharArr(length);
				
			for (int i = 0; i < length - 5; i++) {
				String section = initial.substring(i, i + 5);
				if (yes.contains(section))
					new_init[i+2] = '#';
				else if (no.contains(section))
					new_init[i+2] = '.';
			}
			
			System.out.println(initial);
			initial = toString(new_init) + ".";
			length += 1;
			gen++;
		}
		
		long total = 0;
		for (int i = 0; i < length; i++)
			if (initial.charAt(i) == '#')
				total += i - 3;
		System.out.println(total);
	}
	
	private static String toString (char[] new_init) {
		String res = "";
		for (char c : new_init)
			res += c;
		return res;
	}
	
	private static char[] initializeCharArr (int length) {
		char[] new_init = new char[length];
		for(int i = 0; i < length; i++)
			new_init[i] = '.';
		return new_init;
	}
}
