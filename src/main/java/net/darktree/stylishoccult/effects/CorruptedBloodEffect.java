package net.darktree.stylishoccult.effects;

import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public class CorruptedBloodEffect extends SimpleStatusEffect {

    protected CorruptedBloodEffect() {
        super(StatusEffectType.HARMFUL, false, 0x801a00);
    }

    public void onUpdate(LivingEntity entity, int amplifier) {
        if( !entity.world.isClient && amplifier > 0 ) {
            entity.damage(DamageSource.MAGIC, amplifier / 3.0f);
        }
    }

    private void applyToEntity( LivingEntity entity, int infectionAmplifier ) {
        if( entity.world.random.nextInt(20) == 0 ) {
            if( !entity.hasStatusEffect( this ) ) {
                entity.applyStatusEffect( new CorruptedBloodEffectInstance(
                        this,
                        500 + entity.world.random.nextInt(200),
                        infectionAmplifier)
                );
            }
        }
    }

    @Override
    public void instanceUpdate(LivingEntity entity, StatusEffectInstance instance) {
        if( !entity.world.isClient && getStoredDuration(instance) > 0 ) {

            if( instance instanceof CorruptedBloodEffectInstance ) {
                CorruptedBloodEffectInstance inst = (CorruptedBloodEffectInstance) instance;

                if( entity.world.random.nextInt(20) == 0 ) {
                    BlockPos pos = entity.getBlockPos();
                    float r = 4 + instance.getAmplifier() / 4.0f;

                    Box box = new Box(
                            pos.getX() - r,
                            pos.getY() - r + 1,
                            pos.getZ() - r,
                            pos.getX() + r,
                            pos.getY() + r - 1,
                            pos.getZ() + r
                    );

                    List<LivingEntity> list = entity.world.getNonSpectatingEntities(LivingEntity.class, box);
                    int infectionAmplifier = inst.getInfectionAmplifier();

                    for (LivingEntity livingEntity : list) {
                        applyToEntity(livingEntity, infectionAmplifier);
                    }
                }

            }else{
                StylishOccult.LOGGER.error( "CorruptedBloodEffectInstance object expected!" );
            }

        }
    }

    public void instanceOnAdded(LivingEntity entity, StatusEffectInstance instance) {
        if( instance instanceof CorruptedBloodEffectInstance ) {
            CorruptedBloodEffectInstance inst = (CorruptedBloodEffectInstance) instance;
            int amplifier = Math.max(0, instance.getAmplifier());
            inst.setInfectionAmplifier( amplifier );
        }else{
            StylishOccult.LOGGER.error( "CorruptedBloodEffectInstance object expected!" );
        }
    }

}
