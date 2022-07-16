package net.darktree.stylishoccult.block.entity.demon;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.LavaDemonBlock;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.property.LavaDemonPart;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.entities.SparkEntity;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.Random;

public class LavaDemonBlockEntity extends BlockEntity {

    private short timeout = 50;
    private short interval = 0;
    private short amount = 4;
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE = new TargetPredicate(true).setBaseMaxDistance(16.0D);

    public LavaDemonBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LAVA_DEMON, pos, state);
    }

    public PlayerEntity getNearbyPlayer() {
        return world == null ? null : world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    public static void tick(World world, BlockPos pos, BlockState state, LavaDemonBlockEntity entity) {
        if( world == null || world.isClient ) {
            return;
        }

        if( entity.timeout >= 0 ) {
            entity.timeout -= 1;
        }

        if( entity.interval >= 0 ) {
            entity.interval -= 1;
        }

        Difficulty d = world.getDifficulty();
        Random random = world.getRandom();
        int anger = state.get(LavaDemonBlock.ANGER);
        LavaDemonPart part = state.get(LavaDemonBlock.PART);
        PlayerEntity player = entity.getNearbyPlayer();

        // Spread anger
        if( anger == 2 && random.nextInt( StylishOccult.SETTINGS.lavaDemonSpreadAngerRarity.get(d) ) == 0 ) {
            BlockPos target = pos.offset( RandUtils.getEnum( Direction.class ) );
            BlockState targetState = world.getBlockState(target);

            if( targetState.getBlock() == ModBlocks.LAVA_DEMON ){
                if( targetState.get(LavaDemonBlock.ANGER) == 0 ) {
                    world.setBlockState(target, targetState.with(LavaDemonBlock.ANGER, 2));
                    Sounds.LAVA_DEMON_WAKEUP.play(world, pos);
                }
            }
        }

        // shoot fireballs
        if( d != Difficulty.PEACEFUL && entity.timeout < 1 && entity.interval < 1 && (anger > 0) && (part == LavaDemonPart.HEAD) && (player != null) && !player.isCreative() ) {
            if( BlockUtils.areInLine( player.getBlockPos(), pos ) || BlockUtils.areInLine( player.getBlockPos().up(), pos ) ) {

                if( entity.amount <= 0 ) {
                    entity.timeout = (short) (random.nextInt( 60 ) + StylishOccult.SETTINGS.lavaDemonFireBallTimeoutBase);
                    entity.amount = (short) (random.nextInt( 6 ) + StylishOccult.SETTINGS.lavaDemonFireBallAmountBase);
                }

                entity.interval = 3;

                Direction direction = BlockUtils.getOffsetDirection( pos, player.getBlockPos() );
                if( direction != Direction.DOWN && direction != Direction.UP ) {
                    BlockPos spawnPoint = pos.offset( direction );

                    float speed = StylishOccult.SETTINGS.lavaDemonFireBallSpeed;
                    float vx = direction == Direction.WEST ? -speed : direction == Direction.EAST ? speed : 0;
                    float vz = direction == Direction.NORTH ? -speed : direction == Direction.SOUTH ? speed : 0;

                    world.playSound( null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 0.5f * random.nextFloat(),  random.nextFloat() * 0.7F + 0.3F);
                    entity.amount -= 1;

                    SmallFireballEntity fireball = new SmallFireballEntity(
                            world,
                            spawnPoint.getX() + 0.5,
                            spawnPoint.getY() + 0.5,
                            spawnPoint.getZ() + 0.5,
                            vx + (random.nextFloat() / 15) - (random.nextFloat() / 15),
                            0,
                            vz + (random.nextFloat() / 15) - (random.nextFloat() / 15) );


                    world.spawnEntity( fireball );

                }
            }
        }

        // summon spark
        if( d != Difficulty.PEACEFUL && (random.nextInt( StylishOccult.SETTINGS.lavaDemonSparkSpawnRarity.get(d) ) == 0) && (anger > 0) && (part != LavaDemonPart.BODY) && (player != null) ) {
            if( BlockUtils.touchesAir(world, pos) ) {

                for( int i = 0; i < 10; i ++ ){
                    Direction  dir = RandUtils.getEnum( Direction.class );
                    BlockPos targetPos = pos.offset( dir );
                    if( world.getBlockState( targetPos ).isAir() ) {

                        if( part == LavaDemonPart.HEAD && (dir == Direction.DOWN || dir == Direction.UP) ) {
                            continue;
                        }

                        SparkEntity sparkEntity = ModEntities.SPARK.create(world);

                        if( sparkEntity == null ){
                            throw new RuntimeException( "Unable to summon Spark!" );
                        }

                        sparkEntity.refreshPositionAndAngles(targetPos, 0.0F, 0.0F);
                        sparkEntity.initialize(((ServerWorld) world), world.getLocalDifficulty(targetPos), SpawnReason.REINFORCEMENT, null, null);
                        world.spawnEntity(sparkEntity);

                        break;
                    }
                }
            }
        }

    }
}