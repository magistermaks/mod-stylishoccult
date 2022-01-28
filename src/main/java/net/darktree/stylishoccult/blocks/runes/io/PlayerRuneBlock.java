package net.darktree.stylishoccult.blocks.runes.io;

import net.darktree.stylishoccult.blocks.runes.InputRuneBlock;
import net.darktree.stylishoccult.script.RunicScript;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PlayerRuneBlock extends InputRuneBlock {

    private final Box box;

    public PlayerRuneBlock(String name, double distance) {
        super(name);
        this.box = new Box(distance, distance, distance, -distance, -distance, -distance);
    }

    private boolean check( World world, Box box ) {
        return !world.getNonSpectatingEntities(PlayerEntity.class, box).isEmpty();
    }

    @Override
    public void apply(RunicScript script, World world, BlockPos pos) {
        script.getStack().put( check(world, box.offset(pos)) ? 1.0 : 0.0 );
    }
}
