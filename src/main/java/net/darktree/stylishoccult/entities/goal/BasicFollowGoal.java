package net.darktree.stylishoccult.entities.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;

public abstract class BasicFollowGoal extends TrackTargetGoal {

    protected final int reciprocalChance;
    protected LivingEntity targetEntity;
    protected TargetPredicate targetPredicate;

    public BasicFollowGoal(MobEntity mob, boolean checkVisibility, boolean checkNavigable, int reciprocalChance) {
        super(mob, checkVisibility, checkNavigable);
        this.reciprocalChance = reciprocalChance;
    }

    @Override
    public boolean canStart() {
        if( mob.getRandom().nextInt(this.reciprocalChance) != 0 ) {
            return false;
        } else {
            this.findClosestTarget();
            return this.targetEntity != null;
        }
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0D, distance);
    }

    protected void findClosestTarget() {
        targetEntity = mob.world.getClosestEntity(LivingEntity.class, targetPredicate, mob, mob.getX(), mob.getEyeY(), mob.getZ(), getSearchBox(getFollowRange()));
    }

    @Override
    public void start() {
        mob.setTarget(targetEntity);
        super.start();
    }

}
