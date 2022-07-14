package net.darktree.stylishoccult.script.elements;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FluidElement extends StackElement {

	private final FluidVariant fluid;
	private final long amount;

	public FluidElement(FluidVariant fluid, long amount) {
		this.fluid = fluid;
		this.amount = amount;
	}

	public FluidElement(NbtCompound nbt) {
		this.fluid = FluidVariant.fromNbt(nbt.getCompound("v"));
		this.amount = nbt.getLong("a");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("v", this.fluid.toNbt());
		nbt.putLong("a", this.amount);
		return super.writeNbt(nbt);
	}

	@Override
	public double value() {
		return this.amount;
	}

	@Override
	public StackElement copy() {
		throw RuneException.of(RuneExceptionType.UNMET_EQUIVALENCY);
	}

	@Override
	public List<StackElement> split(int split) {
		ArrayList<StackElement> list = new ArrayList<>();

		long reminder = amount % split;
		long count = amount / split;

		for (int i = 0; i < split; i ++) {
			list.add(new FluidElement(fluid, count + reminder));

			if (reminder > 0) {
				reminder --;
			}
		}

		return list;
	}

	public boolean equals(StackElement element) {
		if (element instanceof FluidElement fluidElement) {
			return fluidElement.amount == this.amount && fluidElement.fluid.equals(this.fluid);
		}

		return super.equals(element);
	}

	@Override
	public String toString() {
		return "FluidElement " + this.amount + "x " + this.fluid.toNbt();
	}

	@Override
	public void drop(World world, BlockPos pos) {
		long remainder = insert(world, pos);

		if (remainder > 0) {
			// TODO do something creative here
			// TODO spawn particles and/or place a source block if > FluidConstants.BLOCK
			StylishOccult.LOGGER.info("{} units of fluid were lost to entropy!", remainder);
		}
	}

	private long insert(World world, BlockPos pos) {
		long left = this.amount;

		for(Direction direction : Direction.values()) {

			// It should never be less than 0, but just to be sure...
			if (left <= 0) {
				return 0;
			}

			Storage<FluidVariant> target = FluidStorage.SIDED.find(world, pos.offset(direction), direction.getOpposite());

			if (target != null) {
				try (Transaction transaction = Transaction.openOuter()) {
					left -= target.insert(this.fluid, left, transaction);
					transaction.commit();
				}
			}
		}

		return left;
	}

}
