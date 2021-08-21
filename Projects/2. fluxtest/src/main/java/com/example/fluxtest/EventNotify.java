package com.example.fluxtest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EventNotify {

	private List<String> events = new ArrayList<>();

	private boolean change = false;

	public void add(String data) {
		events.add(data);
		change = true;
	}

	public boolean getChange() {
		return change;
	}
	
	public void setChange(boolean change) {
		this.change = change;
	}
	
	public List<String> getEvents() {
		return events;
	}
}
