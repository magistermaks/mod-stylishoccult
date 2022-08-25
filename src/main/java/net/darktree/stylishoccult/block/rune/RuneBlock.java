package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.entity.rune.RuneBlockAttachment;
import net.darktree.stylishoccult.block.entity.rune.RuneBlockEntity;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneInstance;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.Directions;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class RuneBlock extends SimpleBlock implements BlockEntityProvider {

	public static final int COLOR_0 = 0x852020;
	public static final int COLOR_1 = 0xA52020;
	public static final int COLOR_2 = 0xD52020;
	public static final int COLOR_3 = 0xF52020;

	public static final IntProperty COOLDOWN = IntProperty.of("cooldown", 0, 3);
	public static final BooleanProperty FROZEN = BooleanProperty.of("frozen");

	public final RuneType type;
	public final String name;

	public RuneBlock(RuneType type, String name) {
		super(FabricBlockSettings.of(Material.STONE).mapColor(MapColor.BLACK).requiresTool().strength(2.5f, 6.0f));

		this.type = type;
		this.name = name;
		setDefaultState(getDefaultState().with(COOLDOWN, 0).with(FROZEN, false));
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.SIMPLE_RESISTANT;
	}

	@Override
	public String getTranslationKey() {
		return "block." + StylishOccult.NAMESPACE + ".engraved_runestone";
	}

	@Override
	public MutableText getName() {
		return new TranslatableText(this.getTranslationKey(), new TranslatableText("rune." + super.getTranslationKey()));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COOLDOWN, FROZEN);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RuneBlockEntity(pos, state);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int cooldown = state.get(COOLDOWN);

		try {
			if (cooldown != 0) {
				state = state.with(COOLDOWN, cooldown - 1);
				world.setBlockState(pos, state);
				world.createAndScheduleBlockTick(pos, this, getDelayLength());

				if (cooldown == 3) {
					executeStoredScript(world, pos, state);
				}
			} else {
				onDelayEnd(world, pos);
			}
		} catch (RuneException exception) {
			getEntity(world, pos).getScript().handle(exception, world, pos);
		}
	}

	@Override
	public final void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.isOf(newState.getBlock())) {
			return;
		}

		RuneBlockEntity entity = getEntity(world, pos);
		Script script = entity.getScript();

		if (script != null) {
			StylishOccult.LOGGER.info("Rune was destroyed before the stored script was executed");
			script.reset(world, pos);
		}

		onRuneBroken(world, pos, entity.getAttachment());
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	private void executeStoredScript(World world, BlockPos pos, BlockState state) {
		RuneBlockEntity entity = getEntity(world, pos);

		if (entity.hasScript()) {
			Script script = entity.getScript();
			script.apply(this, world, pos);

			Direction[] directions = getDirections(world, pos, state, script, entity.getAttachment());
			propagateTo(world, pos, script, directions, entity.getDirection());

			// clear has to be after propagateTo as that method can throw runic exceptions
			// and those need to be handled in scheduledTick
			entity.clear();
		}
	}

	protected final void propagateTo(World world, BlockPos pos, Script script, Direction[] directions, @Nullable Direction except) {
		boolean used = false;

		for (Direction direction : directions) {

			if (direction == except) {
				continue;
			}

			BlockPos target = pos.offset(direction);
			BlockState state = world.getBlockState(target);

			if (state.getBlock() instanceof RuneBlock rune) {
				Direction from = direction.getOpposite();

				if (rune.canAcceptSignal(state, from)) {
					rune.onSignalAccepted(world, pos);
					rune.execute(world, target, state, used ? script.copyFor(direction) : script.with(direction), from);
					used = true;
				}
			}
		}

		// if the script wasn't propagated anywhere dump the contents
		if (!used) {
			script.reset(world, pos);
		}
	}

	protected final void execute(World world, BlockPos pos, BlockState state, Script script, @Nullable Direction from) {
		RuneBlockEntity entity = getEntity(world, pos);

		entity.store(script, from);
		state = state.with(COOLDOWN, 3);
		world.setBlockState(pos, state);
		onTriggered(script, world, pos, state);
		world.createAndScheduleBlockTick(pos, this, getDelayLength());
	}

	protected final RuneBlockEntity getEntity(World world, BlockPos pos) {
		return BlockUtils.get(RuneBlockEntity.class, world, pos);
	}

	protected final int getDelayLength() {
		return 1;
	}

	public int getTint(BlockState state) {
		return switch (state.get(COOLDOWN)) {
			case 0 -> COLOR_0;
			case 1 -> COLOR_1;
			case 2 -> COLOR_2;
			case 3 -> COLOR_3;
			default -> 0;
		};
	}

	/**
	 * Returns the directions in which the output signal will be propagated, by default keeps the current one
	 */
	public Direction[] getDirections(World world, BlockPos pos, BlockState state, Script script, RuneBlockAttachment attachment) {
		return Directions.of(script.direction);
	}

	/**
	 * Used to determine if a rune can be triggered or not
	 */
	public boolean canAcceptSignal(BlockState state, @Nullable Direction from) {
		return state.get(COOLDOWN) == 0 && !state.get(FROZEN);
	}

	/**
	 * Used for crating a RuneInstance of this rune, useful for gathering additional state across rune activations
	 */
	public RuneInstance getInstance() {
		return null;
	}

	/**
	 * Called {@link RuneBlock#getDelayLength()} ticks after {@link RuneBlock#onTriggered}, just before the signal is about to be propagated
	 */
	public void apply(Script script, World world, BlockPos pos) {

	}

	/**
	 * Called when the rune was just powered
	 */
	protected void onTriggered(Script script, World world, BlockPos pos, BlockState state) {

	}

	/**
	 * Called when the cooldown period runs out
	 */
	protected void onDelayEnd(World world, BlockPos pos) {

	}

	/**
	 * Works like {@link RuneBlock#onTriggered} but is not triggered if the rune activated itself
	 */
	protected void onSignalAccepted(World world, BlockPos pos) {

	}

	/**
	 * Called when the block is destroyed and stacks should be dropped
	 */
	protected void onRuneBroken(World world, BlockPos pos, RuneBlockAttachment attachment) {

	}

}
