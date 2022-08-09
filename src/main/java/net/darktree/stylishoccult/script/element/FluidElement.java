package net.darktree.stylishoccult.script.element;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.view.ElementView;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
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
			long amount = count;

			if (reminder > 0) {
				reminder --;
				amount ++;
			}

			list.add(new FluidElement(fluid, amount));
		}

		return list;
	}

	@Override
	public ElementView view() {
		Identifier fluid = Registry.FLUID.getId(this.fluid.getFluid());
		String text = amount + "x " + I18n.translate("block." + fluid.getNamespace() + "." + fluid.getPath());

		return ElementView.of("fluid", ElementView.FLUID_ICON, text, fluid.toString());
	}

	public boolean equals(StackElement element) {
		if (element instanceof FluidElement fluidElement) {
			return fluidElement.amount == this.amount && fluidElement.fluid.equals(this.fluid);
		}

		return super.equals(element);
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
