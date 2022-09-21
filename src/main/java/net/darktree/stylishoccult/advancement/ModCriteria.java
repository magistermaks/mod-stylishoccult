package net.darktree.stylishoccult.advancement;

import net.minecraft.advancement.criterion.Criteria;

public class ModCriteria {

	public static final ExceptionCriterion EXCEPTION = Criteria.register(new ExceptionCriterion());
	public static final TriggerCriterion TRIGGER = Criteria.register(new TriggerCriterion());
	public static final InsightCriterion INSIGHT = Criteria.register(new InsightCriterion());
	public static final WakeDemonCriterion WAKE = Criteria.register(new WakeDemonCriterion());
	public static final ScrapeFleshCriterion SCRAPE = Criteria.register(new ScrapeFleshCriterion());
	public static final AscendCriterion ASCEND = Criteria.register(new AscendCriterion());
	public static final RitualCriterion RITUAL = Criteria.register(new RitualCriterion());

	public static void init() {
		// load class
	}

}
