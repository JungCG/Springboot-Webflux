package com.cos.reactivetest;

import java.util.Iterator;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

// 구독 정보 (구독자, 어떤 데이터를 구독할지)
public class MySubscription implements Subscription {

	private Subscriber s;
	private Iterator<Integer> it;

	public MySubscription(Subscriber s, Iterable<Integer> its) {
		System.out.println("&&&& MySubscription - MySubscription 호출");
		this.s = s;
		this.it = its.iterator();
	}

	public void cancel() {
		// TODO Auto-generated method stub

	}

	public void request(long n) {
		System.out.println("&&&& MySubscription - request 호출");
		while( n>0) {
			if(it.hasNext()) {
				s.onNext(it.next());
			}else {
				s.onComplete();
				break;
			}
			n--;
		}
	}
}
