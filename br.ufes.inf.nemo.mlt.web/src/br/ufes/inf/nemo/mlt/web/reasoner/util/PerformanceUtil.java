package br.ufes.inf.nemo.mlt.web.reasoner.util;

import java.util.Date;

public class PerformanceUtil {
	public static long getExecutionTimeInMs(Date beginDate){
		Date endDate = new Date();
		long diff = endDate.getTime() - beginDate.getTime();
		return diff;
	}
	
	public static String getExecutionMessage(Date beginDate){
		long diff = getExecutionTimeInMs(beginDate);
		
		long diffHours = diff / (60 * 60 * 1000);
		diff -= diffHours * 60 * 60 * 1000;
		long diffMinutes = diff / (60 * 1000);         
		diff -= diffMinutes * 60 * 1000;
		long diffSeconds = diff / 1000;
		diff -= diffSeconds * 1000;
		long diffMiliSeconds = diff;
		
		return diffHours + "h " + diffMinutes + "m " + diffSeconds + "s " + diffMiliSeconds + "ms";
	}
	
	public static String printExecutionTime(Date beginDate){
		String message = getExecutionMessage(beginDate);
		System.out.println(message);
		
		return message;
	}
}