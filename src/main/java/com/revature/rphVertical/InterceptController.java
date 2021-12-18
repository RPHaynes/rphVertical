package com.revature.rphVertical;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;



public class InterceptController<T> {

	InterceptService<T,?> service;
	Class<T> tClass;

	public void settClass(Class<T> tClass) {
		this.tClass = tClass;
	}

	public void setService(InterceptService<T, ?> service) {
		this.service = service;
	}
	@GetMapping
	ResponseEntity<?> test () throws InstantiationException, IllegalAccessException {
		System.out.println("HERE");
		return ResponseEntity.ok(service.save(tClass.newInstance()));
	}
}
