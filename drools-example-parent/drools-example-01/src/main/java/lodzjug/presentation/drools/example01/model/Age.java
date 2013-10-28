package lodzjug.presentation.drools.example01.model;

import java.util.Map;

public class Age extends Symptom {

	private Integer ageInYears;

	public Age() {
	}

	public int getAgeInYears() {
		return ageInYears;
	}

	@Override
	void details(Map<String, Object> answerDetails) {
		this.ageInYears = (Integer) answerDetails.get("ageInYears");
	}
}
