package com.jcg.flux.web;


import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.jcg.flux.domain.Customer;
import com.jcg.flux.domain.CustomerRepository;

import reactor.core.publisher.Mono;

// 통합테스트
//@SpringBootTest
//@AutoConfigureWebTestClient

// 컨트롤러 테스트
@WebFluxTest
public class CustomerControllerTest {

	@MockBean
	private CustomerRepository customerRepository;

	@Autowired
	private WebTestClient webClient;

	@Test
	public void 한건찾기_테스트() {

		// given
		Mono<Customer> givenData = Mono.just(new Customer("Jack", "Bauer"));
		
		// stub -> 행동 지시
		when(customerRepository.findById(1L)).thenReturn(givenData);
//		when(customerRepository.findById(1L).thenReturn(givenData));
		webClient.get().uri("/customer/{id}", 1L)
			.exchange()
			.expectBody()
			.jsonPath("$.firstName").isEqualTo("Jack")
			.jsonPath("$.lastName").isEqualTo("Bauer");
	}
}
