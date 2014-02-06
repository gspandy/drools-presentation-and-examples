package lodzjug.presentation.drools.example03.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Collection;

import lodzjug.presentation.drools.example03.model.Account;
import lodzjug.presentation.drools.example03.model.Content;
import lodzjug.presentation.drools.example03.model.Customer;
import lodzjug.presentation.drools.example03.model.MarketingMessage;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class MarketingMessagesRulesTest {

	private KieBase kieBase;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(MarketingMessagesRulesTest.class);
	
	@Before
	public void setUp() throws Exception {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kieContainer = kieServices.getKieClasspathContainer();
		kieBase = kieContainer.getKieBase();
	}

	@Test
	public void whenAccountBalanceIsHigherThan5kSendGoldCardOffer() {
		KieSession session = kieBase.newKieSession();
		Customer customer = new Customer(new BigDecimal(10000));
		Account account = new Account(new BigDecimal(6000), customer);
		session.insert(account);
		session.insert(customer);
		
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.fireAllRules();
		
		Collection<? extends MarketingMessage> messages = 
				findFactsOfTypeInSession(session, MarketingMessage.class);
		assertEquals(1, messages.size());
		MarketingMessage[] messagesArray = messages.toArray(new MarketingMessage[0]);
		MarketingMessage message = messagesArray[0];
		assertEquals(customer, message.getCustomer());
		assertEquals(Content.GOLD_CARD, message.getContent());
	}
	
	@Test
	public void whenEarningsIsLowerThan2kSendLoanOffer() {
		KieSession session = kieBase.newKieSession();
		Customer customer = new Customer(new BigDecimal(1000));
		Account account = new Account(new BigDecimal(2000), customer);
		session.insert(account);
		session.insert(customer);
		
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.fireAllRules();
		
		Collection<? extends MarketingMessage> messages = 
				findFactsOfTypeInSession(session, MarketingMessage.class);
		assertEquals(1, messages.size());
		MarketingMessage[] messagesArray = messages.toArray(new MarketingMessage[0]);
		MarketingMessage message = messagesArray[0];
		assertEquals(customer, message.getCustomer());
		assertEquals(Content.LOAN, message.getContent());
	}
	
	@Test
	public void whenEarningsIsLowerThan2kAndAccountBalanceIsHigherThan5kSendLoanOffer() {
		KieSession session = kieBase.newKieSession();
		session.getAgenda().getAgendaGroup("Second messages").setFocus();
		session.getAgenda().getAgendaGroup("First messages").setFocus();

		Customer customer = new Customer(new BigDecimal(1000));
		Account account = new Account(new BigDecimal(7000), customer);
		session.insert(account);
		session.insert(customer);
		
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.fireAllRules();
		
		Collection<? extends MarketingMessage> messages = 
				findFactsOfTypeInSession(session, MarketingMessage.class);
		assertEquals(1, messages.size());
		MarketingMessage[] messagesArray = messages.toArray(new MarketingMessage[0]);
		MarketingMessage message = messagesArray[0];
		assertEquals(customer, message.getCustomer());
		assertEquals(Content.LOAN, message.getContent());
	}

	private <T> Collection<? extends T> findFactsOfTypeInSession(
			KieSession session, Class<T> type) {
		Collection<? extends Object> facts = session.getObjects(new TypeFilter(type));
		return Collections2.transform(facts, new TypeTransformFunction<Object, T>());
	}
	
	private class TypeFilter implements ObjectFilter {
	
		private Class<?> type;
	
		public TypeFilter(Class<?> type) {
			this.type = type;
		}
		
		@Override
		public boolean accept(Object object) {
			if (type.isInstance(object)) {
				return true;
			}
			return false;
		}
		
	}

	public class TypeTransformFunction<F, T> implements Function<F, T> {

		@SuppressWarnings("unchecked")
		@Override
		public T apply(F input) {
			return (T)input;
		}
	
	}
}
