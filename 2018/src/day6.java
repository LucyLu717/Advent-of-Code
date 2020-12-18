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
 	Using only the Manhattan distance, determine the area around each 
 	coordinate by counting the number of integer X,Y locations that are 
 	closest to that coordinate (and aren't tied in distance to any other 
 	coordinate).

	Your goal is to find the size of the largest area that isn't infinite. 
	For example, consider the following list of coordinates:

	1, 1
	1, 6
	8, 3
	3, 4
	5, 5
	8, 9

	If we name these coordinates A through F, we can draw them on a grid, 
	putting 0,0 at the top left:

	..........
	.A........
	..........
	........C.
	...D......
	.....E....
	.B........
	..........
	..........
	........F.

	This view is partial - the actual grid extends infinitely in all 
	directions. Using the Manhattan distance, each location's closest 
	coordinate can be determined, shown here in lowercase:

	aaaaa.cccc
	aAaaa.cccc
	aaaddecccc
	aadddeccCc
	..dDdeeccc
	bb.deEeecc
	bBb.eeee..
	bbb.eeefff
	bbb.eeffff
	bbb.ffffFf

	Locations shown as . are equally far from two or more coordinates, and 
	so they don't count as being closest to any.

	In this example, the areas of coordinates A, B, C, and F are infinite - 
	while not shown here, their areas extend forever outside the visible grid. 
	However, the areas of coordinates D and E are finite: D is closest to 9 
	locations, and E is closest to 17 (both including the coordinate's 
	location itself). Therefore, in this example, the size of the largest 
	area is 17.

	What is the size of the largest area that isn't infinite?

	==============================

	On the other hand, if the coordinates are safe, maybe the best you can 
	do is try to find a region near as many coordinates as possible.

	For example, suppose you want the sum of the Manhattan distance to all 
	of the coordinates to be less than 32. For each location, add up the 
	distances to all of the given coordinates; if the total of those 
	distances is less than 32, that location is within the desired region. 
	Using the same coordinates as above, the resulting region looks like 
	this:

	..........
	.A........
	..........
	...###..C.
	..#D###...
	..###E#...
	.B.###....
	..........
	..........
	........F.

	This region, which also includes coordinates D and E, has a total 
	size of 16.

	Your actual region will need to be much larger than this example, 
	though, instead including all locations with a total distance of less 
	than 10000.

	What is the size of the region containing all locations which have a 
	total distance to all given coordinates of less than 10000?

 */

public class day6 {

	public static void main(String[] args) throws IOException {
		String filename = "files/input6.txt";
		BufferedReader br = getFile(filename);

		HashSet<Point> points = new HashSet<Point>();
		HashMap<Point, Integer> map = new HashMap<Point, Integer>();
		getPointSet(br, points);
		getCount(map, points);
		Point thepoint = findMaxKey(map);
		System.out.println(thepoint.x);
		System.out.println(thepoint.y);
		System.out.println(findMaxVal(map));
		System.out.println(getDistance(points));

		br.close();
	}

	private static BufferedReader getFile(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		return br;
	}

	// get points set and find max and min x-y coordinates to determine the 
	// range of area to check
	private static Point getPointSet(BufferedReader br, HashSet<Point> points) 
			throws IOException {
		String line = br.readLine();
		int maxx = 0;
		int maxy = 0;
		int minx = Integer.MAX_VALUE; // 67
		int miny = Integer.MAX_VALUE; // 41

		while (line != null) {
			int comma = line.indexOf(',');
			int x = Integer.parseInt(line.substring(0, comma));
			int y = Integer.parseInt(line.substring(comma + 2));

			maxx = Math.max(maxx, x);
			maxy = Math.max(maxy, y);
			minx = Math.min(minx, x);
			miny = Math.min(miny, y);

			Point point = new Point(x, y);
			points.add(point);

			line = br.readLine();
		}

		//		System.out.println(minx);
		//		System.out.println(miny);

		return new Point(maxx, maxy);
	}

	private static void getCount(HashMap<Point, Integer> map, 
			HashSet<Point> points) {
		for (int x = 67; x <= 357; x++) {
			for (int y = 41; y <= 353; y++) {
				int min_distance = Integer.MAX_VALUE;
				Point minp = new Point();

				for (Point p: points) {
					int dist = Math.abs(x - p.x) + Math.abs(y - p.y);
					if (dist < min_distance) {
						min_distance = dist;
						minp = p;
					}
				}

				// need to take into account the points that are equally 
				// far from multiple points
				boolean isDot = false;
				for (Point p: points) {
					int dist = Math.abs(x - p.x) + Math.abs(y - p.y);
					if (dist == min_distance && p != minp) {
						isDot = true;
						break;
					}
				}

				if (isDot)
					continue;

				if (map.get(minp) != null)
					map.put(minp, map.get(minp) + 1);
				else
					map.put(minp, 1);

				// get rid of the points on edges - these have infinite area
				if (x == 67 || x == 357)
					map.remove(minp);
				if (y == 41 || y == 353)
					map.remove(minp);
			}
		}
	}

	private static int getDistance(HashSet<Point> points) {
		int count = 0;
		
		for (int x = 67; x <= 357; x++) {
			for (int y = 41; y <= 353; y++) {
				int total_dist = 0;
				for (Point p: points) {
					total_dist += Math.abs(x - p.x) + Math.abs(y - p.y);
					if (total_dist >= 10000)
						break;
				}
				
				if (total_dist < 10000)
					count++;
			}
		}
		return count; 
	}
	
	
	// a helper function that is the key with the max value in [map]
	private static Point findMaxKey(HashMap<Point, Integer> map) {
		int max_value = 0;
		Point max_key = null;

		for (Point i : map.keySet()) {
			int value = map.get(i);
			if (value > max_value) {
				max_value = value;
				max_key = i;
			}
		}
		return max_key;
	}

	// a helper function that is the key with the max value in [map]
	private static int findMaxVal(HashMap<Point, Integer> map) {
		int max_value = 0;

		for (Point i : map.keySet()) {
			int value = map.get(i);
			if (value > max_value) {
				max_value = value;
			}
		}
		return max_value;
	}

	private static class Point {
		private int x, y;

		public Point() {
		}

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
