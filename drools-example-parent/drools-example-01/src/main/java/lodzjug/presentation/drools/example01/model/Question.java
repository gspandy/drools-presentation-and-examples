package lodzjug.presentation.drools.example01.model;


public class Question {
	private Class<? extends Symptom> symptomType;
	
	private boolean answered = false;

	public Question(Class<? extends Symptom> symptomType) {
		this.symptomType = symptomType;
	}

	public boolean isAnswered() {
		return answered;
	}
	
	public void answer() {
		answered = true;
	}

	public Class<? extends Symptom> getSymptomType() {
		return symptomType;
	}
}
