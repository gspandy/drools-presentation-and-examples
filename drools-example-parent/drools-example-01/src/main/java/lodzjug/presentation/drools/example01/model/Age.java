package lodzjug.presentation.drools.example01.model;

public class Age extends Symptom {

	private int ageInYears;

	public Age(int ageInYears) {
		this.ageInYears = ageInYears;
	}

	public int getAgeInYears() {
		return ageInYears;
	}
}
