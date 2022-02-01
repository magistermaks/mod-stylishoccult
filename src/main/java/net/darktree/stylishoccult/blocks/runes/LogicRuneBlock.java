package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.elements.NumericElement;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.components.RuneType;
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
        try {
            function.apply(script, world, pos);
        }catch(Exception exception) {
            throw RuneExceptionType.INVALID_ARGUMENT.get();
        }
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

        public static final LogicFunction PUSH = (script, world, pos) -> script.stack.push(script.value.copy());
        public static final LogicFunction PULL = (script, world, pos) -> script.value = script.stack.pull(); // FIXME: that destroys stuff
        public static final LogicFunction EXCHANGE = (script, world, pos) -> script.stack.swap();
        public static final LogicFunction DUPLICATE = (script, world, pos) -> script.stack.duplicate();
        public static final LogicFunction NOT = (script, world, pos) -> put(script, get(script, world, pos) == 0);
        public static final LogicFunction INVERT = (script, world, pos) -> put(script, -get(script, world, pos)); // TODO: remove
        public static final LogicFunction RECIPROCAL = (script, world, pos) -> put(script, 1.0 / get(script, world, pos)); // TODO: remove
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
        public static final LogicFunction DROP = Script::pull;

    }

}
