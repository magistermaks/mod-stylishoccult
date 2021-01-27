package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.script.RunicScript;

public class DebugRuneBlockEntity extends RuneBlockEntity {

    public RunicScript scriptCopy = null;

    public DebugRuneBlockEntity() {
        super(BlockEntities.DEBUG_RUNESTONE);
    }

    @Override
    public void store( RunicScript script ) {
        super.store( script );
        scriptCopy = script.copyFor( script.getDirection() );
    }

}