package net.darktree.stylishoccult.mixin;

import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Direction.class)
public abstract class DirectionMixin {

	@Invoker("<init>")
	private static Direction init(String string, int ordinal, int id, int idOpposite, int idHorizontal, String name, Direction.AxisDirection direction, Direction.Axis axis, Vec3i vector) {
		throw new AssertionError(); // unreachable statement
	}

	static {
		// haha, the mixin police will get me for that sin
		Directions.UNDECIDED = init("UNDECIDED", 0, 0, 1, -1, "UNDECIDED", Direction.AxisDirection.NEGATIVE, Direction.Axis.Y, new Vec3i(0, 0, 0));
	}

}
