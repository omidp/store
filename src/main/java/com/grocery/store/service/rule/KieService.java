package com.grocery.store.service.rule;

import lombok.extern.slf4j.Slf4j;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class KieService {

	private static final String UTF_8 = "UTF-8";

	public KieSessionsPool newKieSessionsPool(InputStream is) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		org.kie.api.io.Resource resource = ResourceFactory.newInputStreamResource(is, UTF_8);
		kbuilder.add(resource, ResourceType.DRL);
		return kbuilder.newKieBase().newKieSessionsPool(Runtime.getRuntime().availableProcessors());
	}

	public StatelessKieSession newStatelessKieSession(InputStream is) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		org.kie.api.io.Resource resource = ResourceFactory.newInputStreamResource(is, UTF_8);
		kbuilder.add(resource, ResourceType.DRL);
		return kbuilder.newKieBase().newStatelessKieSession();
	}

	public String generatedDRL(InputStream decisionTable) {
		DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		dtableconfiguration.setInputType(DecisionTableInputType.XLSX);
		DecisionTableProviderImpl dtp = new DecisionTableProviderImpl();
		return dtp.loadFromResource(ResourceFactory.newInputStreamResource(decisionTable, UTF_8), dtableconfiguration);
	}

}