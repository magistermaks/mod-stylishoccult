package net.darktree.stylishoccult.block.fluid;

import net.darktree.stylishoccult.utils.RegUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.FlowableFluid;

public class ModFluids {

	// fluids
	public static final FlowableFluid STILL_BLOOD = RegUtil.fluid("blood", new BloodFluid.Still());
	public static final FlowableFluid FLOWING_BLOOD = RegUtil.fluid("flowing_blood", new BloodFluid.Flowing());

	public static final FluidVariant BLOOD_VARIANT = FluidVariant.of(STILL_BLOOD);

}
