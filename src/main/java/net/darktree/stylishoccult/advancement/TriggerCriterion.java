package net.darktree.stylishoccult.advancement;

import com.google.gson.JsonObject;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TriggerCriterion extends AbstractCriterion<TriggerCriterion.Conditions> {

	static final Identifier ID = new ModIdentifier("special_rune_trigger");

	public Identifier getId() {
		return ID;
	}

	public TriggerCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new TriggerCriterion.Conditions(extended, jsonObject);
	}

	public void trigger(World world, BlockPos pos, RuneBlock rune, boolean successful) {
		if (world instanceof ServerWorld server) {
			for(ServerPlayerEntity player : server.getPlayers()) {
				double distance = player.getPos().distanceTo(Vec3d.of(pos));
				this.trigger(player, (conditions) -> conditions.matches(rune.name,  distance, successful));
			}
		}
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final String rune;
		private final double distance;
		private final boolean successful;

		public Conditions(EntityPredicate.Extended player, JsonObject json) {
			super(TriggerCriterion.ID, player);
			this.rune = json.get("rune").getAsString();
			this.distance = json.get("distance").getAsDouble();
			this.successful = json.get("successful").getAsBoolean();
		}

		public boolean matches(String name, double distance, boolean successful) {
			return this.distance > distance && name.equals(this.rune) && this.successful == successful;
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = super.toJson(predicateSerializer);
			json.addProperty("rune", this.rune);
			json.addProperty("distance", this.distance);
			json.addProperty("successful", this.successful);
			return json;
		}
	}

}
