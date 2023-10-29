package com.microservice.paroll.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker {

	private Long id;
	private String name;
	private Double dailyIncome;
}
