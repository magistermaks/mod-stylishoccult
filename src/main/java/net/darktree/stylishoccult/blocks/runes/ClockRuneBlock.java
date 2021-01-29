package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.blocks.entities.RuneBlockEntity;
import net.darktree.stylishoccult.script.components.RuneException;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    protected void onDelayEnd( World world, BlockPos pos ) {
        world.getBlockTickScheduler().schedule(pos, world.getBlockState(pos).getBlock(), getDelayLength());
        RuneBlockEntity entity = getEntity(world, pos);

        try {
            if( !entity.hasMeta() || updateTime(entity) ) {
                emit(world, pos);
            }
        }catch (Exception exception){
            throw new RuneException("invalid_metadata");
        }
    }

    @Override
    protected void emit(World world, BlockPos pos) {
        RuneBlockEntity entity = getEntity(world, pos);

        CompoundTag tag = new CompoundTag();
        tag.putInt("time", timeout);
        entity.setMeta(tag);
        super.emit(world, pos);
    }

    private boolean updateTime(RuneBlockEntity entity) {
        CompoundTag tag = entity.getMeta();
        int time = tag.getInt("time");
        if( time <= 0 ) {
            entity.setMeta(null);
            return true;
        }else{
            tag.putInt("time", time - 1);
            entity.setMeta(tag);
            return false;
        }
    }

}
