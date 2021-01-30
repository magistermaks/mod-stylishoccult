package net.darktree.stylishoccult.script.components;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.utils.RuneUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class RuneException extends RuntimeException {

    public static RuneException of(RuneExceptionType type) {
        return new RuneException( type.getName() );
    }

    private RuneException( String message ) {
        super(message);
    }

    public void apply(World world, BlockPos pos) {
        StylishOccult.debug( "Exception in script at: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " " + getMessage() );
        float size = StylishOccult.SETTINGS.runicErrorExplosionSize.get( world.getDifficulty() );
        world.createExplosion( null, pos.getX(), pos.getY(), pos.getZ(), size, Explosion.DestructionType.BREAK );
        RuneUtils.createErrorReport( this, world, pos );
    }

}
