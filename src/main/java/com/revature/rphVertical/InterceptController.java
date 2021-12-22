package com.revature.rphVertical;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
		map.put("aBoolean",false);
		return ResponseEntity.ok(service.customSelectAll(map));
	}

	@PostMapping
	ResponseEntity<?> testAdd () throws InstantiationException, IllegalAccessException, NoSuchFieldException {
		T t = tClass.newInstance();
		return ResponseEntity.ok(service.save(t));
	}
}
