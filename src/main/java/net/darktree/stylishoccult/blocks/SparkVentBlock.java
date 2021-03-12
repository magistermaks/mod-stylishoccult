package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.entities.SparkEntity;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.SoundManager;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.Random;

public class SparkVentBlock extends SimpleBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public SparkVentBlock() {
        super(RegUtil.settings( Material.STONE, BlockSoundGroup.NETHERRACK, 4.0F, 16.0F, false )
                .ticksRandomly()
                .requiresTool() );

        setDefaultState( getDefaultState().with( ACTIVE, false ) );
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add( ACTIVE );
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.SPARK_VENT;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        if( world.getBlockState(pos).get(ACTIVE) ){
            entity.setFireTicks( world.getRandom().nextInt(20 * 4) + 20 * 4 );
        }
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockPos up = pos.up();
        if ( world.getBlockState(up).isAir() ) {

            float d = ((float) pos.getX()) + 0.4f + random.nextFloat() / 5;
            float e = ((float) pos.getY()) + 1.0f;
            float f = ((float) pos.getZ()) + 0.4f + random.nextFloat() / 5;

            if (random.nextInt(10) == 0 && state.get(ACTIVE)) {
                world.addParticle(ParticleTypes.LAVA, d, e, f, 0.0D, -0.1D, 0.0D);
            }

            if(random.nextInt(5) == 0) {
                world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.1D, 0.0D);
            }

        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean source = world.getFluidState(pos.down()).getFluid() == Fluids.LAVA;

        if( state.get(ACTIVE) != source ) {
            world.setBlockState(pos, state.with(ACTIVE, source));
        }

        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    private void schedule( World world, BlockPos pos ) {
        int baseTime = StylishOccult.SETTINGS.sparkVentSleepTimeBase.get( world.getDifficulty() );
        world.getBlockTickScheduler().schedule(pos, this, baseTime + world.random.nextInt( 100 ));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( state.get(ACTIVE) ) {
            if( !world.getBlockTickScheduler().isScheduled(pos, this) ) {
                schedule( world, pos );
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( state.get(ACTIVE) ) {

            Difficulty d = world.getDifficulty();
            BlockPos up = pos.up();

            if( d != Difficulty.PEACEFUL && world.getBlockState(up).isAir() ) {

                int count = world.random.nextInt( 8 );

                for( int i = 0; i < count; i ++ ) {

                    SparkEntity sparkEntity = ModEntities.LAVA_SPARK.create(world);

                    if( sparkEntity == null ){
                        throw new RuntimeException( "Unable to summon Spark!" );
                    }

                    sparkEntity.setVentDirection( Direction.UP, 0.5f );
                    sparkEntity.refreshPositionAndAngles(up, 0.0F, 0.0F);
                    sparkEntity.initialize(world, world.getLocalDifficulty(up), SpawnReason.REINFORCEMENT, null, null);
                    world.spawnEntity(sparkEntity);

                    float x = pos.getX() + 0.4f + random.nextFloat() / 5;
                    float y = pos.getY() + 1.0f;
                    float z = pos.getZ() + 0.4f + random.nextFloat() / 5;
                    world.spawnParticles( ParticleTypes.LAVA, x, y, z, 2, 0, -0.05, 0, 0 );

                }

                if( count > 0 ) {
                    SoundManager.playSound(world, pos, "vent");
                }

            }

            schedule( world, pos );
        }
    }
}
