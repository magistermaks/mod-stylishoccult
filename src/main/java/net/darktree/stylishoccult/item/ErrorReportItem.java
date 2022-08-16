package net.darktree.stylishoccult.item;

import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ErrorReportItem extends Item {

	public ErrorReportItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound nbt = stack.getNbt();

		if (nbt != null && !nbt.isEmpty()) {
			String x = String.valueOf(nbt.getInt("x"));
			String y = String.valueOf(nbt.getInt("y"));
			String z = String.valueOf(nbt.getInt("z"));

			tooltip.add(Utils.tooltip("error_tablet.error." + nbt.getString("error")));
			tooltip.add(Utils.tooltip("error_tablet.location", x, y, z, nbt.getString("rune")));
		} else {
			tooltip.add(Utils.tooltip("error_tablet.success"));
		}

		tooltip.add(Utils.tooltip("error_tablet.dispose").formatted(Formatting.DARK_GRAY));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) {
			Random random = world.getRandom();
			float p = 0.9f + (random.nextFloat() - 0.5f) * 0.25f;
			float v = 0.3f + (random.nextFloat() * 0.1f);

			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(Sounds.DISPOSE.event, p, v));
		}

		return TypedActionResult.consume(ItemStack.EMPTY);
	}

}
