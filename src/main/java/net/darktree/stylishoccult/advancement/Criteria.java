package net.darktree.stylishoccult.advancement;

import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class Criteria {

	public static final ExceptionCriterion EXCEPTION = CriterionRegistry.register(new ExceptionCriterion());
	public static final TriggerCriterion TRIGGER = CriterionRegistry.register(new TriggerCriterion());
	public static final InsightCriterion INSIGHT = CriterionRegistry.register(new InsightCriterion());
	public static final WakeCriterion WAKE = CriterionRegistry.register(new WakeCriterion());

	public static void init() {
		// load class
	}

}
