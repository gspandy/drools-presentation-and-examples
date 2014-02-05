package lodzjug.presentation.drools.example02.model;

public class MarketingMessage {
	private Content content;
	private Customer customer;

	public MarketingMessage(Customer customer, Content content) {
		this.content = content;
		this.customer = customer;
	}

	public Content getContent() {
		return content;
	}

	public Customer getCustomer() {
		return customer;
	}
}
