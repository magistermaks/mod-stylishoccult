package net.darktree.stylishoccult.block.rune.trigger;

import net.darktree.stylishoccult.block.entity.rune.RuneBlockEntity;
import net.darktree.stylishoccult.block.rune.EntryRuneBlock;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ClockRuneBlock extends EntryRuneBlock {

    private final int timeout;

    public ClockRuneBlock(String name, int timeout) {
        super(name);
        this.timeout = timeout;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        onDelayEnd(world, pos);
    }

    @Override
    protected void onDelayEnd(World world, BlockPos pos) {
        world.getBlockTickScheduler().schedule(pos, world.getBlockState(pos).getBlock(), getDelayLength());
        RuneBlockEntity entity = getEntity(world, pos);

        if (!entity.hasMeta() || updateTime(entity)) {
            emit(world, pos, null);
        }
    }

    @Override
    protected void emit(World world, BlockPos pos, @Nullable PlayerEntity player) {
        RuneBlockEntity entity = getEntity(world, pos);

        NbtCompound tag = new NbtCompound();
        tag.putInt("time", timeout);
        entity.setMeta(tag);
        super.emit(world, pos, player);
    }

    private boolean updateTime(RuneBlockEntity entity) {
        try {
            NbtCompound nbt = entity.getMeta();
            int time = nbt.getInt("time");
            if (time <= 0) {
                entity.setMeta(null);
                return true;
            } else {
                nbt.putInt("time", time - 1);
                entity.setMeta(nbt);
                return false;
            }
        }catch (Exception exception){
            throw RuneException.of(RuneExceptionType.INVALID_STATE);
        }
    }

}
