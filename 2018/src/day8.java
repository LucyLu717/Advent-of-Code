import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

/*
	The tree is made up of nodes; a single, outermost node forms the tree's 
	root, and it contains all other nodes in the tree (or contains nodes that 
	contain nodes, and so on).

	Specifically, a node consists of:
	
	A header, which is always exactly two numbers:
		The quantity of child nodes.
		The quantity of metadata entries.
		Zero or more child nodes (as specified in the header).
		One or more metadata entries (as specified in the header).
	
	Each child node is itself a node that has its own header, child nodes, 
	and metadata. For example:
	
	2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
	A----------------------------------
	    B----------- C-----------
	                     D-----
	In this example, each node of the tree is also marked with an underline 
	starting with a letter for easier identification. In it, there are four 
	nodes:
	
	A, which has 2 child nodes (B, C) and 3 metadata entries (1, 1, 2).
	B, which has 0 child nodes and 3 metadata entries (10, 11, 12).
	C, which has 1 child node (D) and 1 metadata entry (2).
	D, which has 0 child nodes and 1 metadata entry (99).
	
	The first check done on the license file is to simply add up all of the 
	metadata entries. In this example, that sum is 1+1+2+10+11+12+2+99=138.
	
	What is the sum of all metadata entries?
	
	=============================	

	The second check is slightly more complicated: you need to find the value 
	of the root node (A in the example above).
	
	The value of a node depends on whether it has child nodes.
	
	If a node has no child nodes, its value is the sum of its metadata 
	entries. So, the value of node B is 10+11+12=33, and the value of node 
	D is 99.
	
	However, if a node does have child nodes, the metadata entries become 
	indexes which refer to those child nodes. A metadata entry of 1 refers 
	to the first child node, 2 to the second, 3 to the third, and so on. The 
	value of this node is the sum of the values of the child nodes referenced 
	by the metadata entries. If a referenced child node does not exist, that 
	reference is skipped. A child node can be referenced multiple time and 
	counts each time it is referenced. A metadata entry of 0 does not refer 
	to any child node.
	
	For example, again using the above nodes:
	
	Node C has one metadata entry, 2. Because node C has only one child node,
	2 references a child node which does not exist, and so the value of node 
	C is 0.
	Node A has three metadata entries: 1, 1, and 2. The 1 references node A's 
	first child node, B, and the 2 references node A's second child node, C. 
	Because node B has a value of 33 and node C has a value of 0, the value 
	of node A is 33+33+0=66.
	
	So, in this example, the value of the root node is 66.
	
	What is the value of the root node?

 */

/*
	GOOD PRACTICE:
		- parse trees from a string using several stacks
*/

public class day8 {

	public static void main(String[] args) throws Exception {
//		String example = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";
//		String[] ints_ex = example.split(" ");
//		parseTree(ints_ex);
//		System.out.println(example);
//		parseVal(ints_ex);
		String filename = "files/input8.txt";
		String input = getRawInput(filename);
		String[] ints = input.split(" ");
		parseTree(ints);
		parseVal(ints);
	}
	
	private static String getRawInput(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		String input = br.readLine();
		
		br.close();
		ir.close();
		is.close();
		return input;
	}
	
	private static void parseTree(String[] input) throws Exception {
		LinkedList<Integer> tree_stack = new LinkedList<Integer>();
		LinkedList<Integer> num_stack = new LinkedList<Integer>();

		int pointer = 0;
		int total = 0;
		
		while (pointer < input.length) {
			assert tree_stack.size() == num_stack.size();
			
			if (!tree_stack.isEmpty() && tree_stack.peek() == 0) {
				tree_stack.poll();
				int num_meta = num_stack.poll();
				for (int i = pointer; i < pointer + num_meta; i++)
					total += Integer.parseInt(input[i]);
				pointer += num_meta;
				if (!tree_stack.isEmpty())
					tree_stack.addFirst(tree_stack.poll() - 1); 
			} else {
				tree_stack.addFirst(Integer.parseInt(input[pointer]));
				num_stack.addFirst(Integer.parseInt(input[pointer + 1]));
				pointer += 2; }			
		}
		
		// there exist nodes that can't fit into the tree
		if (tree_stack.size() != 0)
			throw new Exception("Invalid Tree Input");
		
		System.out.println(total);
	}
	
	private static void parseVal(String[] input) throws Exception {
		LinkedList<Integer> tree_stack = new LinkedList<Integer>();
		LinkedList<Integer> num_stack = new LinkedList<Integer>();
		LinkedList<ArrayList<Integer>> val_stack = new LinkedList<ArrayList<Integer>>();

		int pointer = 0;
		
		while (pointer < input.length) {
			assert tree_stack.size() == num_stack.size();
			assert tree_stack.size() == val_stack.size();
			
			if (!tree_stack.isEmpty() && tree_stack.peek() == 0) {
				tree_stack.poll();
				int num_meta = num_stack.poll();
				ArrayList<Integer> list = val_stack.poll();
				
				int value = 0;
				
				// the leaf node
				if (list.size() == 0) {
					for (int i = pointer; i < pointer + num_meta; i++) 
						value += Integer.parseInt(input[i]);
				} else { // non-leaf node
					for (int i = pointer; i < pointer + num_meta; i++) {
						int index = Integer.parseInt(input[i]);
						if (Integer.parseInt(input[i]) <= list.size())
							value += list.get(index - 1); }
				}
								
				if (tree_stack.isEmpty()) {
					System.out.println(value);
					return; }
				
				pointer += num_meta;
				if (!tree_stack.isEmpty()) {
					tree_stack.addFirst(tree_stack.poll() - 1);
					val_stack.get(0).add(value); } 
			} else {
				tree_stack.addFirst(Integer.parseInt(input[pointer]));
				num_stack.addFirst(Integer.parseInt(input[pointer + 1]));
				val_stack.addFirst(new ArrayList<Integer>());
				pointer += 2; }			
		}
		
		// there exist nodes that can't fit into the tree
		if (tree_stack.size() != 0)
			throw new Exception("Invalid Tree Input");
	}
}
