package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.RuneInstance;
import net.darktree.stylishoccult.script.components.RuneType;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RuneUtils;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
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

public class RuneBlock extends SimpleBlock implements BlockEntityProvider {

    public static final IntProperty COOLDOWN = IntProperty.of("cooldown", 0, 3);
    public final RuneType type;
    public final String name;

    public RuneBlock( RuneType type, String name ) {
        super( FabricBlockSettings.of(Material.STONE)
                .materialColor(MaterialColor.BLACK)
                .breakByTool(FabricToolTags.PICKAXES)
                .requiresTool() );

        this.type = type;
        this.name = name;
        setDefaultState( getDefaultState().with(COOLDOWN, 0) );
    }

    public String getTypeString() {
        return type.getName();
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
        builder.add(COOLDOWN);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new RuneBlockEntity();
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int cooldown = state.get(COOLDOWN);

        if( cooldown != 0 ) {
            world.setBlockState(pos, state.with(COOLDOWN, cooldown - 1));
            world.getBlockTickScheduler().schedule( pos, state.getBlock(), getDelayLength() );
            if( cooldown == 3 ) {
                executeStoredScript(world, pos);
            }
        }
        super.scheduledTick(state, world, pos, random);
    }

    protected void executeStoredScript( World world, BlockPos pos ) {
        RuneBlockEntity entity = getEntity(world, pos);

        if( entity != null && entity.hasScript() ) {
            RunicScript state = entity.getScript();
            state.apply( this, world, pos );
            Direction[] dirs = getDirections( world, pos, state );

            if( dirs.length >= 1 ) {
                state.setDirection( dirs[0] );
                propagateTo( world, pos, dirs[0], state );

                for( int i = 1; i < dirs.length; i ++ ) {
                    propagateTo( world, pos, dirs[i], entity.copyScript(dirs[i]) );
                }
            }

            entity.clear();
        }
    }

    protected void propagateTo( World world, BlockPos pos, Direction dir, RunicScript script ) {
        BlockPos target = pos.offset(dir);
        BlockState state = world.getBlockState(target);

        if (state.getBlock() instanceof RuneBlock) {
            RuneBlock runeBlock = (RuneBlock) state.getBlock();
            if( runeBlock.canAcceptSignal() ) {
                runeBlock.execute(world, target, state, script);
            }
        }
    }

    protected void execute( World world, BlockPos pos, BlockState state, RunicScript script ) {
        if( state.get(COOLDOWN) == 0 ) {
            RuneBlockEntity entity = getEntity(world, pos);

            if( entity != null ) {
                entity.store(script);
                world.setBlockState(pos, state.with(COOLDOWN, 3));
                world.getBlockTickScheduler().schedule( pos, state.getBlock(), getDelayLength() );
                onTriggered( world, pos, state );
            }
        }
    }

    protected final RuneBlockEntity getEntity( World world, BlockPos pos ) {
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

    public Direction[] getDirections( World world, BlockPos pos, RunicScript script ) {
        Direction dir = script.getDirection();
        return  dir == null ? new Direction[] {} : new Direction[] { dir };
    }

    public boolean canAcceptSignal() {
        return true;
    }

    public RuneInstance getInstance() {
        return null;
    }

    public void apply(RunicScript script, World world, BlockPos pos) {
        apply(script);
    }

    public void apply(RunicScript script) {

    }

    protected void onTriggered( World world, BlockPos pos, BlockState state ) {

    }

}
