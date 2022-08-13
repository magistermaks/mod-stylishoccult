package net.darktree.stylishoccult.block;

import net.darktree.interference.Voxels;
import net.darktree.interference.api.DropsItself;
import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NetherFernBlock extends NetherGrassBlock implements DropsItself {

    protected static final VoxelShape SHAPE = Voxels.shape(2, 0, 2, 14, 13, 14);

    protected NetherFernBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            if (!world.isClient && !((LivingEntity) entity).hasStatusEffect(StatusEffects.POISON)) {
                int duration = StylishOccult.SETTING.poison_time_min + world.random.nextInt(50);
                ((LivingEntity) entity).addStatusEffect( new StatusEffectInstance(StatusEffects.POISON, duration, 0) );
            }
        }
    }

}
