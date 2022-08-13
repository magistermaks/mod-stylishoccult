package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.StylishOccult;
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
		super( FabricBlockSettings.of(Material.STONE)
				.mapColor(MapColor.BLACK)
				.requiresTool()
				.strength(2.5f, 6.0f));

		this.type = type;
		this.name = name;
		setDefaultState( getDefaultState().with(COOLDOWN, 0).with(FROZEN, false) );
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
				world.setBlockState(pos, state.with(COOLDOWN, cooldown - 1));
				world.getBlockTickScheduler().schedule(pos, state.getBlock(), getDelayLength());
				if (cooldown == 3) {
					executeStoredScript(world, pos);
				}
			} else {
				onDelayEnd(world, pos);
			}
		} catch(RuneException exception) {
			getEntity(world, pos).getScript().handle(exception, world, pos);
		}
	}

	protected void executeStoredScript(World world, BlockPos pos) {
		RuneBlockEntity entity = getEntity(world, pos);

		if (entity != null && entity.hasScript()) {
			Script script = entity.getScript();
			script.apply(this, world, pos);
			propagateTo(world, pos, script, getDirections(world, pos, script));

			entity.clear();
		}
	}

	protected void propagateTo(World world, BlockPos pos, Script script, Direction[] directions) {
		boolean used = false;

		for (Direction direction : directions) {

			BlockPos target = pos.offset(direction);
			BlockState state = world.getBlockState(target);

			if (state.getBlock() instanceof RuneBlock rune) {
				if (rune.canAcceptSignal(state, direction.getOpposite())) {
					rune.onSignalAccepted(world, pos);
					rune.execute(world, target, state, used ? script.copyFor(direction) : script.with(direction));
					used = true;
				}
			}

		}

		if(!used) {
			script.reset(world, pos);
		}
	}

	protected void execute(World world, BlockPos pos, BlockState state, Script script) {
		RuneBlockEntity entity = getEntity(world, pos);

		if( entity != null ) {
			entity.store(script);
			world.setBlockState(pos, state.with(COOLDOWN, 3));
			onTriggered(script, world, pos, state);
			world.getBlockTickScheduler().schedule( pos, state.getBlock(), getDelayLength() );
		}
	}

	protected final RuneBlockEntity getEntity(World world, BlockPos pos) {
		return BlockUtils.get(RuneBlockEntity.class, world, pos);
	}

	protected int getDelayLength() {
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

	public Direction[] getDirections(World world, BlockPos pos, Script script) {
		return Directions.of(script.direction);
	}

	public boolean canAcceptSignal(BlockState state, @Nullable Direction from) {
		return state.get(COOLDOWN) == 0 && !state.get(FROZEN);
	}

	public RuneInstance getInstance() {
		return null;
	}

	public void apply(Script script, World world, BlockPos pos) {
		apply(script);
	}

	public void apply(Script script) {

	}

	protected void onTriggered(Script script, World world, BlockPos pos, BlockState state) {

	}

	protected void onDelayEnd(World world, BlockPos pos) {

	}

	protected void onSignalAccepted(World world, BlockPos pos) {

	}

}
