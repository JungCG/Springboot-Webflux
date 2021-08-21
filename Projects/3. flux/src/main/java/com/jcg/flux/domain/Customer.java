package com.jcg.flux.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Customer {
	
    @Id
    private Long id;
    private final String firstName;
    private final String lastName;
}