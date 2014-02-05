package lodzjug.presentation.drools.example02.model;

import java.math.BigDecimal;

public class Account {

	private BigDecimal balance;
	private Customer customer;

	public Account(BigDecimal balance, Customer customer) {
		this.balance = balance;
		this.customer = customer;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Customer getCustomer() {
		return customer;
	}
}
