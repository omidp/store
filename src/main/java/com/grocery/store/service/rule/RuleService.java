package com.grocery.store.service.rule;

import com.grocery.store.model.rule.OrderItemRuleInput;
import com.grocery.store.model.rule.OrderItemRuleOutput;
import com.grocery.store.model.rule.RuleOutputResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleService {

	private final KieService kieService;

	public RuleOutputResult execute(OrderItemRuleInput ruleInput) {
		String drl = generateDrlSession();
		log.trace(drl);
		try (InputStream is = new ByteArrayInputStream(drl.getBytes())) {
			//TODO: use sessionPool + cache
			StatelessKieSession session = kieService.newStatelessKieSession(is);
			List<Command> cmds = prepareCommand();
			cmds.add(CommandFactory.newInsert(ruleInput));
			cmds.add(CommandFactory.newSetGlobal("resultList", new RuleOutputResult(), true));
			ExecutionResults er = session.execute(CommandFactory.newBatchExecution(cmds));
			RuleOutputResult res = (RuleOutputResult) er.getValue("resultList");
			return res;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<RuleOutputResult> executeBatch(List<OrderItemRuleInput> ruleInputs) {
		List<RuleOutputResult> resultList = new ArrayList<>();
		for (OrderItemRuleInput ruleInput : ruleInputs) {
			resultList.add(execute(ruleInput));
		}
		return resultList;
	}

	private String generateDrlSession() {
		return kieService.generatedDRL(RuleService.class.getResourceAsStream("/discount_rule.xlsx"));
	}

	private List<Command> prepareCommand() {
		List<Command> cmds = new ArrayList<>();
		OrderItemRuleOutput output = new OrderItemRuleOutput();
		cmds.add(CommandFactory.newInsert(output));
		return cmds;
	}
}
