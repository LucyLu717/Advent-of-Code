import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
/*
	The Elves suggest an alternative. In times like these, North Pole 
	rescue operations will arrange points of light in the sky to guide 
	missing Elves back to base. Unfortunately, the message is easy to 
	miss: the points move slowly enough that it takes hours to align 
	them, but have so much momentum that they only stay aligned for a 
	second. If you blink at the wrong time, it might be hours before 
	another message appears.
	
	You can see these points of light floating in the distance, and 
	record their position in the sky and their velocity, the relative 
	change in position per second (your puzzle input). The coordinates 
	are all given from your perspective; given enough time, those 
	positions and velocities will move the points into a cohesive 
	message!
	
	Rather than wait, you decide to fast-forward the process and 
	calculate what the points will eventually spell.
	
	For example, suppose you note the following points:
	
	position=< 9,  1> velocity=< 0,  2>
	position=< 7,  0> velocity=<-1,  0>
	position=< 3, -2> velocity=<-1,  1>
	position=< 6, 10> velocity=<-2, -1>
	position=< 2, -4> velocity=< 2,  2>
	position=<-6, 10> velocity=< 2, -2>
	position=< 1,  8> velocity=< 1, -1>
	position=< 1,  7> velocity=< 1,  0>
	position=<-3, 11> velocity=< 1, -2>
	position=< 7,  6> velocity=<-1, -1>
	position=<-2,  3> velocity=< 1,  0>
	position=<-4,  3> velocity=< 2,  0>
	position=<10, -3> velocity=<-1,  1>
	position=< 5, 11> velocity=< 1, -2>
	position=< 4,  7> velocity=< 0, -1>
	position=< 8, -2> velocity=< 0,  1>
	position=<15,  0> velocity=<-2,  0>
	position=< 1,  6> velocity=< 1,  0>
	position=< 8,  9> velocity=< 0, -1>
	position=< 3,  3> velocity=<-1,  1>
	position=< 0,  5> velocity=< 0, -1>
	position=<-2,  2> velocity=< 2,  0>
	position=< 5, -2> velocity=< 1,  2>
	position=< 1,  4> velocity=< 2,  1>
	position=<-2,  7> velocity=< 2, -2>
	position=< 3,  6> velocity=<-1, -1>
	position=< 5,  0> velocity=< 1,  0>
	position=<-6,  0> velocity=< 2,  0>
	position=< 5,  9> velocity=< 1, -2>
	position=<14,  7> velocity=<-2,  0>
	position=<-3,  6> velocity=< 2, -1>
	
	Each line represents one point. Positions are given as <X, Y> pairs: 
	X represents how far left (negative) or right (positive) the point 
	appears, while Y represents how far up (negative) or down (positive) 
	the point appears.
	
	At 0 seconds, each point has the position given. Each second, each 
	point's velocity is added to its position. So, a point with velocity 
	<1, -2> is moving to the right, but is moving upward twice as quickly. 
	If this point's initial position were <3, 9>, after 3 seconds, its 
	position would become <6, 3>.
	
	What message will eventually appear in the sky?
		
	============================
	
	Good thing you didn't have to wait, because that would have taken a 
	long time - much longer than the 3 seconds in the example above.
	
	Impressed by your sub-hour communication capabilities, the Elves are 
	curious: exactly how many seconds would they have needed to wait 
	for that message to appear?

*/

/*
	GOOD PRACTICE:
		- use long instead of int for large results
		- override equals AND hashcode methods for HashSet contains method
*/

public class day10 {

	public static final int ROUNDS = 20;
	public static final int CUTOFF = 1000;

	public static void main(String[] args) throws IOException {
		String filename = "files/input10.txt";
		HashSet<Dot> set = getRawInput(filename);
		timeFlies(set);
	}

	private static HashSet<Dot> getRawInput(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);

		HashSet<Dot> set = new HashSet<Dot>();

		String line = br.readLine();

		while (line != null) {
			int pos_start = line.indexOf("<");
			int pos_mid = line.indexOf(",");
			int pos_end = line.indexOf(">");
			int vel_start = line.lastIndexOf("<");
			int vel_end = line.lastIndexOf(">");
			int vel_mid = line.lastIndexOf(",");

			int posx = Integer.parseInt
					(line.substring(pos_start+1, pos_mid).trim());
			int posy = Integer.parseInt
					(line.substring(pos_mid+2, pos_end).trim());
			Position pos = new Position(posx, posy);

			int velx = Integer.parseInt
					(line.substring(vel_start+1, vel_mid).trim());
			int vely = Integer.parseInt
					(line.substring(vel_mid+2, vel_end).trim());
			Velocity vel = new Velocity(velx, vely);
			Dot d = new Dot(pos, vel);
			set.add(d);

			line = br.readLine();
		}

		br.close();
		ir.close();
		is.close();

		return set;
	}

	private static void timeFlies(HashSet<Dot> set) {
		int round = 0;
		while (round < ROUNDS) {
			long min = Long.MAX_VALUE;
			int min_time = 0;
			HashSet<Dot> min_set = new HashSet<Dot>();

			int timer = 0;
			while (timer < CUTOFF) {
				long dist = getTotalDist(set);
				if (dist < min) {
					min = dist;
					min_set = set;
					min_time = timer;
				}
				timer++;
				set = move(set);
			}
			System.out.println(round * CUTOFF + min_time);
			print(min_set);
			round++;
		}
	}

	private static long getTotalDist(HashSet<Dot> set) {
		long total = 0;
		for (Dot d1 : set)
			for (Dot d2 : set) {
				Position p1 = d1.pos;
				Position p2 = d2.pos;
				total += Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
			}
		return total;
	}

	private static HashSet<Dot> move(HashSet<Dot> set) {
		HashSet<Dot> new_set = new HashSet<Dot>();
		
		for (Dot d : set) {
			Velocity v = d.vel;
			Position p = d.pos;
			Position newp = new Position (p.x + v.x, p.y + v.y);
			new_set.add(new Dot(newp, v));
		}
		
		return new_set;
	}

	private static void print(HashSet<Dot> set) {
		HashSet<Position> positions = new HashSet<Position>();
		int maxx = Integer.MIN_VALUE;
		int maxy = Integer.MIN_VALUE;
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;

		for (Dot d : set) {
			Position p = d.pos;
			positions.add(p);
			if (p.x > maxx)
				maxx = p.x;
			else if (p.x < minx)
				minx = p.x;
			if (p.y > maxy)
				maxy = p.y;
			else if (p.y < miny)
				miny = p.y;
		}

		if (maxx - minx > 100 || maxy - miny > 100)
			return;
		
		for (int y = miny; y <= maxy; y++) {
			for (int x = minx; x <= maxx; x++) {
				if (positions.contains(new Position(x, y)))
					System.out.print("#");
				else 
					System.out.print(".");
			}
			System.out.println();
		}
	}

	private static class Dot {
		private Position pos;
		private Velocity vel;

		private Dot (Position p, Velocity v) {
			pos = p;
			vel = v;
		}
	}

	private static class Position {
		private int x, y;

		private Position (int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals (Object p) {
			if (p == this)
				return true;
			if (!(p instanceof Position))
				return false;
			Position pos = (Position)p;
			return x == pos.x && y == pos.y;
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * x + 37 * y;
			return result;
		}
	}

	private static class Velocity {
		private int x, y;

		private Velocity (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

}
