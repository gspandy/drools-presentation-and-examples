package lodzjug.presentation.drools.example01.model;

//B�l spoczynkowy
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
