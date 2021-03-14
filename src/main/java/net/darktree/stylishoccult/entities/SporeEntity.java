package net.darktree.stylishoccult.entities;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.entities.goal.FollowNonCorruptedGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
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

    protected void addSpecificGoals() {
        this.targetSelector.add(0, new FollowNonCorruptedGoal(this, true));
    }

    protected void dealDamage() {
        damage(DamageSource.GENERIC, StylishOccult.SETTINGS.sporeEntityDamage);
    }

    public SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_HONEY_BLOCK_BREAK;
    }

}
