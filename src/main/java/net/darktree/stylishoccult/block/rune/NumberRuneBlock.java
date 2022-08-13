package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.component.RuneInstance;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.element.NumericElement;
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
		public RuneInstance copy() {
			NumberRuneInstance instance = new NumberRuneInstance(this.rune);
			instance.raw = this.raw;
			return instance;
		}

		@Override
		public RuneInstance choose(Script script, RuneInstance instance ) {

			if( instance instanceof NumberRuneInstance ) {
				raw += ((NumberRuneBlock) instance.rune).value;
				parse(raw, 6);

				if( raw.length() > 16 ) {
					throw RuneException.of(RuneExceptionType.NUMBER_TOO_LONG);
				}

				return this;
			}else{
				script.stack.push(new NumericElement(parse(raw, 6)));
			}

			return instance;
		}

		/**
		 * A naive float parser,
		 * intentionally ignores possible exceptions.
		 */
		private double parse(String string, int base) {
			try {
				String[] parts = string.split("\\.");
				double value = Integer.parseInt(parts[0], base);

				if(parts.length == 2 && parts[1].length() > 0) {
					value += Integer.parseInt(parts[1], base) / Math.pow(base, parts[1].length());
				}

				return value;
			}catch (Exception e){
				throw RuneException.of(RuneExceptionType.INVALID_NUMBER);
			}
		}

	}

}
