package com.revature.rphVertical;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@SpringBootApplication
public class RphVerticalApplication {
	@Autowired
	BeanFactory beanFactory;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext= SpringApplication.run(RphVerticalApplication.class, args);

	}
//	@PostConstruct
//	public void next(){
//		System.out.println(beanFactory);
//
//
//	}

}
