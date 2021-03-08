package net.darktree.stylishoccult.entities;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.particles.Particles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class SparkEntity extends HostileEntity {

    private BlockPos wanderTargetPoint;
    private int maxAge;

    public SparkEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);

        this.experiencePoints = 1;
    }

    protected float getSoundVolume() {
        return 0.05F;
    }

    protected float getSoundPitch() {
        return super.getSoundPitch();
    }

    public SoundEvent getAmbientSound() {
        return this.random.nextInt(4) == 0 ? null : SoundEvents.BLOCK_CAMPFIRE_CRACKLE;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH;
    }

    public boolean isPushable() {
        return false;
    }

    protected void pushAway(Entity entity) {
    }

    protected void tickCramming() {
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.age = tag.getInt("age");
        this.maxAge = tag.getInt("maxAge");
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("age", this.age);
        tag.putInt("maxAge", this.maxAge);
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(0, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createSparkAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, StylishOccult.SETTINGS.sparkEntityHealth)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, StylishOccult.SETTINGS.sparkEntityDamage);
    }

    public void tick() {
        if( this.getFireTicks() > 0 ) {
            this.setFireTicks(-1);
        }

        super.tick();
        this.setVelocity(this.getVelocity().multiply(1.0D, 0.6D, 1.0D));

        if (this.age > maxAge) {
            this.damage(DamageSource.STARVE, 1.0F);
        }
    }

    protected void mobTick() {
        super.mobTick();

        LivingEntity target = this.getTarget();
        double d, e, f, s;

        if (target != null) {
            if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                if (this.tryAttack(target)) {
                    this.damage(DamageSource.STARVE, 4.0F);
                }
            }

            s = 0.82F + this.random.nextFloat() / 10;
            this.wanderTargetPoint = null;

            d = target.getX() + 0.0D - this.getX() + this.random.nextFloat() - this.random.nextFloat();
            e = target.getY() + 0.5D - this.getY() + this.random.nextFloat() - this.random.nextFloat();
            f = target.getZ() + 0.0D - this.getZ() + this.random.nextFloat() - this.random.nextFloat();
        } else {
            s = 0.7F;
            if (this.wanderTargetPoint != null && (!this.world.isAir(this.wanderTargetPoint) || this.wanderTargetPoint.getY() < 1)) {
                this.wanderTargetPoint = null;
            }

            if (this.wanderTargetPoint == null || this.random.nextInt(30) == 0 || this.wanderTargetPoint.isWithinDistance(this.getPos(), 1.0D)) {
                this.wanderTargetPoint = new BlockPos(this.getX() + (double) this.random.nextInt(7) - (double) this.random.nextInt(7), this.getY() + (double) this.random.nextInt(6) - 2.0D, this.getZ() + (double) this.random.nextInt(7) - (double) this.random.nextInt(7));
            }

            d = (double) wanderTargetPoint.getX() + 0.5D - this.getX() + this.random.nextFloat() - this.random.nextFloat();
            e = (double) wanderTargetPoint.getY() + 0.5D - this.getY() + this.random.nextFloat() - this.random.nextFloat();
            f = (double) wanderTargetPoint.getZ() + 0.5D - this.getZ() + this.random.nextFloat() - this.random.nextFloat();
        }

        Vec3d vec3d = this.getVelocity();
        Vec3d vec3d2 = vec3d.add((Math.signum(d) * 0.5D - vec3d.x) * 0.10000000149011612D, (Math.signum(e) * 0.699999988079071D - vec3d.y) * 0.10000000149011612D, (Math.signum(f) * 0.5D - vec3d.z) * 0.10000000149011612D);
        this.setVelocity(vec3d2.multiply(s));
        float g = (float) (MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.2957763671875D) - 90.0F;
        float h = MathHelper.wrapDegrees(g - this.yaw);
        this.yaw += h;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
        Difficulty d = difficulty.getGlobalDifficulty();
        this.maxAge = world.getRandom().nextInt(10 * 20) + (20 * StylishOccult.SETTINGS.sparkEntityBaseLiveTime.get(d));
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    protected boolean canClimb() {
        return false;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    public boolean canAvoidTraps() {
        return true;
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            return super.damage(source, amount);
        }
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height / 2.0F;
    }
}