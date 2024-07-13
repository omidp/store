package com.grocery.store.service.rule;

import com.grocery.store.domain.Category;
import com.grocery.store.model.rule.OrderItemRuleInput;
import com.grocery.store.model.rule.OrderItemRuleOutput;
import com.grocery.store.model.rule.RuleOutputResult;
import com.grocery.store.service.rule.KieService;
import com.grocery.store.service.rule.RuleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RuleEngineIntegrationTest.RuleTestConfig.class)
public class RuleEngineIntegrationTest {

	@Autowired
	private RuleService ruleService;

	@Test
	void testBreads() {
		var item1 = new OrderItemRuleInput(Category.BREADS.name(), 1, BigDecimal.ONE, "Sangak");
		item1.setNumberOfDaysExpired(1L);
		var item2 = new OrderItemRuleInput(Category.BREADS.name(), 1, BigDecimal.ONE, "Lavash");
		item2.setNumberOfDaysExpired(3L);
		List<RuleOutputResult> actual = ruleService.executeBatch(List.of(item1, item2));
		RuleOutputResult res1 = new RuleOutputResult();
		res1.setResult(new OrderItemRuleOutput("No discount", 1, BigDecimal.ONE));
		RuleOutputResult res2 = new RuleOutputResult();
		res2.setResult(new OrderItemRuleOutput("Buy 1 take 2", 2, BigDecimal.ONE));
		Assertions.assertThat(actual).isEqualTo(List.of(res1, res2));
	}

	@Test
	void testVegetables() {
		var item1 = new OrderItemRuleInput(Category.VEGETABLES.name(), 1, BigDecimal.ONE, "Lettuce");
		item1.setWeight(200);
		List<RuleOutputResult> actual = ruleService.executeBatch(List.of(item1));
		RuleOutputResult res = new RuleOutputResult();
		res.setResult(new OrderItemRuleOutput("7% discount", 1, valueOf(1.86)));
		Assertions.assertThat(actual).isEqualTo(List.of(res));
	}

	@Test
	void testBeers() {
		var item1 = new OrderItemRuleInput(Category.BEERS.name(), 6, valueOf(0.5), "Dutch beer");
		List<RuleOutputResult> actual = ruleService.executeBatch(List.of(item1));
		RuleOutputResult res = new RuleOutputResult();
		res.setResult(new OrderItemRuleOutput("2 for each Dutch beer pack", 6, valueOf(1.0)));
		Assertions.assertThat(actual).isEqualTo(List.of(res));
	}

	@Configuration
	@ComponentScan(basePackageClasses = KieService.class)
	public static class RuleTestConfig {

	}

}
