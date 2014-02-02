package lodzjug.presentation.drools.example01.model;

public abstract class Symptom {
	private boolean existing;
	
	public Symptom(boolean existing) {
		this.existing = existing;
	}


	public boolean isExisting() {
		return existing;
	}

}
