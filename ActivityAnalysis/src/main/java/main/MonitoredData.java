package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonitoredData {
	private String start_time;
	private String end_time;
	private String activity;
	
	
	public MonitoredData(String start_time, String end_time, String activity) {
		this.start_time = start_time;
		this.end_time = end_time;
		this.activity = activity;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public MonitoredData() {
	}

	@Override
	public String toString() {
		return "MonitoredData [start_time = " + start_time + ", end_time = " + end_time + ", activity=  " + activity + "]";
	}
	
	public String getTime(String s) {
		int i = s.indexOf(' ');
		return s.substring(0, i);
	}

	public List<MonitoredData> readFile() { //task1
		String fileName = "D://Activities.txt";
		List<MonitoredData> list = new ArrayList<MonitoredData>();
	    
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream
					.map(line -> line.split("\t\t"))
					.map(a -> new MonitoredData(a[0], a[1], a[2]))
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public long countDays(List<MonitoredData> list) { //task2
		long d = list.stream()
				.filter(s -> s.activity.equals("Sleeping"))
				.count();
		return d;
	}
	
	public Map<String, Long> activityTotalAppearances(List<MonitoredData> list){ //task3 
		Map<String, Long> counts = list.stream()
									   .collect(Collectors.groupingBy(s->s.activity, 
											   Collectors.counting()));
		
		counts.entrySet().stream()
			.forEach(pair -> System.out.println(pair.getKey() + " - " + pair.getValue() 
			+ " times"));
		return counts;
	}
	
	public void countActivitiesByDay(List<MonitoredData> list) { //task4
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
	
		Map<LocalDate, Map<String, Long>> map = list.stream()
			.collect(Collectors.groupingBy(s->LocalDate.parse(s.getTime(s.start_time), formatter),
						Collectors.groupingBy(s->s.activity, Collectors.counting())));
		
		map.entrySet().stream()
		.forEach(pair -> System.out.println(pair.getKey() + " - " + pair.getValue()));
		
	}
	
	public Long getSpecial(Map<String, Long> counts, String s) {
		if(counts.get(s) == null) {
			return (long) 0;
		}
		else return counts.get(s);
	}
	
	public void lessThan5(List<MonitoredData> list, MonitoredData md) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		Map<String, Long> total = list.stream()
			.collect(Collectors.groupingBy(s->s.activity, Collectors.counting()));
		
		Map<String, Long> counts = list.stream()
				.filter( s->Duration.between(LocalDateTime.parse(s.start_time, formatter), 
						LocalDateTime.parse(s.end_time, formatter)).abs().toMinutes() < 5)
				.collect(Collectors.groupingBy(s->s.activity, 
				Collectors.counting()));
		
		Map<String, Long> newlist = list.stream()
				.filter(s ->  (md.getSpecial(counts, s.activity) * 100 / total.get(s.activity)  >= 90) )
				.collect(Collectors.groupingBy(s->s.activity, Collectors.counting()));
		
		newlist.entrySet().stream().forEach(pair -> System.out.println(pair.getKey() + " - " + "lasts " + pair.getValue() + " times (90% of the monitoring records)"+ " less than 5 mins"));
	}
	
	public void computeEntireDuration(List<MonitoredData> list) { //task6
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		Map <String, Duration>  duration = 
				list.stream()
				.collect(Collectors.groupingBy(s->s.activity, 
						Collectors.reducing(Duration.ZERO,
								s -> Duration.between(LocalDateTime.parse(s.start_time, formatter), 
										LocalDateTime.parse(s.end_time, formatter)).abs(),
										Duration::plus
										)
								));
		duration.entrySet().stream()
			.forEach(pair -> System.out.println(pair.getKey() + " - " + pair.getValue()));
		
	}
	
	public void computeDuration(List<MonitoredData> list) { //task5
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		Map <String, Duration>  duration = list.stream()
				.collect(Collectors.toMap(s->s.toString(), 
						s->Duration.between(LocalDateTime.parse(s.start_time, formatter),
								LocalDateTime.parse(s.end_time, formatter)).abs()));
		duration.entrySet().stream()
			.forEach(pair -> System.out.println(pair.getKey() + " - " + pair.getValue()));
	}
	
}
