package com.revature.rphVertical;


import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Component
@Entity
public class TempNestedNestedEnt {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int i;

	public boolean aBoolean;
}
