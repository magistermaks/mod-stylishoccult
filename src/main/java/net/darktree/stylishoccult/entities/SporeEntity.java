package net.darktree.stylishoccult.entities;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.entities.goal.FollowSparkGoal;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class SporeEntity extends SparkEntity implements CorruptedEntity {

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
        this.targetSelector.add(0, new FollowSparkGoal(this, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
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
}
