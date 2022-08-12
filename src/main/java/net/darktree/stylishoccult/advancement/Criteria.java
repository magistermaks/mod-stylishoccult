package net.darktree.stylishoccult.advancement;

import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class Criteria {

	public static final ExceptionCriterion EXCEPTION = CriterionRegistry.register(new ExceptionCriterion());
	public static final TriggerCriterion TRIGGER = CriterionRegistry.register(new TriggerCriterion());
	public static final InsightCriterion INSIGHT = CriterionRegistry.register(new InsightCriterion());
	public static final WakeDemonCriterion WAKE = CriterionRegistry.register(new WakeDemonCriterion());
	public static final ScrapeFleshCriterion SCRAPE = CriterionRegistry.register(new ScrapeFleshCriterion());
	public static final AscendCriterion ASCEND = CriterionRegistry.register(new AscendCriterion());
	public static final RitualCriterion RITUAL = CriterionRegistry.register(new RitualCriterion());

	public static void init() {
		// load class
	}

}
