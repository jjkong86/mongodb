package com.example.mongodb.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class MongoDBController {
	@GetMapping(value = "/")
	public String test() {
		return "hello world!!";
	}
}
