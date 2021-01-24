package net.darktree.stylishoccult.script.components;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RuneException extends RuntimeException {

    public RuneException( String message ) {
        super(message);
    }

    public void apply(World world, BlockPos pos) {
        System.out.println("Oh no! Exception in script at BlockPos: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " " + getMessage() );
        // world.createExplosion( null, pos.getX(), pos.getY(), pos.getZ(), 1.0f, Explosion.DestructionType.BREAK );
    }

}
