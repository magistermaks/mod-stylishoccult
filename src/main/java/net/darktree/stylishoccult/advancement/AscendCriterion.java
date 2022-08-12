package net.darktree.stylishoccult.advancement;

import com.google.gson.JsonObject;
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

public class AscendCriterion extends AbstractCriterion<AscendCriterion.Conditions> {

	static final Identifier ID = new ModIdentifier("ascend");

	public Identifier getId() {
		return ID;
	}

	public AscendCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new AscendCriterion.Conditions(extended, jsonObject);
	}

	public void trigger(World world, BlockPos pos) {
		if (world instanceof ServerWorld server) {
			for (ServerPlayerEntity player : server.getPlayers()) {
				double distance = player.getPos().distanceTo(Vec3d.of(pos));
				this.trigger(player, (conditions) -> conditions.matches(distance));
			}
		}
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final double distance;

		public Conditions(EntityPredicate.Extended player, JsonObject json) {
			super(AscendCriterion.ID, player);
			this.distance = json.get("distance").getAsDouble();
		}

		public boolean matches(double distance) {
			return (this.distance > distance);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = super.toJson(predicateSerializer);
			json.addProperty("distance", this.distance);
			return json;
		}
	}

}
