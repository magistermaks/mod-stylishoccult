package net.darktree.stylishoccult.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockUtils {

	public interface BlockStateComparator {
		boolean call(BlockState state);
	}

	public static <T extends BlockEntity, Y extends BlockView> T get(Class<T> clazz, Y world, BlockPos pos){
		BlockEntity entity = world.getBlockEntity(pos);

		if (clazz.isInstance(entity)) {
			return clazz.cast(entity);
		}

		throw new RuntimeException("Failed to fetch block entity at " + pos.toShortString());
	}

	public static boolean touchesAir(BlockView world, BlockPos origin) {
		for (Direction direction : Directions.ALL){
			BlockState state = world.getBlockState(origin.offset(direction));

			if (state.isAir()) {
				return true;
			}
		}

		return false;
	}

	public static int countInArea(World world, BlockPos origin, Class<?> clazz, int radius) {
		int count = 0;
		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int x = -radius; x <= radius; x ++){
			for (int y = -radius; y <= radius; y ++) {
				for (int z = -radius; z <= radius; z++) {
					pos.set(x + origin.getX(), y + origin.getY(), z + origin.getZ());

					if (clazz.isInstance(world.getBlockState(pos).getBlock())) {
						count ++;
					}
				}
			}
		}

		return count;
	}

	public static BlockPos find(World world, BlockPos origin, Block block, int radius, BlockUtils.BlockStateComparator comp) {
		for (int x = -radius; x <= radius; x ++){
			for (int y = -radius; y <= radius; y ++){
				for (int z = -radius; z <= radius; z ++){

					BlockPos pos = new BlockPos(x + origin.getX(), Math.max(y + origin.getY(), 0), z + origin.getZ());
					BlockState state = world.getBlockState(pos);

					if (state.getBlock() == block || (block == null)) {
						if (comp != null) {
							if (comp.call(state)){
								return pos;
							}
						}else{
							return pos;
						}
					}
				}
			}
		}

		return null;
	}

}
