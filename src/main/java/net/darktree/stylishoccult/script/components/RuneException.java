package net.darktree.stylishoccult.script.components;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.utils.RuneUtils;
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
        StylishOccult.LOGGER.warn( "Exception in script at: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " " + getMessage() );

        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        boolean safe = mode == SafeMode.ENABLED;
        RuneBlock rune = (RuneBlock) world.getBlockState(pos).getBlock();
        Criteria.EXCEPTION.trigger(world, pos, rune, getMessage(), safe);

        if (safe) {
            Network.DEFUSE_PACKET.send(pos, (ServerWorld) world);
        }else{
            float size = StylishOccult.SETTINGS.runicErrorExplosionSize.get(world);
            world.createExplosion(null, x, y, z, size, Explosion.DestructionType.BREAK);
            RuneUtils.createErrorReport(this, rune, world, pos);
        }
    }

}
