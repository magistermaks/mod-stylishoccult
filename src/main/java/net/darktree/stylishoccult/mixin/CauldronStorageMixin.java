package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.BloodCauldronBlockEntity;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.impl.transfer.fluid.CauldronStorage;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(value=CauldronStorage.class, remap=false)
public abstract class CauldronStorageMixin {

	@Shadow protected abstract CauldronFluidContent getCurrentContent();

	@Inject(method="insert(Lnet/fabricmc/fabric/api/transfer/v1/fluid/FluidVariant;JLnet/fabricmc/fabric/api/transfer/v1/transaction/TransactionContext;)J", at=@At("TAIL"), cancellable=true)
	private void insert(FluidVariant fluidVariant, long maxAmount, TransactionContext transaction, CallbackInfoReturnable<Long> info) {
		if (fluidVariant == ModBlocks.BLOOD_VARIANT && getCurrentContent().fluid == Fluids.EMPTY) {

			CauldronStorage self = ((CauldronStorage) (Object) this);
			World world = null;
			BlockPos pos = null;

			try {
				Field locationFiled = self.getClass().getDeclaredField("location");
				locationFiled.setAccessible(true);
				Object location = locationFiled.get(self);

				for (Field field : location.getClass().getDeclaredFields()) {
					field.setAccessible(true);

					if (field.getType() == World.class) {
						world = (World) field.get(location);
					}

					if (field.getType() == BlockPos.class) {
						pos = (BlockPos) field.get(location);
					}
				}
			}catch (Exception e) {
				StylishOccult.LOGGER.error("Failed to access location with reflection!");
				return;
			}

			long amount = Math.min(FluidConstants.BUCKET, maxAmount);

			if (amount > 0) {
				self.updateSnapshots(transaction);
				world.setBlockState(pos, ModBlocks.BLOOD_CAULDRON.getDefaultState(), 0);
				BlockUtils.getEntity(BloodCauldronBlockEntity.class, world, pos).getStorage().insert(fluidVariant, amount, transaction);
			}

			info.setReturnValue(amount);
		}
	}

}
