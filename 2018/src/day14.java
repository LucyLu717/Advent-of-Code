/*
	Only two recipes are on the board: the first recipe got a score of 3, 
	the second, 7. Each of the two Elves has a current recipe: the first Elf 
	starts with the first recipe, and the second Elf starts with the second 
	recipe.
	
	To create new recipes, the two Elves combine their current recipes. This 
	creates new recipes from the digits of the sum of the current recipes' 
	scores. With the current recipes' scores of 3 and 7, their sum is 10, 
	and so two new recipes would be created: the first with score 1 and the 
	second with score 0. If the current recipes' scores were 2 and 3, the sum, 
	5, would only create one recipe (with a score of 5) with its single digit.
	
	The new recipes are added to the end of the scoreboard in the order they 
	are created. So, after the first round, the scoreboard is 3, 7, 1, 0.
	
	After all new recipes are added to the scoreboard, each Elf picks a new 
	current recipe. To do this, the Elf steps forward through the scoreboard 
	a number of recipes equal to 1 plus the score of their current recipe. 
	So, after the first round, the first Elf moves forward 1 + 3 = 4 times, 
	while the second Elf moves forward 1 + 7 = 8 times. If they run out of 
	recipes, they loop back around to the beginning. After the first round, 
	both Elves happen to loop around until they land on the same recipe that 
	they had in the beginning; in general, they will move to different 
	recipes.
	
	Drawing the first Elf as parentheses and the second Elf as square 
	brackets, they continue this process:
	
	(3)[7]
	(3)[7] 1  0 
	 3  7  1 [0](1) 0 
	 3  7  1  0 [1] 0 (1)
	(3) 7  1  0  1  0 [1] 2 
	 3  7  1  0 (1) 0  1  2 [4]
	 3  7  1 [0] 1  0 (1) 2  4  5 
	 3  7  1  0 [1] 0  1  2 (4) 5  1 
	 3 (7) 1  0  1  0 [1] 2  4  5  1  5 
	 3  7  1  0  1  0  1  2 [4](5) 1  5  8 
	 3 (7) 1  0  1  0  1  2  4  5  1  5  8 [9]
	 3  7  1  0  1  0  1 [2] 4 (5) 1  5  8  9  1  6 
	 3  7  1  0  1  0  1  2  4  5 [1] 5  8  9  1 (6) 7 
	 3  7  1  0 (1) 0  1  2  4  5  1  5 [8] 9  1  6  7  7 
	 3  7 [1] 0  1  0 (1) 2  4  5  1  5  8  9  1  6  7  7  9 
	 3  7  1  0 [1] 0  1  2 (4) 5  1  5  8  9  1  6  7  7  9  2 
	 
	What are the scores of the ten recipes immediately after the number of 
	recipes in your puzzle input?
		
	======================
	
	As it turns out, you got the Elves' plan backwards. They actually want to 
	know how many recipes appear on the scoreboard to the left of the first 
	recipes whose scores are the digits from your puzzle input.
	
	51589 first appears after 9 recipes.
	01245 first appears after 5 recipes.
	
	How many recipes appear on the scoreboard to the left of the score 
	sequence in your puzzle input?
 */

/*
	GOOD PRACTICE:
	- use StringBuilder instead of ArrayList
	- python uses persistent data structures while java does not seem to
*/

public class day14 {

	// puzzle input: 607331
	public static final int RECIPES = 607331;

	public static void main(String[] args) {
		Part1();
//		Part2(); in python because of persistent data structures 
//		thus, new strings are not created over and over again
	}	

	private static void Part1() {
		StringBuilder list = new StringBuilder();
		list.append(String.valueOf(3));
		list.append(String.valueOf(7));
		int pointer1 = 0;
		int pointer2 = 1;
		int size = list.length();
		
		while (size < RECIPES + 10) {
			int one = Integer.parseInt("" + list.charAt(pointer1));
			int two = Integer.parseInt("" + list.charAt(pointer2));
			list.append(String.valueOf(one + two));
			size = list.length();
			pointer1 = (pointer1 + 1 + one) % size;
			pointer2 = (pointer2 + 1 + two) % size;
		}

		String res = "";
		for (int i = RECIPES; i < RECIPES + 10; i++)
			res += "" + list.charAt(i);
		System.out.println(res);
	}
	
	/*
		recipes = "607331"
		score = "37"
		pointer1 = 0
		pointer2 = 1
		len = len(score)
		
		while recipes != score[len:]:
			s1 = int(score[pointer1])
			s2 = int(score[pointer2])
		    score += str(s1 + s2)
		    	len = len(score)
		    pointer1 = (pointer1 + 1 + s1) % len
		    pointer2 = (pointer2 + 1 + s2) % len
		print(score.index(recipes))
	 */
}
