package lodzjug.presentation.drools.example01.model;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class DoctorAdvisorTestOfCoronaryArteryDiseaseDiagnostics {

	private DoctorAdvisor doctorAdvisor;
	
	@Before
	public void setUp() throws Exception {
		DoctorAdvisorFactory doctorAdvisorFactory = DoctorAdvisorFactory.newFactory();
		doctorAdvisor = doctorAdvisorFactory .newAdvisor();
	}

	@Test
	public void whenChestPainGetFiveQuestions() {
		doctorAdvisor.tellAboutSymptom(new ChestPain());
		
		Collection<Question> questions = doctorAdvisor.anyQuestions();
		assertEquals(5, questions.size());
		
		Collection<Diagnose> possibleDiagnoses = doctorAdvisor.checkDiagnose();
		assertTrue(possibleDiagnoses.isEmpty());
	}

}
