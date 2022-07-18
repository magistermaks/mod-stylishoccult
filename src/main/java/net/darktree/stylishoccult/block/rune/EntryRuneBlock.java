package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class EntryRuneBlock extends RuneBlock {

    public EntryRuneBlock(String name) {
        super(RuneType.ENTRY, name);
    }

    protected void emit(World world, BlockPos pos, @Nullable PlayerEntity player) {
        if (player == null || player.getAbilities().allowModifyWorld) {
            emitScript(world, pos, new Script());
        }
    }

    protected void emitScript(World world, BlockPos pos, Script script) {
        BlockState state = world.getBlockState(pos);

        if (super.canAcceptSignal(state, null)) {
            execute(world, pos, state, script);
        }
    }

    @Override
    public Direction[] getDirections(World world, BlockPos pos, Script script) {
        return Direction.values();
    }

    @Override
    public boolean canAcceptSignal(BlockState state, @Nullable Direction from) {
        return false;
    }

}
