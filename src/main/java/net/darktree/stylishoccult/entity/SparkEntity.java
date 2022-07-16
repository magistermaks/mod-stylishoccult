package net.darktree.stylishoccult.entity;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.entity.goal.FollowSporeGoal;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
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

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_CAMPFIRE_CRACKLE;
    }

    @Override
    protected float getSoundVolume() {
        return 0.01f;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.age = tag.getInt("age");
        this.maxAge = tag.getInt("maxAge");
        double x = tag.getDouble("dirX");
        double y = tag.getDouble("dirY");
        double z = tag.getDouble("dirZ");
        direction = new Vec3d(x, y, z);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("age", this.age);
        tag.putInt("maxAge", this.maxAge);
        tag.putDouble("dirX", direction.x);
        tag.putDouble("dirY", direction.y);
        tag.putDouble("dirZ", direction.z);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // noop
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));

        addSpecificGoals();
    }

    protected void addSpecificGoals() {
        this.targetSelector.add(0, new FollowSporeGoal(this, true));
    }

    public static DefaultAttributeContainer.Builder createSparkAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, StylishOccult.SETTINGS.entityHealth)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, StylishOccult.SETTINGS.entityDamage);
    }

    protected void playRandomEffect(int c, boolean die) {
        for(int i = 0; i < c; i ++) {
            world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0, 0.03f, 0);
        }

        if(die) {
            world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.HOSTILE, 0.1f, 1.1f);
        }
    }

    @Override
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
            playRandomEffect(random.nextInt(4) + 3, true);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    protected void dealDamage() {
        damage(DamageSource.ON_FIRE, StylishOccult.SETTINGS.entityDamage);
    }

    protected boolean hasTarget() {
        return this.getTarget() != null;
    }

    protected Vec3d getTargetPosition() {
        return this.getTarget().getPos();
    }

    protected Box getTargetBoundingBox() {
        return this.getTarget().getBoundingBox();
    }

    protected void tryAttackTarget() {
        LivingEntity entity = getTarget();

        if(random.nextInt(OccultHelper.getBoneArmor(entity)) == 0) {
            if (this.tryAttack(entity)) {
                dealDamage();
            }
        }else{
            entity.damage(DamageSource.mob(this), 0);
            dealDamage();
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        direction = direction.multiply( 0.9 );
        double d, e, f, s;

        if (hasTarget()) {
            Vec3d target = this.getTargetPosition();
            if (this.getBoundingBox().intersects(getTargetBoundingBox())) {
                tryAttackTarget();
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
        float h = MathHelper.wrapDegrees(g - this.headYaw);
        this.headYaw += h;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag) {
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

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    public boolean canAvoidTraps() {
        return true;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height / 2.0F;
    }
}