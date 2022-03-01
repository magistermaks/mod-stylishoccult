package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.duck.LivingEntityDuck;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.script.elements.FluidElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.sounds.SoundManager;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RandUtils;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class SuspiciousRuneBlock extends TransferRuneBlock {

	public SuspiciousRuneBlock(String name) {
		super(name);
	}

	private LivingTarget getTarget(World world, BlockPos pos) {
		double x = pos.getX(), y = pos.getY(), z = pos.getZ();
		Box box = new Box(x - 6, y - 6, z - 6, x + 6, y + 6, z + 6);

		LivingTarget[] candidates = world.getEntitiesByClass(LivingEntity.class, box, entity ->
				(entity.getGroup() != EntityGroup.UNDEAD)
						&& entity.isAttackable() && entity.isAlive() && !entity.isSpectator()
						&& ((entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative()) || entity instanceof MobEntity)
		).stream().map(entity -> new LivingTarget(entity, pos)).filter(LivingTarget::verify).toArray(LivingTarget[]::new);

		if(candidates.length != 0) {
			return RandUtils.getArrayEntry(candidates, world.random);
		}

		return null;
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		LivingTarget target = getTarget(world, pos);

		float damage = 0;
		if (target != null) {

			if (target.entity instanceof LivingEntityDuck duck) {
				damage = target.getDamage();
				damage = duck.stylish_applyShock(world.getTime(), damage);

				if (damage > 0 && world instanceof ServerWorld server) {
					Network.ATTACK.send(target.source, target.entity, target.face, server);
					SoundManager.playSound(server, pos, "spark");
				}
			}
		}

		script.stack.push(new FluidElement(FluidVariant.of(ModBlocks.STILL_BLOOD), Math.round(damage)));
	}

	public static class LivingTarget {

		public final LivingEntity entity;
		public final Direction face;
		public final BlockPos source;
		public final float distance;

		public LivingTarget(LivingEntity entity, BlockPos pos) {
			this.entity = entity;
			this.face = closestFace(entity, pos, entity.world);
			this.source = pos;
			this.distance = (float) entity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ());
		}

		private Direction closestFace(LivingEntity entity, BlockPos pos, World world) {
			Direction closest = null;
			double distance = 128;

			for (Direction direction : Direction.values()) {
				BlockPos target = pos.offset(direction);

				if (!world.getBlockState(target).getCollisionShape(world, target).isEmpty()) {
					continue;
				}

				double dist = entity.squaredDistanceTo(Vec3d.ofCenter(target));

				if (dist < distance) {
					distance = dist;
					closest = direction;
				}
			}

			return closest;
		}

		/**
		 * Check if there is a clean line of attack from source
		 */
		public boolean verify() {

			// no valid face to attack this entity or too far away from source
			if (this.face == null || this.distance >= 36) {
				return false;
			}

			Vec3d from = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
			Vec3d to = addFaceOffset(Vec3d.ofCenter(source));
			BlockHitResult result = entity.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
			return result.getType() == HitResult.Type.MISS || source.equals(result.getBlockPos());
		}

		/**
		 * Get random damage value, based on distance and entity armor
		 */
		public float getDamage() {
			float f = Math.min(1.5f - MathHelper.sqrt(this.distance) / 6f, 1f);
			return f * ((entity.world.random.nextFloat() + RandUtils.rangeInt(1, 5)) / (OccultHelper.getBoneArmor(entity) + 1));
		}

		/**
		 * Offset given vector by the 'face offset' and return new value
		 */
		public Vec3d addFaceOffset(Vec3d pos) {
			return pos.add(face.getOffsetX() * 0.5f, face.getOffsetY() * 0.5f, face.getOffsetZ() * 0.5f);
		}

	}

}