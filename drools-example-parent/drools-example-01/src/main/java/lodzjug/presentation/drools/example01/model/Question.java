package lodzjug.presentation.drools.example01.model;

import java.util.HashMap;
import java.util.Map;

import lodzjug.presentation.drools.example01.model.Symptom.SymptomExistence;
import static com.google.common.base.Preconditions.*;

public class Question {
	private Symptom symptom;
	
	private boolean answered = false;
	
	private Map<String, Object> answerDetails = new HashMap<>();

	Question(Symptom symptom) {
		checkArgument(SymptomExistence.UNKNOWN.equals(symptom.getExistence()));
		
		this.symptom = symptom;
	}
	
	public Symptom answer(boolean exists) {
		Symptom answeredSymptom = exists ? symptom.exists() : symptom.notExists();
		answeredSymptom.details(answerDetails);
		answered = true;
		return answeredSymptom;
	}
	
	public Question withDetail(String detaillName, Object detail) {
		answerDetails.put(detaillName, detail);
		return this;
	}

	public Class<? extends Symptom> about() {
		return symptom.getClass();
	}

	public boolean isAnswered() {
		return answered;
	}
}
