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

public class WakeCriterion extends AbstractCriterion<WakeCriterion.Conditions> {
	static final Identifier ID = new ModIdentifier("wake");

	public WakeCriterion() {
		// NOOP
	}

	public Identifier getId() {
		return ID;
	}

	public WakeCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new WakeCriterion.Conditions(extended);
	}

	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, (conditions) -> true);
	}

	public static class Conditions extends AbstractCriterionConditions {
		public Conditions(EntityPredicate.Extended player) {
			super(WakeCriterion.ID, player);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			return super.toJson(predicateSerializer);
		}
	}

}