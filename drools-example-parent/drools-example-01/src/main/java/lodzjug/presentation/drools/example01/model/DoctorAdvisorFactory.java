package lodzjug.presentation.drools.example01.model;

import java.util.Arrays;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoctorAdvisorFactory {
	private static final Logger logger = LoggerFactory.getLogger(DoctorAdvisorFactory.class);
	private KnowledgeBase knowledgeBase;

	public static DoctorAdvisorFactory newFactory() {
		return new DoctorAdvisorFactory();
	}
	
	private DoctorAdvisorFactory() {
		setUpKnowledgeBase();
	}
	
	private void setUpKnowledgeBase() {
		KnowledgeBuilder knowledgeBuilder = 
				KnowledgeBuilderFactory.newKnowledgeBuilder();
		Resource diagnosticRuleResource = 
				ResourceFactory.newClassPathResource("diagnostics.drl");
		knowledgeBuilder.add(diagnosticRuleResource , ResourceType.DRL);
		
		if (knowledgeBuilder.hasErrors()) {
			logErrors(knowledgeBuilder);
			throw new IllegalStateException();
		}
		
		knowledgeBase = knowledgeBuilder.newKnowledgeBase();
	}

	private void logErrors(KnowledgeBuilder knowledgeBuilder) {
		for (KnowledgeBuilderError error : knowledgeBuilder.getErrors()) {
			String errorLine = 
					"[" + error.getSeverity() +"] " + 
					"at resource '" + error.getResource() + "', " + 
					"in lines " + Arrays.toString(error.getLines()) +": " +
					"'" + error.getMessage() + "'";
			logger.error(errorLine );
		}
	}
	
	public DoctorAdvisor newAdvisor() {
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		session.setGlobal("logger", LoggerFactory.getLogger("DIAGNOSTIC RULES"));
		return new DoctorAdvisor(session);
	}
}
