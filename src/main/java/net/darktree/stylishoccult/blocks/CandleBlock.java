package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.loot.BakedLootTable;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CandleBlock extends SimpleBlock {

    public static final IntProperty LAYERS = Properties.LAYERS;
    protected static final VoxelShape[] OUTLINE_SHAPE = new VoxelShape [8];

    public CandleBlock() {
        super(RegUtil.settings( Material.SUPPORTED, Sounds.CANDLE, 0.1F, 1.0f, false )
                .ticksRandomly()
                .luminance( state -> state.get( LAYERS ) > 1 ? 14 : 0 )
        );
    }

    public CandleBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LAYERS, 8));
    }

    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if( player.getStackInHand(hand).isEmpty() ) {
            world.setBlockState(pos, ModBlocks.EXTINGUISHED_CANDLE.getDefaultState().with( LAYERS, state.get( LAYERS ) ));
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.06F, world.random.nextFloat() * 0.4F + 2.0F);

            if( !world.isClient ) {
                addParticle(state, world, pos, ParticleTypes.SMOKE, 2);
            }

            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public boolean canReplace(BlockState state, ItemPlacementContext ctx) {
        return state.get( LAYERS ) == 1;
    }

    public float getHardness(BlockState state, BlockView world, BlockPos pos) {
        return world.getBlockState(pos).get( LAYERS ) != 1 ? getStoredHardness(state) : 0.0f;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add( LAYERS );
    }

    @Deprecated
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( (random.nextInt( StylishOccult.SETTINGS.candleLayerBurnoutRarity ) == 0) || world.hasRain(pos) ) {
            int layers = state.get( LAYERS ) - 1;
            if( layers > 1 ){
                world.setBlockState(pos, state.with( LAYERS, layers ));
            }else if( layers == 1 ) {
                world.setBlockState(pos, ModBlocks.EXTINGUISHED_CANDLE.getDefaultState().with( LAYERS, 1 ));
            }else{
                world.setBlockState(pos, ModBlocks.EXTINGUISHED_CANDLE.getDefaultState().with( LAYERS, 1 ));
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.05F, world.random.nextFloat() * 0.4F + 2.0F);
            }
        }
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        addParticle(state, world, pos, Particles.CANDLE_FLAME, 1);
        world.getBlockTickScheduler().schedule(pos, this, 40);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return OUTLINE_SHAPE[ state.get( LAYERS ) - 1 ];
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.CANDLE;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        addParticle( state, world, pos, Particles.CANDLE_FLAME, 0 );
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return sideCoversSmallSquare(world, pos.down(), Direction.UP);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack it = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        CompoundTag tag2 = new CompoundTag();
        tag2.putString( "layers", String.valueOf( state.get(Properties.LAYERS) ) );
        tag.put( "BlockStateTag", tag2 );
        it.setTag(tag);

        return it;
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
        initScheduler( world, pos );
    }

    public void initScheduler(World world, BlockPos pos) {
        if( !world.getBlockTickScheduler().isScheduled(pos, this) )  {
            world.getBlockTickScheduler().schedule(pos, this, RandUtils.rangeInt(0, 40));
        }
    }

    protected void addParticle(BlockState state, World world, BlockPos pos, ParticleEffect type, int server) {
        int layers = state.get( LAYERS );

        double d = (double)pos.getX() + 0.5D;
        double e = (double)pos.getY() + 0.15D + 0.059D * layers;
        double f = (double)pos.getZ() + 0.5D;

        if( world.isClient && (layers != 1) ){
            world.addParticle(type, d, e, f, 0.0D, 0.0D, 0.0D);
        }else{
            if( server != 0 ) {
                ((ServerWorld) world).spawnParticles(type, d, e, f, server, 0, 0, 0, 0);
            }
        }
    }

    static {
        OUTLINE_SHAPE[7] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 8.0D, 9.0D);
        OUTLINE_SHAPE[6] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D);
        OUTLINE_SHAPE[5] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D);
        OUTLINE_SHAPE[4] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 5.0D, 9.0D);
        OUTLINE_SHAPE[3] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 4.0D, 9.0D);
        OUTLINE_SHAPE[2] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D);
        OUTLINE_SHAPE[1] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D);
        OUTLINE_SHAPE[0] = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 1.0D, 9.0D);
    }

}
