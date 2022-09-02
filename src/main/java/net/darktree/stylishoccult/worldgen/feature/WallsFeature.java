package net.darktree.stylishoccult.worldgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.darktree.lootboxes.LootBoxes;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.tag.ModTags;
import net.darktree.stylishoccult.utils.Directions;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.SimpleFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.placementmodifier.CountMultilayerPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WallsFeature extends SimpleFeature<DefaultFeatureConfig> {

	public WallsFeature(Codec<DefaultFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		BlockPos target = pos.down();

		if (RandUtils.nextBool(StylishOccult.SETTING.wall_chance, random) && world.getBlockState(target).isSolidBlock(world, target)) {
			generateWall(getAxis(random), world, target, RandUtils.nextInt(2, 5, random), (float) RandUtils.nextInt(83, 90, random), random);
			decorate(world, target, random);
			this.debugWrite(target);
		}

		return false;
	}

	private boolean generateColumn( StructureWorldAccess world, BlockPos origin, int height, Random random ) {

		if (generateFoundation(world, origin, height, random)) {
			BlockPos.Mutable pos = origin.mutableCopy();

			for (int i = 0; i <= height; i ++) {
				pos.move(Direction.UP);
				generateRune(world, pos, random);
			}

			return true;
		}

		return false;
	}

	private void generateWall( Direction.Axis axis, StructureWorldAccess world, BlockPos pos, int height, float chance, Random random ) {
		generateColumn(world, pos, height, random);

		Direction.Axis child = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
		boolean hasChild = true;

		int ah = height, ad = 0;
		while (RandUtils.nextBool(chance, random)) {
			ah += random.nextInt(2) - 2;
			ad --;

			if (hasChild && RandUtils.nextBool(25.0f, random)) {
				generateWall(child, world, pos.offset(axis, ad), ah + 1, chance, random );
				hasChild = false;
			}

			if (!generateColumn(world, pos.offset(axis, ad), ah, random)) break;
		}

		int bh = height, bd = 0;
		while (RandUtils.nextBool(chance, random)) {
			bh += random.nextInt(2) - 2;
			bd ++;

			if (hasChild && RandUtils.nextBool(25.0f, random)) {
				generateWall(child, world, pos.offset(axis, bd), bh + 1, chance, random );
				hasChild = false;
			}

			if (!generateColumn(world, pos.offset(axis, bd), bh, random)) break;
		}
	}

	private boolean generateFoundation(StructureWorldAccess world, BlockPos origin, int height, Random random) {
		BlockPos.Mutable pos = origin.mutableCopy();

		for (int i = 0; i <= height; i ++) {

			if (world.getBlockState(pos).isSolidBlock(world, pos)) {
				return true;
			} else {
				generateRune(world, pos, random);
			}

			pos.move(Direction.DOWN);
		}

		return false;
	}

	private void decorate(StructureWorldAccess world, BlockPos target, Random random) {
		final int minX = -5;
		final int minY = -4;
		final int minZ = -5;
		final int maxX =  5;
		final int maxY =  4;
		final int maxZ =  5;

		ArrayList<BlockPos> qualified = new ArrayList<>();
		int cx = target.getX(), cy = target.getY(), cz = target.getZ();
		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int x = minX; x <= maxX; x ++) {
			for (int z = minZ; z <= maxZ; z ++) {
				for (int y = minY; y <= maxY; y ++) {
					pos.set(cx + x, cy + y, cz + z);

					if (world.getBlockState(pos).isSolidBlock(world, pos)) {
						pos.move(0, 1, 0);

						if (world.getBlockState(pos).isAir() && touchesRunes(world, pos)) {
							qualified.add(pos.toImmutable());
						}
					}
				}
			}
		}

		// pick random candidates
		Collections.shuffle(qualified, random);
		int slots = RandUtils.nextInt(0, 5, random);

		for (BlockPos position : qualified) {
			if (slots > 0) {
				placeRandomDecoration(world, position, random, cy);
				slots --;
			} else {
				break;
			}
		}
	}

	private void placeRandomDecoration(StructureWorldAccess world, BlockPos pos, Random random, int base) {
		int height = pos.getY() - base;

		if (height > 2 || random.nextInt(2) == 0) {
			placeBlock(world, pos, Blocks.CANDLE.getDefaultState()
					.with(CandleBlock.CANDLES, random.nextInt(4) + 1)
					.with(CandleBlock.LIT, random.nextInt(8) != 0));
		} else {
			placeBlock(world, pos, LootBoxes.URN_BLOCK.getDefaultState());
		}
	}

	private static boolean touchesRunes(BlockView world, BlockPos origin) {
		for (Direction direction : Directions.HORIZONTAL) {
			if (world.getBlockState(origin.offset(direction)).getBlock() instanceof RuneBlock) {
				return true;
			}
		}

		return false;
	}

	private void generateRune(StructureWorldAccess world, BlockPos pos, Random random) {
		if (RandUtils.nextBool(StylishOccult.SETTING.wall_rune_chance, random)) {
			BlockState state = RandUtils.pickFromTag(ModTags.RUNES, random, Blocks.AIR).getDefaultState().with(RuneBlock.FROZEN, true);
			placeBlock(world, pos, state);
		} else {
			placeBlock(world, pos, RandUtils.pickFromTag(ModTags.RUNIC_WALL, random, Blocks.AIR));
		}
	}

	private Direction.Axis getAxis(Random random) {
		return random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
	}

	@Override
	public ConfiguredFeature<?, ?> configure() {
		return new ConfiguredFeature<>(this, new DefaultFeatureConfig());
	}

	@Override
	public List<PlacementModifier> modifiers() {
		return ImmutableList.of(
				CountMultilayerPlacementModifier.of(1)
		);
	}

}
