package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.entities.BlockEntities;
import net.darktree.stylishoccult.blocks.entities.LavaDemonBlockEntity;
import net.darktree.stylishoccult.enums.LavaDemonMaterial;
import net.darktree.stylishoccult.enums.LavaDemonPart;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class LavaDemonBlock extends SimpleBlockWithEntity {

    public static final IntProperty ANGER = IntProperty.of("anger", 0, 2);
    public static final EnumProperty<LavaDemonPart> PART = EnumProperty.of("part", LavaDemonPart.class);
    public static final BooleanProperty CAN_SPREAD = BooleanProperty.of("can_spread");
    public static final EnumProperty<LavaDemonMaterial> MATERIAL = EnumProperty.of("material", LavaDemonMaterial.class);

    public LavaDemonBlock() {
        super(RegUtil.settings( Material.STONE, Sounds.LAVA_DEMON, 4.0F, 16.0F, false )
                .luminance( state -> state.get(ANGER) > 0 ? 3 : 0 )
                .ticksRandomly()
                .requiresTool());

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(ANGER, 0)
                .with(PART, LavaDemonPart.BODY)
                .with(CAN_SPREAD, true)
                .with(MATERIAL, LavaDemonMaterial.STONE));
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.LAVA_DEMON;
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        super.onStacksDropped(state, world, pos, stack);

        int i = 0;
        if( state.get(PART) == LavaDemonPart.BODY ) {
            i = MathHelper.nextInt(world.random, 1, 3);
        }else if( state.get(PART) == LavaDemonPart.EMITTER ) {
            i = MathHelper.nextInt(world.random, 5, 7);
        }else if( state.get(PART) == LavaDemonPart.HEAD ) {
            i = MathHelper.nextInt(world.random, 7, 20);
        }

        this.dropExperience( world, pos, i );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add( ANGER, PART, CAN_SPREAD, MATERIAL );
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if( !world.isClient() ){
            if(state.get(ANGER) > 0){
                entity.setFireTicks( world.getRandom().nextInt(20 * 4) + 20 * 4 );
            }
        }
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        world.setBlockState(pos, state.with(ANGER, 2));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        LavaDemonPart part = state.get(PART);
        int anger = state.get(ANGER);

        if( anger > 0 && part == LavaDemonPart.EMITTER ){
            BlockPos posUp = pos.up();
            if ( !world.getBlockState(posUp).isOpaqueFullCube(world, posUp) ) {
                if (random.nextInt(20) == 0) {
                    float d = ((float) pos.getX()) + 0.5f;
                    float e = ((float) pos.getY()) + 1.0f;
                    float f = ((float) pos.getZ()) + 0.5f;
                    world.addParticle(ParticleTypes.LAVA, d, e, f, 0.0D, -0.1D, 0.0D);
                }
                if(random.nextInt(10) == 0) {
                    float d = ((float) pos.getX()) + random.nextFloat();
                    float e = ((float) pos.getY()) + 1.0f;
                    float f = ((float) pos.getZ()) + random.nextFloat();
                    world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.1D, 0.0D);
                }
            }
        }
    }

    @Override
    public float getHardness(BlockState state, BlockView world, BlockPos pos) {
        LavaDemonPart part = state.get(PART);
        float hardness = getStoredHardness(state);

        if( part == LavaDemonPart.BODY ) {
            return hardness;
        }else if( part == LavaDemonPart.EMITTER ) {
            return hardness + 11;
        }else if( part == LavaDemonPart.HEAD ) {
            return hardness + 33;
        }

        return hardness;
    }

    public LavaDemonMaterial getDisguise( World world, BlockPos pos ) {
        LavaDemonMaterial material = LavaDemonMaterial.STONE;
        Direction[] values = Direction.values();

        for (Direction value : values) {
            LavaDemonMaterial material2 = LavaDemonMaterial.getFrom(world.getBlockState(pos.offset(value)).getBlock());
            if (material2 != null && (material2.getLevel() > material.getLevel())) {
                material = material2;
            }
        }

        if( material == LavaDemonMaterial.STONE && RandUtils.getBool( StylishOccult.SETTINGS.lavaDemonRandomDisguise ) ) {
            material = RandUtils.getEnum( LavaDemonMaterial.class );
        }

        return material;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( world.isClient ) {
            return;
        }

        // Cleansing
        OccultHelper.cleanseAround(world, pos, 4, 4, 20);

        // Spreading
        if( state.get(CAN_SPREAD) ) {
            BlockPos target = pos.offset( RandUtils.getEnum(Direction.class) );
            BlockPos origin = BlockUtils.find(
                    world,
                    target,
                    ModBlocks.LAVA_DEMON,
                    StylishOccult.SETTINGS.lavaDemonMaxSearchRadius,
                    (BlockState s) -> s.get(PART) == LavaDemonPart.HEAD);

            if( world.getBlockState(target).getBlock() == net.minecraft.block.Blocks.STONE ){
                if( origin != null ) {
                    int j = BlockUtils.touchesAir(world, pos)
                            ? StylishOccult.SETTINGS.lavaDemonSpreadLockAirRarity
                            : StylishOccult.SETTINGS.lavaDemonSpreadLockDefaultRarity;

                    BlockState targetState = ModBlocks.LAVA_DEMON.getDefaultState();

                    if( random.nextInt(j) < BlockUtils.fastDistance(origin, pos) ) {
                        // Unable to spread! To far from origin!
                        world.setBlockState( pos, state.with(CAN_SPREAD, false) );
                    }

                    if( BlockUtils.touchesAir(world, target) ) {
                        if( random.nextInt( StylishOccult.SETTINGS.lavaDemonEmitterAirRarity ) <= 3 ) {
                            targetState = targetState.with(PART, LavaDemonPart.EMITTER);
                        }
                    }else{
                        if( random.nextInt( StylishOccult.SETTINGS.lavaDemonEmitterDefaultRarity ) <= 3 ) {
                            targetState = targetState.with(PART, LavaDemonPart.EMITTER);
                        }
                    }

                    targetState = targetState.with(MATERIAL, getDisguise(world, target));

                    world.setBlockState( target, targetState );
                }else{
                    // Unable to spread! No origin found!
                    world.setBlockState( pos, state.with(CAN_SPREAD, false) );
                }
            }else{
                if( (origin != null) && (random.nextInt(6) < BlockUtils.fastDistance(origin, pos)) ) {
                    // Unable to spread! Target is not spreadable!
                    world.setBlockState( pos, state.with(CAN_SPREAD, false) );
                }
            }
        }

        // Calming 2 -> 1
        if( state.get(ANGER) == 2 ){
            if( RandUtils.getBool( StylishOccult.SETTINGS.lavaDemonCalmChance1 ) ) {
                world.setBlockState(pos, state.with(ANGER, 1));
                return;
            }
        }

        // Calming 1 -> 0
        if( state.get(ANGER) == 1 ){
            if( RandUtils.getBool( StylishOccult.SETTINGS.lavaDemonCalmChance2 ) ) {
                if( BlockUtils.find(world, pos, ModBlocks.LAVA_DEMON, StylishOccult.SETTINGS.lavaDemonCalmRadius, (BlockState s) -> s.get(ANGER) == 2) == null ) {
                    world.setBlockState( pos, state.with(ANGER, 0) );
                }
            }
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LavaDemonBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockEntities.LAVA_DEMON, LavaDemonBlockEntity::tick);
    }

}