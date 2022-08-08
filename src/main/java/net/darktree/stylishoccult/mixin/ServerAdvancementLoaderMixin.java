package net.darktree.stylishoccult.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(ServerAdvancementLoader.class)
public abstract class ServerAdvancementLoaderMixin {

	@Shadow @Final private LootConditionManager conditionManager;

	@Inject(method="apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at=@At(value="INVOKE", target="Lnet/minecraft/advancement/AdvancementManager;load(Ljava/util/Map;)V", shift=At.Shift.BEFORE), locals=LocalCapture.CAPTURE_FAILHARD)
	private void stylish_apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info, Map<Identifier, Advancement.Task> tasks, AdvancementManager advancementManager) {
		ModItems.RUNESTONES.forEach(item -> {
			Identifier itemId = Registry.ITEM.getId(item);
			Identifier taskId = ModIdentifier.of("gather/" + itemId.getPath());

			tasks.put(taskId, getRuneTask(itemId, taskId));
		});
	}

	@Unique
	private Advancement.Task getRuneTask(Identifier itemId, Identifier taskId) {
		JsonObject advancement = new JsonObject();

		// match only this one item
		JsonArray items = new JsonArray();
		items.add(itemId.toString());

		JsonObject itemEntry = new JsonObject();
		itemEntry.add("items", items);

		JsonArray itemList = new JsonArray();
		itemList.add(itemEntry);

		JsonObject condition = new JsonObject();
		condition.add("items", itemList);

		// create criterion
		JsonObject criterion = new JsonObject();
		criterion.addProperty("trigger", "minecraft:inventory_changed");
		criterion.add("conditions", condition);

		// criteria list
		JsonObject criteria = new JsonObject();
		criteria.add("gather", criterion);

		// add to the advancement
		advancement.add("criteria", criteria);

		JsonArray requirement = new JsonArray();
		requirement.add("gather");

		JsonArray requirements = new JsonArray();
		requirements.add(requirement);

		advancement.add("requirements", requirements);

		return toTask(advancement, taskId);
	}

	@Unique
	private Advancement.Task toTask(JsonObject json, Identifier id) {
		JsonObject jsonObject = JsonHelper.asObject(json, "advancement");
		return Advancement.Task.fromJson(jsonObject, new AdvancementEntityPredicateDeserializer(id, this.conditionManager));
	}

}
