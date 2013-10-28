package lodzjug.presentation.drools.example01.model;

import java.util.Map;

public abstract class Symptom {
	public static enum SymptomExistence {
		EXISTS, NOT_EXISTS, UNKNOWN;
	}

	private SymptomExistence existing = SymptomExistence.UNKNOWN;

	public Question askAbout() {
		return new Question(this);
	}
	
	public SymptomExistence getExistence() {
		return existing;
	}

	public Symptom exists() {
		existing = SymptomExistence.EXISTS;
		return this;
	}

	public Symptom notExists() {
		existing = SymptomExistence.NOT_EXISTS;
		return this;
	}

	abstract void details(Map<String, Object> answerDetails);

}
