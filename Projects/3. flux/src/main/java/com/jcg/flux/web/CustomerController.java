package com.jcg.flux.web;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcg.flux.domain.Customer;
import com.jcg.flux.domain.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
public class CustomerController {

	private final CustomerRepository customerRepository;
	
	private final Sinks.Many<Customer> sink;
	// sink : Stream을 합쳐주는 기능
	// A 요청 -> Flux -> Stream
	// B 요청 -> Flux -> Stream
	// -> Flux.merge -> sink
	
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		sink = Sinks.many().multicast().onBackpressureBuffer();
	}

	// onNext 되면서 데이터를 모아놨다가 한번에 들고 옴
	@GetMapping("/flux")
	public Flux<Integer> flux() {
		return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
	}

	// produces = MediaType.APPLICATION_STREAM_JSON_VALUE
	// 한건 onNext 할 때마다 버퍼로 Flush 해준다.
	@GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> fluxstream() {
		return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
	}

	// 여러 건일 때는 Flux
	// 데이터가 소진되면 종료됨.
	@GetMapping(value = "/customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Customer> findAll() {
		return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
	}
	
	// 한 건밖에 없을 때 Mono
	@GetMapping("/customer/{id}")
	public Mono<Customer> findById(@PathVariable Long id){
		return customerRepository.findById(id).log();
	}
	
	// produces = MediaType.TEXT_EVENT_STREAM_VALUE
	// SSE 프로토콜이 적용되서 응답
//	@GetMapping(value="/customer/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	ServerSentEvent 로 리턴이 되면 자동으로 SSE 가 설정되어 produces 생략 가능하다
	@GetMapping(value="/customer/sse")
	public Flux<ServerSentEvent<Customer>> findAllSSE(){
		return sink.asFlux().map(c->ServerSentEvent.builder(c).build()).doOnCancel(()->{
			sink.asFlux().blockLast();
		});
	}
	
	@PostMapping("/customer")
	public Mono<Customer> save(){
		return customerRepository.save(new Customer("gildong", "Hong")).doOnNext(c -> {
			sink.tryEmitNext(c);
		});
	}
}
