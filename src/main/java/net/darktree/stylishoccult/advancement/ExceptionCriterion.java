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

public class ExceptionCriterion extends AbstractCriterion<ExceptionCriterion.Conditions> {
	static final Identifier ID = new ModIdentifier("exception");

	public Identifier getId() {
		return ID;
	}

	public ExceptionCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new ExceptionCriterion.Conditions(extended, jsonObject);
	}

	public void trigger(World world, BlockPos pos, RuneBlock rune, String message, boolean defused) {
		if (world instanceof ServerWorld server) {
			ItemStack stack = new ItemStack(rune.asItem());

			for(ServerPlayerEntity player : server.getPlayers()) {
				double distance = player.getPos().distanceTo(Vec3d.of(pos));
				this.trigger(player, (conditions) -> conditions.matches(message, stack, defused, distance));
			}
		}
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate rune;
		private final boolean defused;
		private final double distance;
		private final String exception;

		public Conditions(EntityPredicate.Extended player, JsonObject json) {
			super(ExceptionCriterion.ID, player);
			this.rune = ItemPredicate.fromJson(json.get("rune"));
			this.defused = json.get("defused").getAsBoolean();
			this.distance = json.get("distance").getAsDouble();

			if(!json.get("exception").isJsonNull()) {
				this.exception = json.get("exception").getAsString();
			}else{
				this.exception = null;
			}
		}

		public boolean matches(String message, ItemStack stack, boolean defused, double distance) {
			return (this.defused == defused) && (this.distance > distance) && (this.exception == null || message.equals(this.exception)) && this.rune.test(stack);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = super.toJson(predicateSerializer);
			json.add("rune", this.rune.toJson());
			json.addProperty("defused", this.defused);
			json.addProperty("distance", this.distance);
			json.addProperty("exception", this.exception);
			return json;
		}
	}

}
