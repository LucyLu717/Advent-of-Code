import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.nio.file.Path;
import java.nio.file.Files;

/*
	After feeling like you've been falling for a few minutes, 
	you look at the device's tiny screen. "Error: Device must be calibrated 
	before first use. Frequency drift detected. Cannot maintain destination 
	lock." Below the message, the device shows a sequence of changes in 
	frequency (your puzzle input). A value like +6 means the current frequency 
	increases by 6; a value like -3 means the current frequency decreases by 3.

	For example, if the device displays frequency changes of +1, -2, +3, +1, 
	then starting from a frequency of zero, the following changes would occur:
    Current frequency  0, change of +1; resulting frequency  1.
	Current frequency  1, change of -2; resulting frequency -1.
	Current frequency -1, change of +3; resulting frequency  2.
	Current frequency  2, change of +1; resulting frequency  3.
	In this example, the resulting frequency is 3.
	
   Here are other example situations:
	
	+1, +1, +1 results in  3
	+1, +1, -2 results in  0
	-1, -2, -3 results in -6
	
	Starting with a frequency of zero, what is the resulting frequency after 
	all of the changes in frequency have been applied?

	======================

	For example, using the same list of changes above, the device would loop as follows:

	Current frequency  0, change of +1; resulting frequency  1.
	Current frequency  1, change of -2; resulting frequency -1.
	Current frequency -1, change of +3; resulting frequency  2.
	Current frequency  2, change of +1; resulting frequency  3.
	(At this point, the device continues from the start of the list.)
	Current frequency  3, change of +1; resulting frequency  4.
	Current frequency  4, change of -2; resulting frequency  2, 
	which has already been seen.
	
	In this example, the first frequency reached twice is 2. 
	Note that your device might need to repeat its list of frequency changes 
	many times before a duplicate frequency is found, and that duplicates 
	might be found while in the middle of processing the list.
	
	Here are other examples:
	
	+1, -1 first reaches 0 twice.
	+3, +3, +4, -2, -4 first reaches 10 twice.
	-6, +3, +8, +5, -6 first reaches 5 twice.
	+7, +7, -2, -7, -4 first reaches 14 twice.
	What is the first frequency your device reaches twice?
*/

public class day1 {
	public static void main(String[] args) throws IOException{
		String filename = "files/input.txt";
		BufferedReader br = getFile(filename);
		findFreq(br);
		
		br = getFile(filename);
		findRepeatFreq(br);
		
		br.close();
	}
	
	private static BufferedReader getFile(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		
		return br;
	}
	
	public static void findFreq(BufferedReader br) throws IOException {
		String line = br.readLine();
		int total = 0;
		
		while (line != null) {
			char op = line.charAt(0);
			String numbers = line.substring(1);
			int number = Integer.parseInt(numbers);
			
			total = computation(total, op, number);
			
			line = br.readLine();	
		}
		
		System.out.println(total);
	}
	
	private static int computation(int total, char op, int number) {
		if (op == '+')
			total += number;
		else if (op == '-')
			total -= number;
		else
			throw new IllegalArgumentException("Unrecognized Operator");
		
		return total;
	}
		
	public static void findRepeatFreq(BufferedReader br) throws IOException {
		String line = br.readLine();
		int total = 0;
		HashSet<Integer> set = new HashSet<Integer>();
		set.add(0);
		
		while(true) {
			while (line != null) {
				char op = line.charAt(0);
				String numbers = line.substring(1);
				int number = Integer.parseInt(numbers);
				
				total = computation(total, op, number);
				if (set.contains(total)) {
					System.out.println(total);
					System.exit(0);
				}	
				else
					set.add(total);
				
				line = br.readLine();
			}
			
			br = getFile("files/input.txt");
			line = br.readLine();
		}
		
		
	}
}
