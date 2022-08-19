package net.darktree.stylishoccult.script.element;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.view.ElementView;
import net.darktree.stylishoccult.utils.Directions;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		String text = amount == 0 ? "(empty)" : amount + "x " + I18n.translate("block." + fluid.getNamespace() + "." + fluid.getPath());

		return ElementView.of("fluid", ElementView.FLUID_ICON, text, fluid.toString());
	}

	@Override
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
			ParticleEffect particle = fluid.getFluid().getDefaultState().getParticle();
			Random random = world.getRandom();

			if (particle != null) {
				for (Direction direction : Directions.ALL_EXCEPT_UP) {
					BlockPos target = pos.offset(direction);

					if (!world.getBlockState(target).isOpaqueFullCube(world, target)){
						Direction.Axis axis = direction.getAxis();
						double x = pos.getX() + (axis == Direction.Axis.X ? 0.5 + 0.53 * direction.getOffsetX() : random.nextFloat());
						double y = pos.getY() + (axis == Direction.Axis.Y ? 0.5 + 0.53 * direction.getOffsetY() : random.nextFloat());
						double z = pos.getZ() + (axis == Direction.Axis.Z ? 0.5 + 0.53 * direction.getOffsetZ() : random.nextFloat());

						Particles.spawn(world, particle, x, y, z, 1);
					}
				}
			}

			StylishOccult.debug(remainder + " droplets of fluid were lost to entropy!");
		}
	}

	private long insert(World world, BlockPos pos) {
		long left = this.amount;

		for (Direction direction : Directions.ALL) {

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
