import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/*
	The instructions specify a series of steps and requirements about which 
	steps must be finished before others can begin (your puzzle input). Each 
	step is designated by a single letter. For example, suppose you have the 
	following instructions:

	Step C must be finished before step A can begin.
	Step C must be finished before step F can begin.
	Step A must be finished before step B can begin.
	Step A must be finished before step D can begin.
	Step B must be finished before step E can begin.
	Step D must be finished before step E can begin.
	Step F must be finished before step E can begin.
	
	Visually, these requirements look like this:
	
	  -->A--->B--
	 /    \      \
	C      -->D----->E
	 \           /
	  ---->F-----
	  
	Your first goal is to determine the order in which the steps should be 
	completed. If more than one step is ready, choose the step which is first 
	alphabetically. In this example, the correct order is CABDFE.
	
	In what order should the steps in your instructions be completed?
		
	==============================
	
	As you're about to begin construction, four of the Elves offer to help. 
	"The sun will set soon; it'll go faster if we work together." Now, you need 
	to account for multiple people working on steps simultaneously. If multiple 
	steps are available, workers should still begin them in alphabetical order.
	
	Each step takes 60 seconds plus an amount corresponding to its letter: A=1, 
	B=2, C=3, and so on. So, step A takes 60+1=61 seconds, while step Z takes 
	60+26=86 seconds. No time is required between steps.
	
	To simplify things for the example, however, suppose you only have help 
	from one Elf (a total of two workers) and that each step takes 60 fewer 
	seconds (so that step A takes 1 second and step Z takes 26 seconds). Then, 
	using the same instructions as above, this is how each second would be 
	spent:
	
	Second   Worker 1   Worker 2   Done
	   0        C          .        
	   1        C          .        
	   2        C          .        
	   3        A          F       C
	   4        B          F       CA
	   5        B          F       CA
	   6        D          F       CAB
	   7        D          F       CAB
	   8        D          F       CAB
	   9        D          .       CABF
	  10        E          .       CABFD
	  11        E          .       CABFD
	  12        E          .       CABFD
	  13        E          .       CABFD
	  14        E          .       CABFD
	  15        .          .       CABFDE
	
	Each row represents one second of time. The Second column identifies how 
	many seconds have passed as of the beginning of that second. Each worker 
	column shows the step that worker is currently doing (or . if they are 
	idle). The Done column shows completed steps.
	
	Note that the order of the steps has changed; this is because steps now 
	take time to finish and multiple workers can begin multiple steps 
	simultaneously.
	
	In this example, it would take 15 seconds for two workers to complete 
	these steps.
	
	With 5 workers and the 60+ second step durations described above, how long 
	will it take to complete all of the steps?

 */

/*
	GOOD PRACTICE:
		- topological sort: keep track of the number of incoming edges left
*/

public class day7 {
	
	public static final int WORKERS = 5;
	public static final int TIME_ADJUST = 60;
	
	public static void main(String[] args) throws IOException {
		String filename = "files/input7.txt";
		DirectedGraph graph1 = getGraph(filename);
//		DirectedGraph graph1 = example(); 
		topoSort(graph1);
		
		DirectedGraph graph2 = getGraph(filename);
//		DirectedGraph graph2 = example(); 
		withWorkers(graph2);
	}
	
	// construct the given example for testing
	private static DirectedGraph example() {
		List<Node> node_lst = new ArrayList<Node>();
		List<Edge> edge_lst = new ArrayList<Edge>();
		
		Node a = new Node('A');
		Node b = new Node('B');
		Node c = new Node('C');
		Node d = new Node('D');
		Node e = new Node('E');
		Node f = new Node('F');

		node_lst.add(a);
		node_lst.add(b);
		node_lst.add(c);
		node_lst.add(d);
		node_lst.add(e);
		node_lst.add(f);
		
		edge_lst.add(new Edge(c, a));
		edge_lst.add(new Edge(a, b));
		edge_lst.add(new Edge(a, d));
		edge_lst.add(new Edge(c, f));
		edge_lst.add(new Edge(f, e));
		edge_lst.add(new Edge(b, e));
		edge_lst.add(new Edge(d, e));

		return new DirectedGraph(node_lst, edge_lst);
	}

	private static DirectedGraph getGraph(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);

		List<Node> node_lst = new ArrayList<Node>();
		List<Edge> edge_lst = new ArrayList<Edge>();
		HashMap<Character, Node> map = new HashMap<Character, Node>();
		
		String line = br.readLine();
		
		while (line != null) {
			int sndindex = line.indexOf("step");
			char st = line.charAt(5);
			char en = line.charAt(sndindex+5);
			
			if (map.get(st) == null) {
				Node start = new Node(st);
				map.put(st, start); 
				node_lst.add(start); }
			if (map.get(en) == null) {
				Node end = new Node(en);
				map.put(en, end); 
				node_lst.add(end); }	
			
			Node start = map.get(st);
			Node end = map.get(en);
			Edge e = new Edge(start, end);
			edge_lst.add(e);
			
			line = br.readLine();
		} 

		// 26 nodes, 101 edges
		DirectedGraph graph = new DirectedGraph(node_lst, edge_lst);
		
		br.close();
		ir.close();
		is.close();
		
		return graph;
	}
	
	private static void topoSort(DirectedGraph graph) {
		LinkedList<Node> queue = new LinkedList<Node>();
		String result = "";
		
		List<Node> start_lst = new ArrayList<Node>(); // 3 start nodes
		for (Node n : graph.nodes)
			if (n.in_num == 0) {
				start_lst.add(n);
			}
		start_lst.sort(new NodeComparator<Node>());
		queue.addAll(start_lst);
		
		while (!queue.isEmpty()) {
			queue.sort(new NodeComparator<Node>());
			Node n = queue.poll();
			n.visited = true;
			result += n.name;
			
			for (Node w : n.neighbors) {
				if (!w.visited) {
					w.in_num--;
					if (w.in_num == 0) {
						queue.add(w); }}}
		}
		
		System.out.println(result);
	}
	
	private static void withWorkers(DirectedGraph graph) {
		LinkedList<Node> queue = new LinkedList<Node>(); // tasks available
		LinkedList<Node> tasks = new LinkedList<Node>(); // tasks done
				  // and therefore can start doing their neighbor ones

		Worker[] people = new Worker[WORKERS];
		for (int i = 0; i < people.length; i++) {
			people[i] = new Worker();
		}

		int current_time = 0; // time counter
		int done = 0; // the number of tasks done
		boolean worker_available = true;
		
		List<Node> start_lst = new ArrayList<Node>();
		for (Node n : graph.nodes)
			if (n.in_num == 0)
				start_lst.add(n);
		start_lst.sort(new NodeComparator<Node>());
		queue.addAll(start_lst);

		while (done != graph.nodes.size()) {
			queue.sort(new NodeComparator<Node>());
			
			for (Worker w : people) {
				if (current_time >= w.finish_time) {
					if (w.curr_task != null && !w.curr_task.visited) {
						tasks.add(w.curr_task);
						w.curr_task.visited = true;
						done++; }
					if (!queue.isEmpty()) {
						Node n = queue.poll();
						w.curr_task = n;
						w.finish_time = 
								current_time + TIME_ADJUST + n.name - 'A' + 1;
						w.available = false; }
					else
						w.available = true;
				}	
			}
			
			boolean flag = false;
			for (Worker w : people) {
				if (w.available) {
					worker_available = true;
					flag = true;
					break; }
			}
			if (!flag)
				worker_available = false;
			
			while (!tasks.isEmpty()) {
				Node n = tasks.poll();
				for (Node w : n.neighbors) {
					if (!w.visited) {
						w.in_num--;
						if (w.in_num == 0) {
							queue.add(w); }}}
			}
			
			if (!(worker_available && !queue.isEmpty())) {
				print(people, current_time); 
				current_time++; }
		}
		
		System.out.println(current_time-1);
	}
	
	// print out the actual schedule as shown in the example
	private static void print(Worker[] people, int time) {
		System.out.print(time + "\t");
		for (Worker w: people) {
			if (w.curr_task == null)
				System.out.print("." + "\t");
			else if (w.available)
				System.out.print("." + "\t");
			else
				System.out.print(w.curr_task.name + "\t"); }
		System.out.println();
	}
	
	private static class DirectedGraph {
		private List<Node> nodes;
		private List<Edge> edges;
				
		public DirectedGraph(List<Node> n, List<Edge> e) {
			nodes = n;
			edges = e;
		}
	}
	
	private static class NodeComparator<Node> implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return (int)(((day7.Node)o1).name) - 
					(int)(((day7.Node)o2).name); }
	}
	
	private static class Node {
		private char name;
		private boolean visited = false;
		private int in_num = 0; // number of incoming edges
		private List<Node> neighbors = new ArrayList<Node>(); 
		// list of (end) neighbors of the node 
		
		public Node (char n) {
			name = n;
		}
		
		@Override
		public boolean equals(Object n1) {
			if (n1 == this) 
				return true;
			if (!(n1 instanceof Node))
				return false;
			Node n = (Node) n1;
			return name == n.name; 
		}

		@Override
		public int hashCode() {
			int result = 17;
			int ch = (int)name;
			result = 31 * ch + 37 * (ch % result);
			return result; }
	}
	
	private static class Edge {
		private Node start, end;
		
		public Edge(Node s, Node e) {
			start = s;
			end = e;
			end.in_num = end.in_num + 1;
			start.neighbors.add(end); }
	}
	
	private static class Worker {
		private boolean available; // if the worker is available
		private Node curr_task; // the task the worker is working on
		private int finish_time; // the designated finish time of the current task
		
		public Worker() { 
			available = true; // if the worker is available
			curr_task = null; // the task the worker is working on
			finish_time = 0; }
	}
}
