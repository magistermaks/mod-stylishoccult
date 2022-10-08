package net.darktree.stylishoccult.block.entity.demon;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.property.LavaDemonPart;
import net.darktree.stylishoccult.entity.ModEntities;
import net.darktree.stylishoccult.entity.SparkEntity;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.Random;

public class LavaDemonBlockEntity extends BlockEntity {

	private int timeout = 50;
	private int interval = 0;
	private int amount = 4;
	private int spark = 0;
	private static final TargetPredicate PLAYER_PREDICATE = new TargetPredicate(true).setBaseMaxDistance(12.0);

	public LavaDemonBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.LAVA_DEMON, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if (world == null || world.isClient) {
			return;
		}

		if (timeout >= 0) {
			timeout -= 1;
		}

		if (interval >= 0) {
			interval -= 1;
		}

		if (spark >= 0) {
			spark -= 1;
		}

		Difficulty d = world.getDifficulty();
		Random random = world.getRandom();
		int anger = state.get(LavaDemonBlock.ANGER);
		LavaDemonPart part = state.get(LavaDemonBlock.PART);
		PlayerEntity player = getNearbyPlayer();

		// Spread anger
		if (anger == 2 && RandUtils.nextBool(StylishOccult.SETTING.spread_anger_chance.get(d), random)) {
			BlockPos target = pos.offset(RandUtils.pickFromEnum(Direction.class, random));
			BlockState targetState = world.getBlockState(target);

			if (targetState.getBlock() == ModBlocks.LAVA_DEMON) {
				if(targetState.get(LavaDemonBlock.ANGER) == 0) {
					world.setBlockState(target, targetState.with(LavaDemonBlock.ANGER, 2));
					Sounds.LAVA_DEMON_WAKEUP.play(world, pos);
				}
			}
		}

		// check if attack is needed
		if (d == Difficulty.PEACEFUL || anger == 0 || player == null) {
			return;
		}

		// shoot fireballs
		if (timeout < 1 && interval < 1 && part == LavaDemonPart.HEAD && !player.isCreative()) {
			if (areInLine(player.getBlockPos(), pos) || areInLine(player.getBlockPos().up(), pos)) {

				if (amount <= 0) {
					timeout = (random.nextInt(60) + StylishOccult.SETTING.fire_ball_timeout_min);
					amount = (random.nextInt(6) + StylishOccult.SETTING.fire_ball_amount_min);
				}

				interval = 3;

				Direction direction = getOffsetDirection(pos, player.getBlockPos());
				if (direction != Direction.DOWN && direction != Direction.UP) {
					BlockPos spawnPoint = pos.offset(direction);

					float speed = StylishOccult.SETTING.fire_ball_speed;
					float vx = direction == Direction.WEST ? -speed : direction == Direction.EAST ? speed : 0;
					float vz = direction == Direction.NORTH ? -speed : direction == Direction.SOUTH ? speed : 0;

					world.playSound( null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 0.5f * random.nextFloat(),  random.nextFloat() * 0.7F + 0.3F);
					amount -= 1;

					SmallFireballEntity fireball = new SmallFireballEntity(
							world,
							spawnPoint.getX() + 0.5,
							spawnPoint.getY() + 0.5,
							spawnPoint.getZ() + 0.5,
							vx + (random.nextFloat() / 15) - (random.nextFloat() / 15),
							0,
							vz + (random.nextFloat() / 15) - (random.nextFloat() / 15));

					world.spawnEntity(fireball);

				}
			}
		}

		// summon spark
		if (part != LavaDemonPart.BODY) {
			spawnSmoke(random, Math.max(3 - spark / 20, 0));

			if (spark <= 0) {
				spark = RandUtils.nextInt(StylishOccult.SETTING.spark_spawn_time_min, StylishOccult.SETTING.spark_spawn_time_max, random);

				if (BlockUtils.touchesAir(world, pos)) {

					for (int i = 0; i < StylishOccult.SETTING.spark_spawn_attempts; i++) {
						Direction dir = RandUtils.pickFromEnum(Direction.class, random);
						BlockPos targetPos = pos.offset(dir);
						if (world.getBlockState(targetPos).isAir()) {

							if (part == LavaDemonPart.HEAD && (dir == Direction.DOWN || dir == Direction.UP)) {
								continue;
							}

							SparkEntity sparkEntity = ModEntities.SPARK.create(world);

							if (sparkEntity == null) {
								throw new RuntimeException("Unable to summon Spark!");
							}

							sparkEntity.refreshPositionAndAngles(targetPos, 0.0F, 0.0F);
							sparkEntity.initialize(((ServerWorld) world), world.getLocalDifficulty(targetPos), SpawnReason.REINFORCEMENT, null, null);
							world.spawnEntity(sparkEntity);

							break;
						}
					}
				}
			}
		}

	}

	private PlayerEntity getNearbyPlayer() {
		return world == null ? null : world.getClosestPlayer(PLAYER_PREDICATE, this.pos.getX(), this.pos.getY(), this.pos.getZ());
	}

	private Direction getOffsetDirection(BlockPos origin, BlockPos target) {
		if (target.getX() < origin.getX()) return Direction.from(Direction.Axis.X, Direction.AxisDirection.NEGATIVE);
		else if (target.getX() > origin.getX()) return Direction.from(Direction.Axis.X, Direction.AxisDirection.POSITIVE);
		if (target.getZ() < origin.getZ()) return Direction.from(Direction.Axis.Z, Direction.AxisDirection.NEGATIVE);
		else if (target.getZ() > origin.getZ()) return Direction.from(Direction.Axis.Z, Direction.AxisDirection.POSITIVE);
		if (target.getY() < origin.getY()) return Direction.from(Direction.Axis.Y, Direction.AxisDirection.NEGATIVE);
		return Direction.from(Direction.Axis.Y, Direction.AxisDirection.POSITIVE);
	}

	private boolean areInLine(BlockPos pos1, BlockPos pos2) {
		int x = (pos1.getX() - pos2.getX() != 0) ? 1 : 0;
		int y = (pos1.getY() - pos2.getY() != 0) ? 1 : 0;
		int z = (pos1.getZ() - pos2.getZ() != 0) ? 1 : 0;

		return (x + y + z) == 1;
	}

	private void spawnSmoke(Random random, int count) {
		assert world != null;

		if (!StylishOccult.SETTING.warning_particles) {
			return;
		}

		for (int i = 0; i < count; i ++) {
			Direction direction = RandUtils.pickFromEnum(Direction.class, random);
			BlockPos target = pos.offset(direction);

			if (!world.getBlockState(target).isOpaqueFullCube(world, target)) {
				Direction.Axis axis = direction.getAxis();
				double x = axis == Direction.Axis.X ? 0.5 + 0.5625 * direction.getOffsetX() : random.nextFloat();
				double y = axis == Direction.Axis.Y ? 0.5 + 0.5625 * direction.getOffsetY() : random.nextFloat();
				double z = axis == Direction.Axis.Z ? 0.5 + 0.5625 * direction.getOffsetZ() : random.nextFloat();

				Particles.spawn(world, ParticleTypes.SMOKE, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 1);
			}
		}
	}

}