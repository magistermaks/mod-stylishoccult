package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.RunicScript;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.components.RuneType;

import java.util.Random;

public class LogicRuneBlock extends RuneBlock {

    interface LogicFunction {
        void apply(RunicScript script);
    }

    private final LogicFunction function;

    public LogicRuneBlock(String name, LogicFunction function ) {
        super(RuneType.LOGIC, name);
        this.function = function;
    }

    @Override
    public void apply(RunicScript script) {
        try {
            function.apply(script);
        }catch(Exception exception) {
            throw RuneExceptionType.INVALID_ARGUMENT.get();
        }
    }

    public static class Functions {

        private static final Random RNG = new Random();

        // TODO: add sqrt

        public static final LogicFunction PUSH = script -> script.getStack().put( script.value );
        public static final LogicFunction PULL = script -> script.value = script.getStack().pull();
        public static final LogicFunction EXCHANGE = script -> script.getStack().exchange();
        public static final LogicFunction DUPLICATE = script -> script.getStack().duplicate();
        public static final LogicFunction OR = script -> script.getStack().or();
        public static final LogicFunction NOT = script -> script.getStack().not();
        public static final LogicFunction INVERT = script -> script.getStack().invert();
        public static final LogicFunction RECIPROCAL = script -> script.getStack().reciprocal();
        public static final LogicFunction ADD = script -> script.getStack().add(script.value);
        public static final LogicFunction MULTIPLY = script -> script.getStack().multiply(script.value);
        public static final LogicFunction EQUALS = script -> script.getStack().equal(script.value);
        public static final LogicFunction LESS = script -> script.getStack().less(script.value);
        public static final LogicFunction MORE = script -> script.getStack().more(script.value);
        //public static final LogicFunction LESS_OR_EQUAL = script -> script.getStack().lessOrEqual(script.value);
        //public static final LogicFunction MORE_OR_EQUAL = script -> script.getStack().moreOrEqual(script.value);
        //public static final LogicFunction NEGATIVE = script -> script.getStack().less(0);
        //public static final LogicFunction POSITIVE = script -> script.getStack().more(0);
        public static final LogicFunction INCREMENT = script -> script.getStack().increment();
        public static final LogicFunction DECREMENT = script -> script.getStack().decrement();
        public static final LogicFunction SINE = script -> script.getStack().sine();
        public static final LogicFunction RANDOM = script -> script.getStack().put(RNG.nextDouble());
        public static final LogicFunction POP = script -> script.getStack().pull();

    }

}
