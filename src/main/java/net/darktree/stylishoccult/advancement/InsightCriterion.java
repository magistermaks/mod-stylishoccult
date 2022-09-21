package net.darktree.stylishoccult.advancement;

import com.google.gson.JsonObject;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InsightCriterion extends AbstractCriterion<InsightCriterion.Conditions> {

	static final Identifier ID = new ModIdentifier("insight");

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public InsightCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new InsightCriterion.Conditions(extended, jsonObject);
	}

	public void trigger(ServerPlayerEntity player, double value) {
		this.trigger(player, (conditions) -> conditions.matches(value));
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final double value;

		public Conditions(EntityPredicate.Extended player, JsonObject json) {
			super(InsightCriterion.ID, player);
			this.value = json.get("value").getAsDouble();
		}

		public boolean matches(double value) {
			return this.value <= value;
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = super.toJson(predicateSerializer);
			json.addProperty("value", this.value);
			return json;
		}
	}

}
