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
 	The whole piece of fabric they're working on is a very large square - 
 	at least 1000 inches on each side.

	Each Elf has made a claim about which area of fabric would be ideal 
	for Santa's suit. All claims have an ID and consist of a single 
	rectangle with edges parallel to the edges of the fabric. 
	Each claim's rectangle is defined as follows:
	
	The number of inches between the left edge of the fabric and the left 
		edge of the rectangle.
	The number of inches between the top edge of the fabric and the top 
		edge of the rectangle.
	The width of the rectangle in inches.
	The height of the rectangle in inches.
	
	A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a 
	rectangle 3 inches from the left edge, 2 inches from the top edge, 
	5 inches wide, and 4 inches tall. Visually, it claims the square 
	inches of fabric represented by # (and ignores the square inches 
	of fabric represented by .) in the diagram below:
	
	...........
	...........
	...#####...
	...#####...
	...#####...
	...#####...
	...........
	...........
	...........
	
	The problem is that many of the claims overlap, causing two or more 
	claims to cover part of the same areas. For example, consider the 
	following claims:
	
	#1 @ 1,3: 4x4
	#2 @ 3,1: 4x4
	#3 @ 5,5: 2x2
	
	Visually, these claim the following areas:
	
	........
	...2222.
	...2222.
	.11XX22.
	.11XX22.
	.111133.
	.111133.
	........
	The four square inches marked with X are claimed by both 1 and 2. 
	(Claim 3, while adjacent to the others, does not overlap either of them.)
	
	If the Elves all proceed with their own plans, none of them will have 
	enough fabric. How many square inches of fabric are within two or more claims?
	
	======================
	
	Amidst the chaos, you notice that exactly one claim doesn't overlap by 
	even a single square inch of fabric with any other claim. If you can 
	somehow draw attention to it, maybe the Elves will be able to make 
	Santa's suit after all!

	For example, in the claims above, only claim 3 is intact after all claims are made.
	
	What is the ID of the only claim that doesn't overlap?
 */

public class day3 {

	public static void main(String[] args) throws IOException {
		String filename = "files/input3.txt";
		BufferedReader br = getFile(filename);

		findOverlap(br);

		br.close();

	}

	private static BufferedReader getFile(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);

		return br;
	}

	private static void findOverlap(BufferedReader br) throws IOException {
		String line = br.readLine();
		// there are 1373 lines
		// dots contained in at least one claim
		HashSet<Pair> master = new HashSet<Pair>();
		// dots contained in at least two claims
		HashSet<Pair> repeat = new HashSet<Pair>();
		// maps the dots covered by a claim with id to its id
		HashMap<HashSet<Pair>, Integer> map = new HashMap<HashSet<Pair>, Integer>();
		// claims with overlaps with others
		HashSet<Integer> flag = new HashSet<Integer>();

		int total = 0;

		while (line != null) {
			HashSet<Pair> dots = parseInput(line, map);
			int id = map.get(dots);
			for (Pair p : dots) {
				if (master.contains(p)) {
					flag.add(id);
					if (!repeat.contains(p)) {
						total++;
						repeat.add(p);
					}
				}
				else
					master.add(p);
			}
			line = br.readLine();
		}
		
		System.out.println(total);

		// have left those that cover new area first but have overlaps later
		
		map.values().removeAll(flag);
		HashSet<Integer> escape = new HashSet<Integer>();

		for (HashSet<Pair> dots : map.keySet()) {
			for (Pair p : dots)
				if (repeat.contains(p)) {
					escape.add(map.get(dots));
					break;
				}
		}
		
		map.values().removeAll(escape);
		
		System.out.print(map.values());
		
	}

	/* template: #123 @ 3,2: 5x4 */
	private static HashSet<Pair>
	parseInput(String s, HashMap<HashSet<Pair>, Integer> map) {
		int jing = s.indexOf('#');
		int at = s.indexOf('@');
		int comma = s.indexOf(',');
		int colon = s.indexOf(':');
		int times = s.indexOf('x');

		int id = Integer.parseInt(s.substring(jing+1, at-1));
		int fromleft = Integer.parseInt(s.substring(at+2, comma));
		int fromtop = Integer.parseInt(s.substring(comma+1, colon));
		int width = Integer.parseInt(s.substring(colon+2, times));
		int height = Integer.parseInt(s.substring(times+1));

		HashSet<Pair> dots = new HashSet<Pair>();

		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				dots.add(new Pair(w+fromleft, h+fromtop));
			}
		}

		map.put(dots, id);

		return dots;
	}

	private static class Pair {
		private int x, y;

		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object p1) {
			if (p1 == this) {
				return true;
			}
			if (!(p1 instanceof Pair)) {
				return false;
			}

			Pair p = (Pair)p1;

			return x == p.x && y == p.y; 
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * x + 37 * y;
			return result;
		}
	}
}
