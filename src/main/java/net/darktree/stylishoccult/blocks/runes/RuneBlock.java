package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneInstance;
import net.darktree.stylishoccult.script.components.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RuneUtils;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public abstract class RuneBlock extends SimpleBlock implements BlockEntityProvider {

    public static final IntProperty COOLDOWN = IntProperty.of("cooldown", 0, 3);
    public static final BooleanProperty FROZEN = BooleanProperty.of("frozen");

    public final RuneType type;
    public final String name;

    public RuneBlock( RuneType type, String name ) {
        super( FabricBlockSettings.of(Material.STONE)
                .mapColor(MapColor.BLACK)
                .requiresTool()
                .strength(2.5f, 6.0f));

        this.type = type;
        this.name = name;
        setDefaultState( getDefaultState().with(COOLDOWN, 0).with(FROZEN, false) );
    }

    public String getTypeString() {
        return type.getName();
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.SIMPLE_RESISTANT;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add( Utils.tooltip( "rune", new TranslatableText( "rune." + super.getTranslationKey() ) ) );
    }

    @Override
    public String getTranslationKey() {
        return "block." + StylishOccult.NAMESPACE + ".engraved_runestone";
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
            }else{
                onDelayEnd(world, pos);
            }
        }catch(RuneException exception) {
            exception.apply(world, pos);
        }

        super.scheduledTick(state, world, pos, random);
    }

    protected void executeStoredScript(World world, BlockPos pos) {
        RuneBlockEntity entity = getEntity(world, pos);

        if( entity != null && entity.hasScript() ) {
            Script script = entity.getScript();
            script.apply(this, world, pos);
            propagateTo(world, pos, script, getDirections(world, pos, script));

            entity.clear();
        }
    }

    protected void propagateTo(World world, BlockPos pos, Script script, Direction[] directions) {
        boolean copy = false;

        if(directions.length == 0) {
            script.reset(world, pos);
        }

        for (Direction direction : directions) {

            BlockPos target = pos.offset(direction);
            BlockState state = world.getBlockState(target);

            if (state.getBlock() instanceof RuneBlock rune) {
                if (rune.canAcceptSignal()) {
                    rune.execute(world, target, state, copy ? script.copyFor(direction) : script.with(direction));
                    copy = true;
                }
            }

        }
    }

    protected void execute(World world, BlockPos pos, BlockState state, Script script) {
        if( state.get(COOLDOWN) == 0 && !state.get(FROZEN) ) {
            RuneBlockEntity entity = getEntity(world, pos);

            if( entity != null ) {
                entity.store(script);
                world.setBlockState(pos, state.with(COOLDOWN, 3));
                onTriggered(script, world, pos, state);
                world.getBlockTickScheduler().schedule( pos, state.getBlock(), getDelayLength() );
            }
        }
    }

    protected final RuneBlockEntity getEntity(World world, BlockPos pos) {
        RuneBlockEntity entity = BlockUtils.getEntity(RuneBlockEntity.class, world, pos);

        if( entity == null ) {
            StylishOccult.LOGGER.error("Missing block entity for RuneBlock!");
        }

        return entity;
    }

    protected int getDelayLength() {
        return 1;
    }

    public int getTint( BlockState state ) {
        return RuneUtils.getTint( state.get(COOLDOWN) );
    }

    public Direction[] getDirections(World world, BlockPos pos, Script script) {
        return script.direction == null ? new Direction[] {} : new Direction[] { script.direction };
    }

    public boolean canAcceptSignal() {
        return true;
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

}
