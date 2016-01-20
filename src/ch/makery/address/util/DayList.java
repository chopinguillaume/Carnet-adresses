package ch.makery.address.util;

import java.util.ArrayList;

public class DayList {

	private ArrayList<DayEnum> days = new ArrayList<>();
	
	public void setDays(ArrayList<DayEnum> days) {
		this.days = days;
	}
	
	public ArrayList<DayEnum> getDays() {
		return days;
	}
	
	public void add(DayEnum day){
		days.add(day);
	}
	
	public boolean contains(DayEnum day){
		return days.contains(day);
	}
	
	@Override
	public String toString() {
		String res = "";
		
		for (DayEnum day : days) {
			res+= day.getCut()+", ";
		}
		
		if(days.size() > 0){
			return res.substring(0, res.length()-2);
		}
		return res;
	}
}
