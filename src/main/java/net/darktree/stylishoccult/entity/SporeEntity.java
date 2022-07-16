package net.darktree.stylishoccult.entity;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.entity.goal.FollowLanternGoal;
import net.darktree.stylishoccult.entity.goal.FollowSparkGoal;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SporeEntity extends SparkEntity implements CorruptedEntity {

    Vec3d fixedTarget = null;

    public SporeEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createSporeAttributes() {
        return SparkEntity.createSparkAttributes();
    }

    @Override
    protected void playRandomEffect(int c, boolean die) {
        for(int i = 0; i < c; i ++) {
            world.addParticle(die ? Particles.BLOOD_SPLASH : Particles.BLOOD_FALLING, this.getX(), this.getY(), this.getZ(), 0, 0.03f, 0);
        }

        if(die) {
            Sounds.SPORE_ESCAPES.play(world, this.getBlockPos());
        }
    }

    @Override
    protected void addSpecificGoals() {
        this.targetSelector.add(1, new FollowSparkGoal(this, true));
        this.targetSelector.add(1, new FollowLanternGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void dealDamage() {
        damage(DamageSource.GENERIC, StylishOccult.SETTINGS.sporeEntityDamage);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_HONEY_BLOCK_BREAK;
    }

    public void setFixedTarget(Vec3d target) {
        fixedTarget = target;
    }

    @Override
    protected Box getTargetBoundingBox() {
        return fixedTarget != null ? Utils.box(0, 0, 0, 16, 16, 16).offset(fixedTarget) : super.getTargetBoundingBox();
    }

    @Override
    protected Vec3d getTargetPosition() {
        return fixedTarget != null ? fixedTarget : super.getTargetPosition();
    }

    @Override
    protected void tryAttackTarget() {
        if (fixedTarget != null) {
            dealDamage();
        }else{
            super.tryAttackTarget();
        }
    }

    protected boolean hasTarget() {
        return fixedTarget != null || super.hasTarget();
    }

}
