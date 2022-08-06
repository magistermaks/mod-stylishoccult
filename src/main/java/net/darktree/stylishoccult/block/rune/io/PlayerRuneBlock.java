package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.block.rune.InputRuneBlock;
import net.darktree.stylishoccult.script.element.NumericElement;
import net.darktree.stylishoccult.script.engine.Script;
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
    public void apply(Script script, World world, BlockPos pos) {
        script.stack.push(check(world, box.offset(pos)) ? NumericElement.TRUE : NumericElement.FALSE);
    }
}
