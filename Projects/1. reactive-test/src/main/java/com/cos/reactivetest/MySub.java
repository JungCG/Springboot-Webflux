package com.cos.reactivetest;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MySub implements Subscriber<Integer> {
	private Subscription s;
	private int how_many = 5;
	private int bufferSize = how_many;
	
	public void onComplete() {
		System.out.println("&&&& MySub - onComplete 호출");
		System.out.println("구독 완료");
	}

	public void onError(Throwable t) {
		System.out.println("&&&& MySub - onError 호출");
		System.out.println("구독 중 에러");
	}

	public void onNext(Integer t) {
		System.out.println("&&&& MySub - onNext 호출");
		System.out.println("onNext() : "+t);
		
		bufferSize--;
		if(bufferSize == 0) {
			System.out.println("하루 지남");
			bufferSize = how_many;
			s.request(bufferSize);
		}
	}

	public void onSubscribe(Subscription s) {
		System.out.println("&&&& MySub - onSubscribe 호출");
		System.out.println("구독자 : 구독 정보 잘 받았어");
		this.s = s;
		System.out.println("구독자 : 나 이제 신문 1개씩 줘");
		s.request(bufferSize); // 신문 한개씩 매일 매일 줘!! (백프레셔) 소비자가 한번에 처리할 수 있는 개수를 요청
	}
}
