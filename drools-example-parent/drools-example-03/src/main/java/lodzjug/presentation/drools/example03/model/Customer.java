package lodzjug.presentation.drools.example03.model;

import java.math.BigDecimal;

public class Customer {
	
	private BigDecimal averageEarnings;

	public Customer(BigDecimal averageEarnings) {
		this.averageEarnings = averageEarnings;
	}

	public BigDecimal getAverageEarnings() {
		return averageEarnings;
	}
}