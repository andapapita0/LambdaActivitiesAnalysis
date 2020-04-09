package main;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String args[]) {
		MonitoredData md = new MonitoredData();
		List<MonitoredData> list = new ArrayList<MonitoredData>();
		list = md.readFile();
	
		
		long days = md.countDays(list);
		System.out.println("\n\n\nTask 2:\n" + "Total nr. of days: " + days);
		
		System.out.println("\n\n\nTask 3:\n");
		md.activityTotalAppearances(list);
		
		System.out.println("\n\n\nTask 4:\n");
		md.countActivitiesByDay(list);
		
		System.out.println("\n\n\nTask 5:\n");
		md.computeDuration(list);
		
		System.out.println("\n\n\nTask 6:\n");
		md.computeEntireDuration(list);
		
		System.out.println("\n\n\nTask 7:\n");
		md.lessThan5(list, md);
	
	}
}
