package com.revature.rphVertical;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;


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
	ResponseEntity<?> testGet () throws InstantiationException, IllegalAccessException {
		Map<String,Object> map = new HashMap<>();
		map.put("nestedEnt.nestedEnt.aBoolean",true);
		return ResponseEntity.ok(service.customSelectAll(map));
	}

	@PostMapping
	ResponseEntity<?> testAdd(@RequestBody T t) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
		return ResponseEntity.ok(service.save(t));
	}
}
