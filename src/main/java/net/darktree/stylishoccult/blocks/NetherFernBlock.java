package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.block.Block;
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

public class NetherFernBlock extends NetherGrassBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    protected NetherFernBlock(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if( entity instanceof LivingEntity ) {
            if( !world.isClient && !((LivingEntity) entity).hasStatusEffect(StatusEffects.POISON) ) {
                int duration = StylishOccult.SETTINGS.fernPoisonTimeMinBase + world.random.nextInt( StylishOccult.SETTINGS.fernPoisonTimeDelta.get(world) );
                ((LivingEntity) entity).addStatusEffect( new StatusEffectInstance(StatusEffects.POISON, duration, 0) );
            }
        }
    }

}
