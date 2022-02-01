package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.elements.NumericElement;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.components.RuneInstance;
import net.darktree.stylishoccult.script.components.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.nbt.NbtCompound;

public class NumberRuneBlock extends RuneBlock {

    public final char value;

    public NumberRuneBlock(String name, char value) {
        super(RuneType.INPUT, name);
        this.value = value;
    }

    @Override
    public RuneInstance getInstance() {
        NumberRuneInstance instance = new NumberRuneInstance(this);
        instance.raw += value;
        return instance;
    }

    public static class NumberRuneInstance extends RuneInstance {

        String raw = "";

        public NumberRuneInstance(RuneBlock rune) {
            super(rune);
        }

        @Override
        public NbtCompound writeNbt(NbtCompound tag) {
            tag.putString("raw", raw);
            return super.writeNbt( tag );
        }

        @Override
        public void readNbt(NbtCompound tag ) {
            raw = tag.getString("raw");
        }

        @Override
        public boolean push(Script script, RuneInstance instance ) {

            if( raw.length() > 16 ) {
                throw RuneExceptionType.NUMBER_TOO_LONG.get();
            }

            if( instance instanceof NumberRuneInstance ) {
                raw += ((NumberRuneBlock) instance.rune).value;

                try {
                    Integer.parseInt(raw, 6);
                }catch (Exception e){
                    throw RuneExceptionType.INVALID_NUMBER.get();
                }

                return true;
            }

            try {
                // FIXME: this is not allowed, the value gets replaced
                script.value = new NumericElement(Integer.parseInt(raw, 6));
            }catch (Exception e){
                throw RuneExceptionType.INVALID_NUMBER.get();
            }

            return false;
        }

    }

}
