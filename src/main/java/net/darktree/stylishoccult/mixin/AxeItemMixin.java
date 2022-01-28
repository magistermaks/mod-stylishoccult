package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {

	@ModifyVariable(method="useOnBlock", name="optional3", at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/item/ItemUsageContext;getStack()Lnet/minecraft/item/ItemStack;"))
	public Optional<BlockState> useOnBlock(Optional<BlockState> optional2, ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();

		if( !optional2.isPresent() && world.getBlockState(pos).getBlock() == ModBlocks.STONE_FLESH ) {
			Block.dropStack(world, pos, new ItemStack(ModItems.VEINS, world.random.nextInt(3) + 1));
			return Optional.of(Blocks.STONE.getDefaultState());
		}

		return optional2;
	}

}
