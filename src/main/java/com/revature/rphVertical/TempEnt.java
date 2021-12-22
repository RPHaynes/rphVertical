package com.revature.rphVertical;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Component
@Entity
public class TempEnt {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int i;

	public boolean aBoolean;
}
