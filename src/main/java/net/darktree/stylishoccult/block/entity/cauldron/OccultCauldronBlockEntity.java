package net.darktree.stylishoccult.block.entity.cauldron;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.OccultCauldronBlock;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.fluid.ModFluids;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.utils.MutableInteger;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OccultCauldronBlockEntity extends SimpleBlockEntity {

	private final static int MAX_AMOUNT = (int) FluidConstants.BUCKET;
	private final static float BOTTOM = 4.0f / 16.0f;
	private final static float HEIGHT = 15.0f / 16.0f - BOTTOM;

	private final Storage storage;
	private int amount = 0;

	private int boiling = 10;
	private int timer = 0;

	public OccultCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.OCCULT_CAULDRON, pos, state);
		this.storage = new Storage();
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if (amount == 0 || world == null || !world.isClient || state.getBlock() != ModBlocks.OCCULT_CAULDRON || !state.get(OccultCauldronBlock.BOILING)) {
			timer = 0;
			boiling = 10;
			return;
		}

		if (timer >= boiling) {
			if (boiling > 0) {
				boiling --;
				timer = 0;
			}

			float level = getLevel(amount);

			float x = pos.getX() + world.random.nextFloat() * (12.0f / 16.0f) + (2.0f / 16.0f);
			float z = pos.getZ() + world.random.nextFloat() * (12.0f / 16.0f) + (2.0f / 16.0f);

			world.addParticle(Particles.BOILING_BLOOD, false, x, pos.getY() + level, z, 0, 0, 0);
		}

		timer ++;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("amount", amount);
		return super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		amount = nbt.getInt("amount");
		super.readNbt(nbt);
	}

	public Storage getStorage() {
		return storage;
	}

	private void setAmount(long amount) {
		if (amount > MAX_AMOUNT || amount < 0) {
			throw new RuntimeException("Invalid fluid amount!");
		}

		this.amount = (int) amount;
	}

	public static float getLevel(long amount) {
		return BOTTOM + (amount / (float) MAX_AMOUNT) * HEIGHT;
	}

	private long getAmount() {
		return amount;
	}

	public class Storage extends SnapshotParticipant<MutableInteger> implements SingleSlotStorage<FluidVariant> {

		public boolean extract(long amount) {
			if (amount <= getAmount()) {
				try (Transaction transaction = Transaction.openOuter()) {
					extract(ModFluids.BLOOD_VARIANT, amount, transaction);
					transaction.commit();
				}

				return true;
			}

			return false;
		}
		
		public boolean insert(long amount) {
			if (amount <= getEmpty()) {
				try (Transaction transaction = Transaction.openOuter()) {
					insert(ModFluids.BLOOD_VARIANT, amount, transaction);
					transaction.commit();
				}

				return true;
			}

			return false;
		}

		private long getEmpty() {
			return getCapacity() - getAmount();
		}

		@Override
		public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
			if (resource == getResource()) {
				long next = Math.min(getEmpty(), maxAmount);
				updateSnapshots(transaction);
				setAmount(next + getAmount());
				return next;
			}

			return 0;
		}

		@Override
		public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
			if (resource == getResource()) {
				long next = Math.min(getAmount(), maxAmount);
				updateSnapshots(transaction);
				setAmount(getAmount() - next);
				return next;
			}

			return 0;
		}

		@Override
		public boolean isResourceBlank() {
			return false;
		}

		@Override
		public FluidVariant getResource() {
			return ModFluids.BLOOD_VARIANT;
		}

		@Override
		public long getAmount() {
			return OccultCauldronBlockEntity.this.getAmount();
		}

		@Override
		public long getCapacity() {
			return MAX_AMOUNT;
		}

		@Override
		protected MutableInteger createSnapshot() {
			return new MutableInteger((int) getAmount());
		}

		@Override
		protected void readSnapshot(MutableInteger snapshot) {
			setAmount(snapshot.value);
		}

		@Override
		protected void onFinalCommit() {
			update();
		}

	}

}
