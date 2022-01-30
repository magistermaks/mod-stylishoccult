package net.darktree.stylishoccult.script;

import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneInstance;
import net.darktree.stylishoccult.script.components.RuneRegistry;
import net.darktree.stylishoccult.script.components.StackManager;
import net.minecraft.nbt.NbtCompound;
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

    private void instanceFromNbt(NbtCompound nbt) {
        String name = nbt.getString("rune");
        RuneBlock rune = RuneRegistry.get(name);
        if( rune != null ) {
            instance = rune.getInstance();
            if( instance != null ) {
                instance.fromTag(nbt);
            }
        }
    }

    public static RunicScript fromNbt(NbtCompound nbt) {
        RunicScript script = new RunicScript();

        script.value = nbt.getDouble("value");
        if( nbt.contains("direction") ) script.setDirection( Direction.byId( nbt.getInt("direction") ) );
        if( nbt.contains("instance") ) script.instanceFromNbt( nbt.getCompound("instance") );
        script.stack.stackFromNbt( nbt.getCompound("stack") );

        return script;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        if( direction != null) nbt.putInt("direction", direction.getId());
        if( instance != null ) nbt.put("instance", instance.toTag(new NbtCompound()));
        nbt.put("stack", stack.stackToNbt( new NbtCompound() ));
        nbt.putDouble("value", value);
        return nbt;
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
            rune.apply(this, world, pos);
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
        if( instance != null ) script.instanceFromNbt( instance.toTag( new NbtCompound() ) );
        return script;
    }

    public StackManager getStack() {
        return stack;
    }

    private void reset() {
        stack.reset();
        instance = null;
    }

    public void combine( RunicScript script ) {
        this.getStack().combine( script.getStack() );
    }

}
