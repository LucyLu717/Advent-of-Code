import java.util.LinkedList;

/*
  	The Elves play this game by taking turns arranging the marbles in a 
  	circle according to very particular rules. The marbles are numbered 
  	starting with 0 and increasing by 1 until every marble has a number.

	First, the marble numbered 0 is placed in the circle. At this point, 
	while it contains only a single marble, it is still a circle: the 
	marble is both clockwise from itself and counter-clockwise from itself. 
	This marble is designated the current marble.
	
	Then, each Elf takes a turn placing the lowest-numbered remaining 
	marble into the circle between the marbles that are 1 and 2 marbles 
	clockwise of the current marble. (When the circle is large enough, 
	this means that there is one marble between the marble that was just 
	placed and the current marble.) The marble that was just placed then 
	becomes the current marble.
	
	However, if the marble that is about to be placed has a number which 
	is a multiple of 23, something entirely different happens. First, the 
	current player keeps the marble they would have placed, adding it to 
	their score. In addition, the marble 7 marbles counter-clockwise from 
	the current marble is removed from the circle and also added to the 
	current player's score. The marble located immediately clockwise of 
	the marble that was removed becomes the new current marble.
	
	For example, suppose there are 9 players. After the marble with value 
	0 is placed in the middle, each player (shown in square brackets) 
	takes a turn. The result of each of those turns would produce circles 
	of marbles like this, where clockwise is to the right and the resulting 
	current marble is in parentheses:
	
	[-] (0)
	[1]  0 (1)
	[2]  0 (2) 1 
	[3]  0  2  1 (3)
	[4]  0 (4) 2  1  3 
	[5]  0  4  2 (5) 1  3 
	[6]  0  4  2  5  1 (6) 3 
	[7]  0  4  2  5  1  6  3 (7)
	[8]  0 (8) 4  2  5  1  6  3  7 
	[9]  0  8  4 (9) 2  5  1  6  3  7 
	[1]  0  8  4  9  2(10) 5  1  6  3  7 
	[2]  0  8  4  9  2 10  5(11) 1  6  3  7 
	[3]  0  8  4  9  2 10  5 11  1(12) 6  3  7 
	[4]  0  8  4  9  2 10  5 11  1 12  6(13) 3  7 
	[5]  0  8  4  9  2 10  5 11  1 12  6 13  3(14) 7 
	[6]  0  8  4  9  2 10  5 11  1 12  6 13  3 14  7(15)
	[7]  0(16) 8  4  9  2 10  5 11  1 12  6 13  3 14  7 15 
	[8]  0 16  8(17) 4  9  2 10  5 11  1 12  6 13  3 14  7 15 
	[9]  0 16  8 17  4(18) 9  2 10  5 11  1 12  6 13  3 14  7 15 
	[1]  0 16  8 17  4 18  9(19) 2 10  5 11  1 12  6 13  3 14  7 15 
	[2]  0 16  8 17  4 18  9 19  2(20)10  5 11  1 12  6 13  3 14  7 15 
	[3]  0 16  8 17  4 18  9 19  2 20 10(21) 5 11  1 12  6 13  3 14  7 15 
	[4]  0 16  8 17  4 18  9 19  2 20 10 21  5(22)11  1 12  6 13  3 14  7 15 
	[5]  0 16  8 17  4 18(19) 2 20 10 21  5 22 11  1 12  6 13  3 14  7 15 
	[6]  0 16  8 17  4 18 19  2(24)20 10 21  5 22 11  1 12  6 13  3 14  7 15 
	[7]  0 16  8 17  4 18 19  2 24 20(25)10 21  5 22 11  1 12  6 13  3 14  7 15
	
	The goal is to be the player with the highest score after the last 
	marble is used up. Assuming the example above ends after the marble 
	numbered 25, the winning score is 23+9=32 (because player 5 kept 
	marble 23 and removed marble 9, while no other player got any points 
	in this very short example game).
	
	What is the winning Elf's score?
	
	============================
	
	Amused by the speed of your answer, the Elves are curious:
	
	What would the new winning Elf's score be if the number of the last 
	marble were 100 times larger?
	
*/

/*
  GOOD PRACTICE:
  	- use long instead of int for large inputs
  	- build my own linked list to speed up the algorithm (reduce time 
  	complexity of get and remove from O(n) to O(1))
  	- while loops
 */

public class day9 {

	public static final int PLAYERS = 493;
	public static int MARBLES = 71863;

	public static void main(String[] args) {
		// 493 players; last marble is worth 71863 points
		original();
		MARBLES *= 100;
		faster();
		System.exit(0);
	}

	private static void original() {
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(0);
		long[] scoreboard = new long[PLAYERS + 1];
		int current_index = 0;

		int marble = 1;
		while (true) {
			int player = 1;
			while (player <= PLAYERS) {
				if (marble <= MARBLES) {
					if (marble % 23 == 0) {
						scoreboard[player] += marble;
						int index = counter_clockwise(list, current_index, 7);
						scoreboard[player] += list.get(index);
						list.remove(index);
						if (index == list.size() + 1)
							current_index = 0;
						else
							current_index = index;
					} else {
						current_index = clockwise(list, current_index, 2);
						list.add(current_index, marble);
					}
				} else {
					System.out.println(max(scoreboard));
					return;
				}
				marble++;
				player++;
			}
		}
	}

	private static void faster() {
		Node init = new Node(0);
		LinkedListExtended<Integer> list = new LinkedListExtended<Integer>(init);
		long[] scoreboard = new long[PLAYERS + 1];
		Node current = init;

		long marble = 1;
		while (true) {
			int player = 1;
			while (player <= PLAYERS) {
				if (marble <= MARBLES) {
					if (marble % 23 == 0) {
						scoreboard[player] += marble;
						Node index = list.getCounterCw(current, 7 - 1);
						scoreboard[player] += (long)(index.value);
						if (index == list.end)
							current = list.start;
						else
							current = index;
						list.remove(index);
					} else {
						current = list.getCw(current, 2);
						list.add(current, new Node(marble));
					}
				} else {
					System.out.println(max(scoreboard));
					return;				
				}
				marble++;
				player++;
			}
		}
	}

	private static long max(long[] scoreboard) {
		long max = scoreboard[0];
		for (int i = 1; i < scoreboard.length; i++)
			if (scoreboard[i] > max)
				max = scoreboard[i];
		return max;
	}

	private static int counter_clockwise
	(LinkedList<Integer> list, int index, int step) {
		int st = 0;
		int ind = index;
		while (st < step) {
			if (ind == 0) 
				ind = list.size() - 1;
			else 
				ind--;
			st++; 
		}

		return ind;
	}

	private static int clockwise
	(LinkedList<Integer> list, int index, int step) {
		int st = 0;
		int ind = index;
		while (st < step) {
			if (ind == list.size() - 1) 
				ind = 0;
			else 
				ind++;
			st++; 
		}

		if (ind == 0)
			ind = list.size();

		return ind;
	}

	private static class LinkedListExtended<E> {
		private Node start, end;
		private int size;

		private LinkedListExtended (Node s) {
			start = s;
			end = s;
			size = 1;
		}

		private int size() {
			return size;
		}

		private void add(Node pre, Node n) {
			size++;

			if (pre == end) {
				Node stored = end;
				end = n;
				stored.next = n;
				n.prev = stored;
				n.next = start;
				start.prev = n;
				return;
			} 

			Node stored = pre.next;
			pre.next = n;
			n.next = stored;
			stored.prev = n;
			n.prev = pre;
		}

		private void remove(Node n) {
			if (n == start) {
				start = n.next;
				size--;
				start.prev = end;
				end.next = start;
			} else if (n == end) {
				end = end.prev;
				size--;
				end.next = start;
				start.prev = end;
			} else {
				n.prev.next = n.next;
				n.next.prev = n.prev;
			}
		}

		private Node getCw(Node n, int step) {
			int counter = 0;
			Node res = n;
			while (counter < step) {
				if (n.next != null)
					res = res.next;
				else
					res = start;
				counter++; }
			return res;
		}

		private Node getCounterCw(Node n, int step) {
			int counter = 0;
			Node res = n;
			while (counter < step) {
				if (n.prev != null)
					res = res.prev;
				else
					res = start;
				counter++; }
			return res;
		}

		@Override
		public String toString() {
			String res = "[";
			Node n = start;
			while (n != end) {
				res += "" + n.value + ", ";
				n = n.next;
			}
			res += "" + n.value + "]";
			return res;
		}
	}

	private static class Node<E> {
		private E value;
		private Node prev, next;

		private Node (E v) {
			value = v;
			prev = null;
			next = null;
		}
	}
}
