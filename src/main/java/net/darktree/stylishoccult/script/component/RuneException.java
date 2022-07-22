package net.darktree.stylishoccult.script.component;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.network.Network;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class RuneException extends RuntimeException {

    public static RuneException of(RuneExceptionType type) {
        return new RuneException( type.getName() );
    }

    public RuneException(String message) {
        super(message);
    }

    public void apply(World world, BlockPos pos, SafeMode mode) {
        StylishOccult.LOGGER.warn("Exception in script at: " + pos.toShortString() + " " + getMessage());

        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        boolean safe = mode == SafeMode.ENABLED;
        RuneBlock rune = (RuneBlock) world.getBlockState(pos).getBlock();
        Criteria.EXCEPTION.trigger(world, pos, rune, getMessage(), safe);

        if (safe) {
            Network.DEFUSE_PACKET.send(pos, (ServerWorld) world);
        }else{
            float size = StylishOccult.SETTING.rune_error_explosion.get(world);
            world.createExplosion(null, x, y, z, size, Explosion.DestructionType.BREAK);
            createErrorReport(rune, world, pos);
        }
    }

    private void createErrorReport(RuneBlock rune, World world, BlockPos pos) {
        NbtCompound tag = new NbtCompound();
        tag.putString("error", getMessage());
        tag.putString("rune", rune.name);
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());

        ItemStack stack = new ItemStack(ModItems.RUNE_ERROR_REPORT, 1);
        stack.setNbt(tag);
        Block.dropStack(world, pos, stack);
    }

}
