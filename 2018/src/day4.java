import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/*
	For example, consider the following records, which have already been 
	organized into chronological order:
	
	[1518-11-01 00:00] Guard #10 begins shift
	[1518-11-01 00:05] falls asleep
	[1518-11-01 00:25] wakes up
	[1518-11-01 00:30] falls asleep
	[1518-11-01 00:55] wakes up
	[1518-11-01 23:58] Guard #99 begins shift
	[1518-11-02 00:40] falls asleep
	[1518-11-02 00:50] wakes up
	[1518-11-03 00:05] Guard #10 begins shift
	[1518-11-03 00:24] falls asleep
	[1518-11-03 00:29] wakes up
	[1518-11-04 00:02] Guard #99 begins shift
	[1518-11-04 00:36] falls asleep
	[1518-11-04 00:46] wakes up
	[1518-11-05 00:03] Guard #99 begins shift
	[1518-11-05 00:45] falls asleep
	[1518-11-05 00:55] wakes up
	
	Timestamps are written using year-month-day hour:minute format. 
	The guard falling asleep or waking up is always the one whose 
	shift most recently started. Because all asleep/awake times are 
	during the midnight hour (00:00 - 00:59), only the minute portion 
	(00 - 59) is relevant for those events.
	
	Note that guards count as asleep on the minute they fall asleep, and 
	they count as awake on the minute they wake up. For example, because 
	Guard #10 wakes up at 00:25 on 1518-11-01, minute 25 is marked as awake.
	
	If you can figure out the guard most likely to be asleep at a specific 
	time, you might be able to trick that guard into working tonight so you 
	can have the best chance of sneaking in. You have two strategies for 
	choosing the best guard/minute combination.
	
	Strategy 1: Find the guard that has the most minutes asleep. 
	What minute does that guard spend asleep the most?
	
	In the example above, Guard #10 spent the most minutes asleep, 
	a total of 50 minutes (20+25+5), while Guard #99 only slept for 
	a total of 30 minutes (10+10+10). Guard #10 was asleep most during 
	minute 24 (on two days, whereas any other minute the guard was asleep 
	was only seen on one day).
	
	While this example listed the entries in chronological order, your 
	entries are in the order you found them. You'll need to organize them 
	before they can be analyzed.
	
	What is the ID of the guard you chose multiplied by the minute you 
	chose? (In the above example, the answer would be 10 * 24 = 240.)
	
	==============================
	
	Strategy 2: Of all guards, which guard is most frequently asleep on the 
	same minute?

	In the example above, Guard #99 spent minute 45 asleep more than any 
	other guard or minute - three times in total. (In all other cases, any 
	guard spent any minute asleep at most twice.)

	What is the ID of the guard you chose multiplied by the minute you 
	chose? (In the above example, the answer would be 99 * 45 = 4455.)
 */

public class day4 {

	public static void main(String[] args) throws Exception {
		String filename = "files/input4.txt";
		BufferedReader br = getFile(filename);
		
		laziest(br);
		br.close();
	}
	
	private static BufferedReader getFile(String filename) throws IOException {
		Path p = Paths.get(filename);
		InputStream is = Files.newInputStream(p);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		return br;
	}
	
	private static void laziest (BufferedReader br) throws Exception {
		String line = br.readLine();
		// sort state based on date
		Heap<State> heap = new Heap<State> (false);
		// a queue that keeps track of guard_id and 
		//		when this guard falls asleep
		Queue<ID_Sleep> id_asleep = new LinkedList<>();
		// map a log entry to date
		HashMap<State, Date> map = new HashMap<State, Date>();
		// map guard_id to total sleeping time
		HashMap<Integer, Integer> id_time = new HashMap<Integer, Integer>();
		// map guard_id to minute frequencies; could have used an array
		HashMap<Integer, HashMap<Integer, Integer>> min_forall = 
				new HashMap<Integer, HashMap<Integer, Integer>>();

		int lines = 0;
		while (line != null) {
			parse(line, heap, map);
			line = br.readLine();
			lines += 1;
		}
		System.out.println(lines); // 1097 log entries
		
		// latest guard on shift
		int current_guard = 0;
		
		while (heap.size() != 0) {
			State s = heap.poll();
			
			if (s.state.equals("asleep")) {
				ID_Sleep id_sleep = 
						new ID_Sleep(current_guard, map.get(s).minute);
				id_asleep.add(id_sleep);
			}
			else if (s.state.equals("awake")) {
				ID_Sleep id_sleep = id_asleep.poll();
				int asleep_min = id_sleep.asleep_minute;
				int awake_min = map.get(s).minute;
				int local_id = id_sleep.id;
				
				int sleep_len = awake_min - asleep_min;
				if (id_time.get(local_id) != null)
					id_time.put(local_id, 
							id_time.get(local_id) + sleep_len);
				else
					id_time.put(local_id, sleep_len);
				
				HashMap<Integer, Integer> new_map = null;
				if (min_forall.get(local_id) != null) {
					new_map = min_forall.get(local_id);
				} else {
					new_map = new HashMap<Integer, Integer>();
				}
				
				for (int i = asleep_min; i < awake_min; i++) {
					if (new_map.get(i) != null) {
						new_map.put(i, new_map.get(i)+1);
					} else {
						new_map.put(i, 1);
					}
				}
				min_forall.put(local_id, new_map);
			}
			
			// new guard begins shift
			else {
				int id = Integer.parseInt(s.state);
				current_guard = id;
			}
		}
		
		int laziest = findMaxKey(id_time);
		int most = findMaxKey(min_forall.get(laziest));
		
		System.out.println(laziest);
		System.out.println(most);
		System.out.println(laziest * most);
		findMaxMinu(min_forall);
	}
	
	private static void findMaxMinu(HashMap<Integer, HashMap<Integer, Integer>> map) {
		int max = 0;
		int max_id = 0;
		int max_min = -1;
		
		for (int id: map.keySet()) {
			int max_val = findMaxVal(map.get(id));
			if (max_val > max) {
				max = max_val;
				max_id = id;
				max_min = findMaxKey(map.get(id));
			}
		}
		
		System.out.println(max);
		System.out.println(max_id);
		System.out.println(max_min);
		System.out.println(max_id * max_min);
	}
	
	// a helper function that is the max value in [map]
	private static int findMaxVal(HashMap<Integer, Integer> map) {
		int max_value = 0;
		
		for (int i : map.keySet()) {
			int value = map.get(i);
			if (value > max_value) {
				max_value = value;
			}
		}
		return max_value;
	}
	
	// a helper function that is the key with the max value in [map]
	private static int findMaxKey(HashMap<Integer, Integer> map) {
		int max_value = 0;
		int max_key = 0;
		
		for (int i : map.keySet()) {
			int value = map.get(i);
			if (value > max_value) {
				max_value = value;
				max_key = i;
			}
		}
		return max_key;
	}
	
	/*
	 [1518-11-05 00:03] Guard #99 begins shift
	 [1518-11-05 00:45] falls asleep
	 [1518-11-05 00:55] wakes up
	 */
	private static void parse
	(String sentence, Heap<State> heap, HashMap<State, Date> map) 
			throws Exception {
		int right = sentence.indexOf(']');
		String date = sentence.substring(1, right);
		String content = sentence.substring(right+2);
		
		Date d = parseDate(date);
		State s = parseState(content);
		
		map.put(s, d);
		heap.add(s, d);
	}
	
	private static Date parseDate(String date) {
		int first = date.indexOf('-');
		int last = date.lastIndexOf('-');
		int space = date.indexOf(' ');
		int colon = date.indexOf(':');
		
		int month = Integer.parseInt(date.substring(first+1, last));
		int day = Integer.parseInt(date.substring(last+1, space));
		int hour = Integer.parseInt(date.substring(space+1, colon));
		int minute = Integer.parseInt(date.substring(colon+1));
		
		return new Date(month, day, hour, minute);
	}
	
	private static State parseState(String content) throws Exception {
		String st;
		if (content.indexOf("begins") != -1) {
			int jing = content.indexOf('#');
			st = content.substring(jing+1, content.indexOf("begins") - 1);
		} else if (content.indexOf("falls") != -1) {
			st = "asleep";
		} else if (content.indexOf("wakes") != -1) {
			st = "awake";
		} else {
			throw new Exception("Unrecognized State");
		}
		
		return new State(st);
			
	}
	
	public static class Date implements Comparable<Date> {
		private int month, day, hour, minute;
		
		public Date(int m, int d, int h, int min) {
			month = m;
			day = d;
			hour = h;
			minute = min;
		}
		
		@Override
		// is  if [this] is earlier than [d]
		public int compareTo(Date d) {
			int monthd = month - d.month;
			int dayd = day - d.day;
			int hourd = hour - d.hour;
			int minuted = minute - d.minute;
			
			if (monthd > 0)
				return -1;
			else if (monthd < 0)
				return 1;
			
			if (dayd > 0)
				return -1;
			else if (dayd < 0)
				return 1;
			
			if (hourd > 0)
				return -1;
			else if (hourd < 0)
				return 1;
			
			if (minuted > 0)
				return -1;
			else if (minuted < 0)
				return 1;
			
			return 0;
		}
		
	}
	
	private static class State {
		private String state; // guard id, asleep, awake
		
		public State (String s) {
			state = s;
		}
	}
	
	private static class ID_Sleep {
		private int id, asleep_minute;
		
		public ID_Sleep(int id, int asleep_minute) {
			this.id = id;
			this.asleep_minute = asleep_minute;
		}
	}
	
}
