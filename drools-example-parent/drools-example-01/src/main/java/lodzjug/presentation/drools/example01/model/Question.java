package lodzjug.presentation.drools.example01.model;

import lodzjug.presentation.drools.example01.model.Symptom.SymptomExistence;

import static com.google.common.base.Preconditions.*;

public class Question {
	private Symptom symptom;

	Question(Symptom symptom) {
		checkArgument(SymptomExistence.UNKNOWN.equals(symptom.getExistence()));
		
		this.symptom = symptom;
	}
	
	Symptom answer(boolean exists) {
		return exists ? symptom.exists() : symptom.notExists();
	}
}
