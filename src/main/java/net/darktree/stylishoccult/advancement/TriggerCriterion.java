package net.darktree.stylishoccult.advancement;

import com.google.gson.JsonObject;
import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TriggerCriterion extends AbstractCriterion<TriggerCriterion.Conditions> {
	static final Identifier ID = new ModIdentifier("trigger");

	public Identifier getId() {
		return ID;
	}

	public TriggerCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new TriggerCriterion.Conditions(extended, jsonObject);
	}

	public void trigger(World world, BlockPos pos, RuneBlock rune) {
		if (world instanceof ServerWorld server) {
			ItemStack stack = new ItemStack(rune.asItem());

			for(ServerPlayerEntity player : server.getPlayers()) {
				double distance = player.getPos().distanceTo(Vec3d.of(pos));
				this.trigger(player, (conditions) -> conditions.matches(stack, distance));
			}
		}
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate rune;
		private final double distance;

		public Conditions(EntityPredicate.Extended player, JsonObject json) {
			super(TriggerCriterion.ID, player);
			this.rune = ItemPredicate.fromJson(json.get("rune"));
			this.distance = json.get("distance").getAsDouble();
		}

		public boolean matches(ItemStack rune, double distance) {
			return this.distance > distance && this.rune.test(rune);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = super.toJson(predicateSerializer);
			json.add("rune", this.rune.toJson());
			json.addProperty("distance", this.distance);
			return json;
		}
	}

}
