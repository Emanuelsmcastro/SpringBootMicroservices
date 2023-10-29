package com.microservice.paroll.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	
	private String workerName;
	private Double workerDailyIncome;
	private Integer days;
	
	public Double getTotal() {
		return workerDailyIncome * days;
	}
	
}
