package lodzjug.presentation.drools.example01.model;

public class Age extends Symptom {

	private Integer ageInYears;

	public Age(boolean existing, Integer ageInYears) {
		super(existing);
		this.ageInYears = ageInYears;
	}


	public int getAgeInYears() {
		return ageInYears;
	}

}
