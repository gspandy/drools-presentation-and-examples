package lodzjug.presentation.drools.example01.model;


public class Question {
	private Class<? extends Symptom> symptomType;
	
	private boolean answered = false;

	Question(Class<? extends Symptom> symptomType) {
		this.symptomType = symptomType;
	}

	public boolean isAnswered() {
		return answered;
	}

	public Class<? extends Symptom> getSymptomType() {
		return symptomType;
	}
}
