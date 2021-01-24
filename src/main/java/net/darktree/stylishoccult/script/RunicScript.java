package net.darktree.stylishoccult.script;

import net.darktree.stylishoccult.script.components.Rune;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneInstance;
import net.darktree.stylishoccult.script.components.RuneRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RunicScript {

    private Direction direction;
    private final double[] vars = new double[16];
    private RuneInstance instance = null;

    public RunicScript(Direction direction) {
        this.direction = direction;
    }

    public RunicScript() {
        // Pass dummy direction
        this(Direction.UP);
    }

    private CompoundTag varsToTag( CompoundTag tag ) {
        for( int i = 0; i < 16; i ++ ) {
            tag.putDouble(String.valueOf(i), vars[i]);
        }
        return tag;
    }

    private void varsFromTag( CompoundTag tag ) {
        try {
            for (int i = 0; i < 16; i++) {
                vars[i] = tag.getDouble(String.valueOf(i));
            }
        } catch (Exception ignore) {}
    }

    private void instanceFromTag( CompoundTag tag ) {
        if( tag != null ) {
            String name = tag.getString("rune");
            Rune rune = RuneRegistry.get(name);
            if( rune != null ) {
                instance = rune.getInstance();
                if( instance != null ) {
                    instance.fromTag(tag);
                }
            }
        }
    }

    public static RunicScript fromTag( CompoundTag tag ) {
        Direction direction = Direction.byId( tag.getInt("direction") );
        RunicScript script = new RunicScript(direction);
        script.varsFromTag( tag.getCompound("vars") );
        script.instanceFromTag( tag.getCompound("instance") );
        return script;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("direction", direction.getId());
        tag.put("vars", varsToTag( new CompoundTag() ));
        tag.put("instance", instance == null ? null : instance.toTag());
        return tag;
    }

    public Direction getDirection() {
        return direction;
    }

    public void write( int address, double val ) {
        assertAddress( address );
        vars[address] = val;
    }

    public double read( int address ) {
        assertAddress( address );
        return vars[address];
    }

    public void push( Rune rune, World world, BlockPos pos ) {
        RuneInstance inst = rune.getInstance();

        if (inst != null) {
            if (instance == null) {
                instance = inst;
            } else {
                if (!instance.push(inst)) {
                    instance = inst;
                }
            }

            inst.update(this, rune, world, pos);
        }
    }

    public void apply( Rune rune, World world, BlockPos pos ) {
        try {
            push( rune, world, pos );
            rune.apply( this, world, pos );
        } catch (RuneException exception) {
            exception.apply( world, pos );
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public RunicScript copyFor(Direction direction) {
        RunicScript script = fromTag( toTag() );
        script.setDirection( direction );
        return script;
    }

    public void assertAddress(int address ) {
        if( address < 0 || address >= 16 ) {
            throw new RuneException("Invalid address!");
        }
    }

}
