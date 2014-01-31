package lodzjug.presentation.drools.example01.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Collections2;

public class DiagnosticRulesTest {

	private KnowledgeBase knowledgeBase;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DiagnosticRulesTest.class);
	
	@Before
	public void setUp() throws Exception {
		KnowledgeBuilder knowledgeBuilder = 
				KnowledgeBuilderFactory.newKnowledgeBuilder();
		Resource diagnosticRuleResource = 
				ResourceFactory.newClassPathResource("diagnostics.drl");
		knowledgeBuilder.add(diagnosticRuleResource , ResourceType.DRL);
		
		if (knowledgeBuilder.hasErrors()) {
			logErrors(knowledgeBuilder);
			throw new AssertionError();
		}
		
		knowledgeBase = knowledgeBuilder.newKnowledgeBase();
	}

	private void logErrors(KnowledgeBuilder knowledgeBuilder) {
		for (KnowledgeBuilderError error : knowledgeBuilder.getErrors()) {
			String errorLine = 
					"[" + error.getSeverity() +"] " + 
					"at resource '" + error.getResource() + "', " + 
					"in lines " + Arrays.toString(error.getLines()) +": " +
					"'" + error.getMessage() + "'";
			logger.error(errorLine );
		}
	}

	@Test
	public void whenChestPainGetFiveQuestions() {
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.insert(new ChestPain().exists());
		session.fireAllRules();
		
		Collection<Object> questions = session.getObjects(new QuestionFilter());
		
		assertEquals(5, questions.size());
	}

	@Test
	public void cadRiskHigh() {
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.insert(new ChestPain().exists());
		session.fireAllRules();
		
		Collection<Object> questions = session.getObjects(new QuestionFilter());
		
		assertEquals(5, questions.size());
		
		Iterator<Object> qIterator = questions.iterator();
		
		while(qIterator.hasNext()) {
			Question question = (Question) qIterator.next();
			if (question.about().equals(PainAtRest.class)) {
				session.insert(question.withDetail("durationInMinutes", 23).answer(true));
			} else if (question.about().equals(StSegmentAbnormal.class)) {
				session.insert(question.answer(true));
			} else if (question.about().equals(TWaveAbnormal.class)) {
				session.insert(question.answer(true));
			} else if (question.about().equals(Age.class)) {
				session.insert(question.withDetail("ageInYears", 72).answer(true));
			} else if (question.about().equals(PulmonaryEdema.class)) {
				session.insert(question.answer(true));
			}
			session.update(session.getFactHandle(question),question);
		}
		
		session.fireAllRules();
		
		Collection<Object> diagnoses = session.getObjects(new DiagnoseFilter());
		
		assertEquals(1, diagnoses.size());
		Object diagnose = diagnoses.toArray(new Object[0])[0];
		assertEquals (Cad.class, diagnose.getClass());
	}

	@Test
	public void diagnoseUnknownWhenNoPulmonaryEdema() {
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		session.insert(new ChestPain().exists());
		session.fireAllRules();
		
		Collection<Object> questions = session.getObjects(new QuestionFilter());
		
		assertEquals(5, questions.size());
		
		Iterator<Object> qIterator = questions.iterator();
		
		while(qIterator.hasNext()) {
			Question question = (Question) qIterator.next();
			if (question.about().equals(PainAtRest.class)) {
				session.insert(question.withDetail("durationInMinutes", 23).answer(true));
			} else if (question.about().equals(StSegmentAbnormal.class)) {
				session.insert(question.answer(true));
			} else if (question.about().equals(TWaveAbnormal.class)) {
				session.insert(question.answer(true));
			} else if (question.about().equals(Age.class)) {
				session.insert(question.withDetail("ageInYears", 72).answer(true));
			} else if (question.about().equals(PulmonaryEdema.class)) {
				session.insert(question.answer(false));
			}
			
			session.update(session.getFactHandle(question),question);
		}
		
		session.fireAllRules();
		Collection<Object> diagnoses = session.getObjects(new DiagnoseFilter());
		
		assertEquals(1, diagnoses.size());
		Object diagnose = diagnoses.toArray(new Object[0])[0];
		assertEquals (Unknown.class, diagnose.getClass());
	}

	private static final class DiagnoseFilter implements ObjectFilter {
		@Override
		public boolean accept(Object fact) {
			return fact instanceof Diagnose;
		}
	}

	private static final class QuestionFilter implements ObjectFilter {
		@Override
		public boolean accept(Object fact) {
			return fact instanceof Question;
		}
	}
}
