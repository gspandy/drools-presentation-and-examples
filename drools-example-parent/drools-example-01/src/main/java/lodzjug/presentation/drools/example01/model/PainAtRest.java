package lodzjug.presentation.drools.example01.model;

import java.util.Map;

//Ból spoczynkowy
public class PainAtRest extends Symptom {

	private Integer durationInMinutes;

	public PainAtRest(boolean existing, Integer durationInMinutes) {
		super(existing);
		this.durationInMinutes = durationInMinutes;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}
}
