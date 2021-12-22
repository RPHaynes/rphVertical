package com.revature.rphVertical;


import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
public class TempNestedEnt {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int i;

	public boolean aBoolean;
	@OneToOne(cascade = CascadeType.ALL)
	public TempNestedNestedEnt nestedEnt;
}
