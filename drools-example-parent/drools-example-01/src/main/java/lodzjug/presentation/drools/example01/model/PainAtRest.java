package lodzjug.presentation.drools.example01.model;

import java.util.Map;

//Ból spoczynkowy
public class PainAtRest extends Symptom {

	private Integer durationInMinutes;

	public PainAtRest() {
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	@Override
	void details(Map<String, Object> answerDetails) {
		durationInMinutes = (Integer) answerDetails.get("durationInMinutes");
		
	}
}
