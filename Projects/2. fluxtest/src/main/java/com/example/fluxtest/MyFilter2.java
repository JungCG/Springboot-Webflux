package com.example.fluxtest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class MyFilter2 implements Filter {

	@Autowired
	private EventNotify eventNotify;
	
	public MyFilter2(EventNotify eventNotify) {
		this.eventNotify = eventNotify;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("필터2 실행됨");
		// 데이터를 하나 발생시켜서
		eventNotify.add("새로운 데이터");
	}

}
