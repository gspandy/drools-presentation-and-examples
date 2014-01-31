package lodzjug.presentation.drools.example01.model;

import java.util.Collection;

import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class DoctorAdvisor {
	private static final Logger logger = LoggerFactory.getLogger(DoctorAdvisor.class);

	private StatefulKnowledgeSession knowledgeSession;
	
	private boolean anyNewSymptom = false;
	
	DoctorAdvisor(StatefulKnowledgeSession session) {
		this.knowledgeSession = session;
	}

	public void tellAboutSymptom(Symptom symptom) {
		knowledgeSession.insert(symptom.exists());
		markNewSymptomAsTold();
	}

	private void markNewSymptomAsTold() {
		anyNewSymptom = true;
	}
	
	public Collection<Question> anyQuestions() {
		fireRulesIfNeeded();
		Collection<Question> questions = retrieveTypedFactsFromSession(Question.class);
		return questions;
	}

	public Collection<Diagnose> checkDiagnose() {
		fireRulesIfNeeded();
		return retrieveTypedFactsFromSession(Diagnose.class);
	}

	private <T> Collection<T> retrieveTypedFactsFromSession(Class<T> parametrizedClass) {
		Collection<Object> factObjects = 
				knowledgeSession.getObjects(new TypedFactFilter<T>(parametrizedClass));
		Collection<T> facts = 
				Collections2.transform(factObjects, new ConvertFunction<Object, T>());
		return facts;
	}
	
	private void fireRulesIfNeeded() {
		if (anyNewSymptom) {
			knowledgeSession.fireAllRules();
			anyNewSymptom = false;
		}
	}

	private static final class TypedFactFilter<T extends Object> implements ObjectFilter {
		private Class<T> filterClass;

		public TypedFactFilter(Class<T> filterClass) {
			this.filterClass = filterClass;
		}
		
		@Override
		public boolean accept(Object fact) {
			return this.filterClass.isInstance(fact);
		}
	}

	private class ConvertFunction<F, T extends F> implements Function<F, T> {
		@SuppressWarnings("unchecked")
		@Override
		public T apply(F input) {
			return (T)input;
		}
	}
}


