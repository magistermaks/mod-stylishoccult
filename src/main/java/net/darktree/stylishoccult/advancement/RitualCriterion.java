package net.darktree.stylishoccult.advancement;

import com.google.gson.JsonObject;
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

public class RitualCriterion extends AbstractCriterion<RitualCriterion.Conditions> {

	static final Identifier ID = new ModIdentifier("altar");

	public Identifier getId() {
		return ID;
	}

	public RitualCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new RitualCriterion.Conditions(extended, jsonObject);
	}

	public void trigger(World world, BlockPos pos, ItemStack catalyst, ItemStack product) {
		if (world instanceof ServerWorld server) {
			for(ServerPlayerEntity player : server.getPlayers()) {
				double distance = player.getPos().distanceTo(Vec3d.of(pos));
				this.trigger(player, (conditions) -> conditions.matches(catalyst, product, distance));
			}
		}
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate catalyst;
		private final ItemPredicate product;
		private final double distance;

		public Conditions(EntityPredicate.Extended player, JsonObject json) {
			super(RitualCriterion.ID, player);
			this.catalyst = ItemPredicate.fromJson(json.get("catalyst"));
			this.product = ItemPredicate.fromJson(json.get("product"));
			this.distance = json.get("distance").getAsDouble();
		}

		public boolean matches(ItemStack catalyst, ItemStack product, double distance) {
			return (this.distance > distance) && this.catalyst.test(catalyst) && this.product.test(product);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = super.toJson(predicateSerializer);
			json.add("catalyst", this.catalyst.toJson());
			json.add("product", this.product.toJson());
			json.addProperty("distance", this.distance);
			return json;
		}
	}

}
