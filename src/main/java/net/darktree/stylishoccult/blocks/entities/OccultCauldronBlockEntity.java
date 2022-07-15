package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.ModBlocks;
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

public class OccultCauldronBlockEntity extends SimpleBlockEntity {

	private final static int MAX_AMOUNT = (int) FluidConstants.BUCKET;
	private final static float BOTTOM = 4.0f / 16.0f;
	private final static float HEIGHT = 15.0f / 16.0f - BOTTOM;

	private final Storage storage;
	private int amount = 0;

	public OccultCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.OCCULT_CAULDRON, pos, state);
		this.storage = new Storage();
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

	public float getLevel() {
		return BOTTOM + (amount / (float) MAX_AMOUNT) * HEIGHT;
	}

	private long getAmount() {
		return amount;
	}

	public class Storage extends SnapshotParticipant<MutableInteger> implements SingleSlotStorage<FluidVariant> {

		public boolean extract(long amount) {
			if (amount <= getAmount()) {
				try (Transaction transaction = Transaction.openOuter()) {
					extract(ModBlocks.BLOOD_VARIANT, amount, transaction);
					transaction.commit();
				}

				return true;
			}

			return false;
		}
		
		public boolean insert(long amount) {
			if (amount <= getEmpty()) {
				try (Transaction transaction = Transaction.openOuter()) {
					insert(ModBlocks.BLOOD_VARIANT, amount, transaction);
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
			return ModBlocks.BLOOD_VARIANT;
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
