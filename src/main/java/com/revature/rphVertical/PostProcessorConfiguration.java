package com.revature.rphVertical;

import com.revature.rphVertical.TempEnt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostProcessorConfiguration {
	@Bean
	public Interceptor interceptor() {
		return new Interceptor();
	}
}


