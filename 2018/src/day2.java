import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

/*
	To make sure you didn't miss any, you scan the likely candidate boxes again, 
	counting the number that have an ID containing exactly two of any letter 
	and then separately counting those with exactly three of any letter. 
	You can multiply those two counts together to get a rudimentary checksum 
	and compare it to what your device predicts.

	For example, if you see the following box IDs:
	
	abcdef contains no letters that appear exactly two or three times.
	bababc contains two a and three b, so it counts for both.
	abbcde contains two b, but no letter appears exactly three times.
	abcccd contains three c, but no letter appears exactly two times.
	aabcdd contains two a and two d, but it only counts once.
	abcdee contains two e.
	ababab contains three a and three b, but it only counts once.
	
	Of these box IDs, four of them contain a letter which appears exactly 
	twice, and three of them contain a letter which appears exactly three 
	times. Multiplying these together produces a checksum of 4 * 3 = 12.
	
	What is the checksum for your list of box IDs?
	
	======================

	The boxes will have IDs which differ by exactly one character at the 
	same position in both strings. For example, given the following box IDs:

		abcde
		fghij
		klmno
		pqrst
		fguij
		axcye
		wvxyz
	
	The IDs abcde and axcye are close, but they differ by two characters 
	(the second and fourth). However, the IDs fghij and fguij differ by 
	exactly one character, the third (h and u). Those must be the correct boxes.
	
	What letters are common between the two correct box IDs? 
	(In the example above, this is found by removing the differing 
	character from either ID, producing fgij.)

 */

public class day2 {

	public static void main(String[] args) throws Exception {
		String filename = "files/input2.txt";
		BufferedReader br = getFile(filename);
		checkSum(br);
		
		br = getFile(filename);
		findCommon(br);
		br.close();
	}
	
	private static BufferedReader getFile(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		
		return br;
	}
	
	private static void checkSum(BufferedReader br) throws IOException {
		int[] checksum = new int[2];
		String line = br.readLine();

		while (line != null) {
			boolean[] vector = processString(line);
			if (vector[0])
				checksum[0]++;
			if (vector[1])
				checksum[1]++;
			line = br.readLine();
		}
		
		System.out.println(checksum[0] * checksum[1]);
	}
	
	private static boolean[] processString(String s) {
		boolean[] vector = new boolean[2];
		
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if (map.get(c) != null)
				map.put(c, map.get(c) + 1);
			else
				map.put(c, 1);
		}
		
		if (map.containsValue(2)) 
			vector[0] = true; 
		else
			vector[0] = false;
		
		if (map.containsValue(3)) 
			vector[1] = true; 
		else
			vector[1] = false;
		
		return vector;
	}
	
	private static void findCommon(BufferedReader br) throws Exception {
		String line = br.readLine();
		HashSet<String> set = new HashSet<String>();
		int length;
		if (line != null)
			length = line.length();
		else
			throw new Exception("Empty File");
		
		while (line != null) {
			set.add(line);
			line = br.readLine();
		}
		
		for (int i = 0; i < length; i++) {
			HashSet<String> miniset = new HashSet<String>();
			for (String s : set) {
				String ministr = s.substring(0, i) + s.substring(i+1);
				if (miniset.contains(ministr)) {
					System.out.println(ministr);
					System.exit(0);
				} else {
					miniset.add(ministr);
				}
					
			}
			
		}
			
	}
}
