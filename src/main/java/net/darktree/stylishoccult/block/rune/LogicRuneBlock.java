package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.element.NumericElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LogicRuneBlock extends RuneBlock {

	interface LogicFunction {
		void apply(Script script, World world, BlockPos pos);
	}

	private final LogicFunction function;

	public LogicRuneBlock(String name, LogicFunction function ) {
		super(RuneType.LOGIC, name);
		this.function = function;
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		function.apply(script, world, pos);
	}

	public static class Functions {

		private static double get(Script script, World world, BlockPos pos) {
			return script.pull(world, pos).value();
		}

		private static void put(Script script, double value) {
			script.stack.push(new NumericElement(value));
		}

		private static void put(Script script, boolean value) {
			script.stack.push(value ? NumericElement.TRUE : NumericElement.FALSE);
		}

		public static final LogicFunction EXCHANGE = (script, world, pos) -> script.stack.swap();
		public static final LogicFunction DUPLICATE = (script, world, pos) -> script.stack.duplicate();
		public static final LogicFunction NOT = (script, world, pos) -> put(script, get(script, world, pos) == 0);
		public static final LogicFunction INVERT = (script, world, pos) -> put(script, -get(script, world, pos));
		public static final LogicFunction RECIPROCAL = (script, world, pos) -> put(script, 1.0 / get(script, world, pos));
		public static final LogicFunction ADD = (script, world, pos) -> put(script, get(script, world, pos) + get(script, world, pos));
		public static final LogicFunction MULTIPLY = (script, world, pos) -> put(script, get(script, world, pos) * get(script, world, pos));
		public static final LogicFunction EQUALS = (script, world, pos) -> put(script, script.pull(world, pos).equals(script.pull(world, pos)));
		public static final LogicFunction LESS = (script, world, pos) -> put(script, script.pull(world, pos).lessThan(script.pull(world, pos)));
		public static final LogicFunction MORE = (script, world, pos) -> put(script, script.pull(world, pos).moreThan(script.pull(world, pos)));
		public static final LogicFunction INCREMENT = (script, world, pos) -> put(script, get(script, world, pos) + 1);
		public static final LogicFunction DECREMENT = (script, world, pos) -> put(script, get(script, world, pos) - 1);
		public static final LogicFunction SIN = (script, world, pos) -> put(script, Math.sin(get(script, world, pos)));
		public static final LogicFunction RANDOM = (script, world, pos) -> put(script, world.random.nextDouble());
		public static final LogicFunction ASCEND = (script, world, pos) -> script.ascend();
		public static final LogicFunction ROTATE = (script, world, pos) -> script.stack.rotate();
		public static final LogicFunction YEET = (script, world, pos) -> script.stack.pull().drop(world, pos);
		public static final LogicFunction OVER = (script, world, pos) -> script.stack.over();

		public static final LogicFunction VALUE = (script, world, pos) -> {
			try {
				put(script, script.stack.peek(0).value());
			}catch (IndexOutOfBoundsException exception) {
				throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_COUNT);
			}
		};

	}

}
