package net.darktree.stylishoccult.script;

import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneInstance;
import net.darktree.stylishoccult.script.components.RuneRegistry;
import net.darktree.stylishoccult.script.components.StackManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RunicScript {

    private Direction direction;
    private RuneInstance instance = null;
    private final StackManager stack = new StackManager();
    public double value = 0;

    public RunicScript(Direction direction) {
        this.direction = direction;
    }

    public RunicScript() {
        this(null);
    }

    private void instanceFromTag( CompoundTag tag ) {
        String name = tag.getString("rune");
        RuneBlock rune = RuneRegistry.get(name);
        if( rune != null ) {
            instance = rune.getInstance();
            if( instance != null ) {
                instance.fromTag(tag);
            }
        }
    }

    public static RunicScript fromTag( CompoundTag tag ) {
        RunicScript script = new RunicScript();

        script.value = tag.getDouble("value");
        if( tag.contains("direction") ) script.setDirection( Direction.byId( tag.getInt("direction") ) );
        if( tag.contains("instance") ) script.instanceFromTag( tag.getCompound("instance") );
        script.stack.stackFromTag( tag.getCompound("stack") );

        return script;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        if( direction != null) tag.putInt("direction", direction.getId());
        if( instance != null ) tag.put("instance", instance.toTag(new CompoundTag()));
        tag.put("stack", stack.stackToTag( new CompoundTag() ));
        tag.putDouble("value", value);
        return tag;
    }

    public Direction getDirection() {
        return direction;
    }

    public void push(RuneBlock rune) {
        RuneInstance inst = rune.getInstance();

        if (instance == null) {
            instance = inst;
        } else {
            if( !instance.push(this, inst) ) {
                instance = inst;
            }
        }
    }

    public void apply(RuneBlock rune, World world, BlockPos pos ) {
        try {
            push(rune);
            rune.apply( this, world, pos );
            stack.validate();
        } catch (RuneException exception) {
            exception.apply( world, pos );
            reset();
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public RunicScript copyFor(Direction direction) {
        RunicScript script = new RunicScript(direction);
        script.value = value;
        script.stack.copy( stack );
        if( instance != null ) script.instanceFromTag( instance.toTag( new CompoundTag() ) );
        return script;
    }

    public StackManager getStack() {
        return stack;
    }

    private void reset() {
        stack.reset();
        instance = null;
    }

}
