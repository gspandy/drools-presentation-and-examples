package lodzjug.presentation.drools.example01.model;

//B�l spoczynkowy
public class PainAtRest extends Symptom {

	private int durationInMinutes;

	public PainAtRest(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}
}
