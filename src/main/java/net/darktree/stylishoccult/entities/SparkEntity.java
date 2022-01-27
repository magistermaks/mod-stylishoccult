package net.darktree.stylishoccult.entities;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.entities.goal.FollowCorruptedGoal;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class SparkEntity extends HostileEntity {

    private BlockPos wanderTargetPoint;
    private int maxAge;
    private Vec3d direction;

    public SparkEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);

        this.experiencePoints = 1;
        this.direction = new Vec3d(0, 0, 0);
    }

    public void setVentDirection( Direction direction, float power ) {
        this.direction = new Vec3d( direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ() ).multiply(power);
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_CAMPFIRE_CRACKLE;
    }

    @Override
    protected float getSoundVolume() {
        return 0.01f;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH;
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.age = tag.getInt("age");
        this.maxAge = tag.getInt("maxAge");
        double x = tag.getDouble("dirX");
        double y = tag.getDouble("dirY");
        double z = tag.getDouble("dirZ");
        direction = new Vec3d(x, y, z);
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("age", this.age);
        tag.putInt("maxAge", this.maxAge);
        tag.putDouble("dirX", direction.x);
        tag.putDouble("dirY", direction.y);
        tag.putDouble("dirZ", direction.z);
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, true));

        addSpecificGoals();
    }

    protected void addSpecificGoals() {
        this.targetSelector.add(0, new FollowCorruptedGoal(this, true));
    }

    public static DefaultAttributeContainer.Builder createSparkAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, StylishOccult.SETTINGS.sparkEntityHealth)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, StylishOccult.SETTINGS.sparkEntityDamage);
    }

    protected void playRandomEffect(int c, boolean die) {
        for(int i = 0; i < c; i ++) {
            world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0, 0.03f, 0);
        }
    }

    public void tick() {
        if( this.getFireTicks() > 0 ) {
            this.setFireTicks(-1);
        }

        if(random.nextInt(40) == 0) {
            playRandomEffect(random.nextInt(4) + 1, false);
        }

        super.tick();
        this.setVelocity(this.getVelocity().multiply(1.0D, 0.6D, 1.0D));

        if (this.age > maxAge) {
            this.damage(DamageSource.STARVE, 1.0F);
        }

        if (this.getHealth() <= 0) {
            playRandomEffect(random.nextInt(3) + 3, true);
            this.remove();
        }
    }

    protected void dealDamage() {
        damage(DamageSource.ON_FIRE, StylishOccult.SETTINGS.sparkEntityDamage);
    }

    protected void mobTick() {
        super.mobTick();

        direction = direction.multiply( 0.9 );
        LivingEntity target = this.getTarget();
        double d, e, f, s;

        if (target != null) {
            if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                if (this.tryAttack(target)) {
                    dealDamage();
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

            d = (double) wanderTargetPoint.getX() + 0.5 - this.getX() + this.random.nextFloat() - this.random.nextFloat();
            e = (double) wanderTargetPoint.getY() + 0.5 - this.getY() + this.random.nextFloat() - this.random.nextFloat();
            f = (double) wanderTargetPoint.getZ() + 0.5 - this.getZ() + this.random.nextFloat() - this.random.nextFloat();
        }

        Vec3d vec3d = this.getVelocity().add(direction);
        Vec3d vec3d2 = vec3d.add((Math.signum(d) * 0.5 - vec3d.x) * 0.1, (Math.signum(e) * 0.7 - vec3d.y) * 0.1, (Math.signum(f) * 0.5 - vec3d.z) * 0.1);
        this.setVelocity(vec3d2.multiply(s));
        float g = (float) (MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.3) - 90;
        float h = MathHelper.wrapDegrees(g - this.yaw);
        this.yaw += h;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
        this.maxAge = getMaxAge( difficulty.getGlobalDifficulty() );
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    public int getMaxAge(Difficulty d) {
        return world.getRandom().nextInt(10 * 20) + (20 * StylishOccult.SETTINGS.sparkEntityBaseLiveTime.get(d));
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