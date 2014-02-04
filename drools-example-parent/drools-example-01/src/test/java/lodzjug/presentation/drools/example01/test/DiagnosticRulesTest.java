package lodzjug.presentation.drools.example01.test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;

import lodzjug.presentation.drools.example01.model.Age;
import lodzjug.presentation.drools.example01.model.Cad;
import lodzjug.presentation.drools.example01.model.ChestPain;
import lodzjug.presentation.drools.example01.model.Diagnose;
import lodzjug.presentation.drools.example01.model.PainAtRest;
import lodzjug.presentation.drools.example01.model.PulmonaryEdema;
import lodzjug.presentation.drools.example01.model.Question;
import lodzjug.presentation.drools.example01.model.StSegmentAbnormal;
import lodzjug.presentation.drools.example01.model.TWaveAbnormal;
import lodzjug.presentation.drools.example01.model.Unknown;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagnosticRulesTest {

	private KieBase kieBase;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DiagnosticRulesTest.class);
	
	@Before
	public void setUp() throws Exception {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kieContainer = kieServices.getKieClasspathContainer();
		kieBase = kieContainer.getKieBase();
	}

	@Test
	public void whenChestPainGetFiveQuestions() {
		KieSession session = kieBase.newKieSession();
		
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.insert(new ChestPain(true));
		session.fireAllRules();
		
		Collection<? extends Object> questions = session.getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object fact) {
				return fact instanceof Question;
			}
		});
		
		assertEquals(5, questions.size());
	}

	@Test
	public void cadRiskHigh() {
		KieSession session = kieBase.newKieSession();
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.insert(new ChestPain(true));
		session.fireAllRules();
		
		Collection<? extends Object> questions = session.getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object fact) {
				return fact instanceof Question;
			}
		});
		
		assertEquals(5, questions.size());
		
		Iterator<? extends Object> qIterator = questions.iterator();
		
		while(qIterator.hasNext()) {
			Question question = (Question) qIterator.next();
			if (question.getSymptomType().equals(PainAtRest.class)) {
				session.insert(new PainAtRest(true, 25));
			} else if (question.getSymptomType().equals(StSegmentAbnormal.class)) {
				session.insert(new StSegmentAbnormal(true));
			} else if (question.getSymptomType().equals(TWaveAbnormal.class)) {
				session.insert(new TWaveAbnormal(true));
			} else if (question.getSymptomType().equals(Age.class)) {
				session.insert(new Age(true, 72));
			} else if (question.getSymptomType().equals(PulmonaryEdema.class)) {
				session.insert(new PulmonaryEdema(true));
			}

			question.answer();
			session.update(session.getFactHandle(question), question);
		}
		
		session.fireAllRules();
		
		Collection<? extends Object> diagnoses = session.getObjects(new ObjectFilter() {
			
			@Override
			public boolean accept(Object fact) {
				return fact instanceof Diagnose;
			}
		});
		
		assertEquals(1, diagnoses.size());
		Object diagnose = diagnoses.toArray(new Object[0])[0];
		assertEquals (Cad.class, diagnose.getClass());
	}

	@Test
	public void diagnoseUnknownWhenNoPulmonaryEdema() {
		KieSession session = kieBase.newKieSession();
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.insert(new ChestPain(true));
		session.fireAllRules();
		
		Collection<? extends Object> questions = session.getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object fact) {
				return fact instanceof Question;
			}
		});
		
		assertEquals(5, questions.size());
		
		Iterator<? extends Object> qIterator = questions.iterator();
		
		while(qIterator.hasNext()) {
			Question question = (Question) qIterator.next();
			if (question.getSymptomType().equals(PainAtRest.class)) {
				session.insert(new PainAtRest(true, 23));
			} else if (question.getSymptomType().equals(StSegmentAbnormal.class)) {
				session.insert(new StSegmentAbnormal(true));
			} else if (question.getSymptomType().equals(TWaveAbnormal.class)) {
				session.insert(new TWaveAbnormal(true));
			} else if (question.getSymptomType().equals(Age.class)) {
				session.insert(new Age(true, 72));
			} else if (question.getSymptomType().equals(PulmonaryEdema.class)) {
				session.insert(new PulmonaryEdema(false));
			}
			
			question.answer();
			session.update(session.getFactHandle(question), question);
		}
		
		session.fireAllRules();
		
		Collection<? extends Object> diagnoses = session.getObjects(new ObjectFilter() {
			
			@Override
			public boolean accept(Object fact) {
				return fact instanceof Diagnose;
			}
		});
		
		assertEquals(1, diagnoses.size());
		Object diagnose = diagnoses.toArray(new Object[0])[0];
		assertEquals (Unknown.class, diagnose.getClass());
	}
}
