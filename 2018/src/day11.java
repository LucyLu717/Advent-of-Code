/*
 	The low fuel warning light is illuminated on your wrist-mounted device. 
 	Tapping it once causes it to project a hologram of the situation: a 
 	300x300 grid of fuel cells and their current power levels, some negative. 
 	You're not sure what negative power means in the context of time travel, 
 	but it can't be good.

	Each fuel cell has a coordinate ranging from 1 to 300 in both the X 
	(horizontal) and Y (vertical) direction. In X,Y notation, the top-left 
	cell is 1,1, and the top-right cell is 300,1.
	
	The interface lets you select any 3x3 square of fuel cells. To increase 
	your chances of getting to your destination, you decide to choose the 3x3 
	square with the largest total power.
	
	The power level in a given fuel cell can be found through the following 
	process:
	
	Find the fuel cell's rack ID, which is its X coordinate plus 10.
	Begin with a power level of the rack ID times the Y coordinate.
	Increase the power level by the value of the grid serial number 
		(your puzzle input).
	Set the power level to itself multiplied by the rack ID.
	Keep only the hundreds digit of the power level 
		(so 12345 becomes 3; numbers with no hundreds digit become 0).
	Subtract 5 from the power level.
	
	Your goal is to find the 3x3 square which has the largest total power. 
	The square must be entirely within the 300x300 grid. Identify this square 
	using the X,Y coordinate of its top-left fuel cell. For example:
	
	What is the X,Y coordinate of the top-left fuel cell of the 3x3 square 
	with the largest total power?
	
	=============================	

	You discover a dial on the side of the device; it seems to let you select 
	a square of any size, not just 3x3. Sizes from 1x1 to 300x300 are supported.
	
	Realizing this, you now must find the square of any size with the largest 
	total power. Identify this square by including its size as a third 
	parameter after the top-left coordinate: a 9x9 square with a top-left 
	corner of 3,5 is identified as 3,5,9.
	
	What is the X,Y,size identifier of the square with the largest total power?
 */

/*
	GOOD PRACTICE:
		- factor out methods and different parts of the problem
		- draw a simple example on paper to reason about the algorithm
		- spreadsheet approach in database class to optimize
*/

public class day11 {
	public static final int SERIAL = 2187;
	
	public static void main(String[] args) {
	// serial number: 2187
		fixedSize();
		varSize();
	}
	
	private static void fixedSize() {
		long max = Integer.MIN_VALUE;
		int maxx = 0;
		int maxy = 0;
		
		byte[][] map = getMap();
		
		for (int x = 1; x < 299; x++)
			for (int y = 1; y < 299; y++) {
				long total = getTotal3(x, y, map);
				if (total > max) {
					max = total;
					maxx = x;
					maxy = y;
				}	
			}
		System.out.println(max);
		System.out.println(maxx);
		System.out.println(maxy);
	}
	
	private static void varSize() {
		long max = Integer.MIN_VALUE;
		int maxx = 0;
		int maxy = 0;
		int maxs = 0;
		
		byte[][] map = getMap();
		
		for (int x = 1; x < 299; x++)
			for (int y = 1; y < 299; y++) {
				Result res = getTotal(x, y, map);
				long total = res.total;
				if (total > max) {
					max = total;
					maxx = x;
					maxy = y;
					maxs = res.max_size;
				}	
			}
		System.out.println(max);
		System.out.println(maxx);
		System.out.println(maxy);
		System.out.println(maxs);
	}
	
	private static byte[][] getMap() {
		byte[][] map = new byte[301][301];
		for (int x = 1; x <= 300; x++)
			for (int y = 1; y <= 300; y++)
				map[x][y] = calculate(x, y);
		return map;
	}
	
	private static byte calculate(int x, int y) {		
		int rack = x + 10;
		long power = (rack * y + SERIAL) * rack;
		byte digit = 0;
		if (power >= 100)
			digit = (byte)(power / 100 % 10);
		digit -= 5;
		return digit;
	}
	
	private static long getTotal3(int x, int y, byte[][] map) {
		long total = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				total += map[x + i][y + j];
		return total;
	}
	
	private static Result getTotal(int x, int y, byte[][] map) {
		long total = map[x][y];
		long max = total;
		int max_size = 1;
		
		// take the smaller of x and y
		for (int s = 1; s <= Math.min(300 - x, 300 - y); s++) {
			for (int i = x; i <= x + s; i++)
				total += map[i][y + s];
			for (int j = y; j < y + s; j++)
				total += map[x + s][j];
			if (total > max) {
				max = total;
				max_size = s + 1;
			}
		}
		
		return new Result(max_size, max);
	}
	
	private static class Result {
		private int max_size;
		private long total;
		
		private Result (int m, long t) {
			max_size = m;
			total = t;
		}
	}
}
