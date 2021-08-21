package com.cos.reactivetest;

import java.util.Arrays;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class MyPub implements Publisher<Integer> {

	Iterable<Integer> its = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

	public void subscribe(Subscriber<? super Integer> s) {
		System.out.println("&&&& MyPub - subscribe 호출");
		System.out.println("구독자 :  신문사야 나 너희 신문 볼게");
		System.out.println("신문사 :  구독 정보를 만들어서 줄테니 기다려!!");

		MySubscription subscription = new MySubscription(s, its);
		System.out.println("신문사 : 구독 정보 생성 완료 했어 잘받아!");
		
		s.onSubscribe(subscription);
	}
}
