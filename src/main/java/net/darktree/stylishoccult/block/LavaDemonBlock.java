package net.darktree.stylishoccult.block;

import net.darktree.interference.api.DefaultLoot;
import net.darktree.interference.api.MutableHardness;
import net.darktree.interference.mixin.HardnessAccessor;
import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.entity.demon.LavaDemonBlockEntity;
import net.darktree.stylishoccult.block.property.LavaDemonMaterial;
import net.darktree.stylishoccult.block.property.LavaDemonPart;
import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class LavaDemonBlock extends BlockWithEntity implements MutableHardness, DefaultLoot {

	public static final IntProperty ANGER = IntProperty.of("anger", 0, 2);
	public static final EnumProperty<LavaDemonPart> PART = EnumProperty.of("part", LavaDemonPart.class);
	public static final BooleanProperty CAN_SPREAD = BooleanProperty.of("can_spread");
	public static final EnumProperty<LavaDemonMaterial> MATERIAL = EnumProperty.of("material", LavaDemonMaterial.class);

	public LavaDemonBlock() {
		super(RegUtil.settings(Material.STONE, Sounds.LAVA_DEMON, 4.0F, 6.0F, false)
				.luminance(state -> state.get(ANGER) > 0 ? 3 : 0)
				.ticksRandomly()
				.requiresTool());

		this.setDefaultState(this.stateManager.getDefaultState()
				.with(ANGER, 0)
				.with(PART, LavaDemonPart.BODY)
				.with(CAN_SPREAD, true)
				.with(MATERIAL, LavaDemonMaterial.STONE));
	}

	@Override
	public List<ItemStack> getDefaultStacks(BlockState state, LootContext.Builder builder, Identifier identifier, LootContext lootContext, ServerWorld serverWorld, net.minecraft.loot.LootTable lootTable) {
		return LootManager.dispatch(state, builder, this.lootTableId, LootTables.LAVA_DEMON);
	}

	@Override
	public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
		super.onStacksDropped(state, world, pos, stack);

		int i = switch (state.get(PART)) {
			case BODY -> RandUtils.rangeInt(1, 3, world.random);
			case EMITTER -> RandUtils.rangeInt(5, 7, world.random);
			case HEAD -> RandUtils.rangeInt(7, 20, world.random);
		};

		this.dropExperience(world, pos, i);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ANGER, PART, CAN_SPREAD, MATERIAL);
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (!world.isClient()){
			if (state.get(ANGER) > 0){
				entity.setFireTicks(world.getRandom().nextInt(20 * 4) + 20 * 4);
			}
		}
	}

	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		world.setBlockState(pos, state.with(ANGER, 2));

		if (!world.isClient && player != null) {
			Criteria.WAKE.trigger((ServerPlayerEntity) player);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		LavaDemonPart part = state.get(PART);
		int anger = state.get(ANGER);

		if (anger > 0 && part == LavaDemonPart.EMITTER) {
			BlockPos posUp = pos.up();
			if (!world.getBlockState(posUp).isOpaqueFullCube(world, posUp)) {
				if (random.nextInt(20) == 0) {
					float d = ((float) pos.getX()) + 0.5f;
					float e = ((float) pos.getY()) + 1.0f;
					float f = ((float) pos.getZ()) + 0.5f;
					world.addParticle(ParticleTypes.LAVA, d, e, f, 0.0D, -0.1D, 0.0D);
				}

				if (random.nextInt(10) == 0) {
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
		float hardness = ((HardnessAccessor) state).getStoredHardness();

		if (part == LavaDemonPart.BODY) {
			return hardness;
		}else if (part == LavaDemonPart.EMITTER) {
			return hardness * 2;
		}else if (part == LavaDemonPart.HEAD) {
			return hardness * 3;
		}

		return hardness;
	}

	public LavaDemonMaterial getDisguise(World world, BlockPos pos, Random random) {
		LavaDemonMaterial material = LavaDemonMaterial.STONE;

		for (Direction value : Directions.ALL) {
			LavaDemonMaterial neighbour = LavaDemonMaterial.getFrom(world.getBlockState(pos.offset(value)).getBlock());

			if (neighbour != null && (neighbour.getLevel() > material.getLevel())) {
				material = neighbour;
			}
		}

		if (material == LavaDemonMaterial.STONE && RandUtils.getBool(StylishOccult.SETTING.disguise_chance, random)) {
			material = RandUtils.getEnum(LavaDemonMaterial.class, random);
		}

		return material;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isClient) {
			return;
		}

		// Cleansing
		OccultHelper.cleanseAround(world, pos, 4, 4, 20);

		// Spreading
		if (state.get(CAN_SPREAD)) {
			BlockPos target = pos.offset(RandUtils.getEnum(Direction.class, random));
			BlockPos origin = BlockUtils.find(
					world,
					target,
					ModBlocks.LAVA_DEMON,
					StylishOccult.SETTING.max_search_radius,
					(BlockState s) -> s.get(PART) == LavaDemonPart.HEAD);

			if (world.getBlockState(target).getBlock() == net.minecraft.block.Blocks.STONE) {
				if (origin != null) {
					int j = BlockUtils.touchesAir(world, pos)
							? StylishOccult.SETTING.spread_lock_exposed_rarity
							: StylishOccult.SETTING.spread_lock_buried_rarity;

					BlockState targetState = ModBlocks.LAVA_DEMON.getDefaultState();

					if (random.nextInt(j) < origin.getManhattanDistance(pos)) {
						// Unable to spread! To far from origin!
						world.setBlockState( pos, state.with(CAN_SPREAD, false) );
					}

					if (BlockUtils.touchesAir(world, target)) {
						if (RandUtils.getBool(StylishOccult.SETTING.emitter_exposed, random)) {
							targetState = targetState.with(PART, LavaDemonPart.EMITTER);
						}
					} else {
						if (RandUtils.getBool(StylishOccult.SETTING.emitter_buried, random)) {
							targetState = targetState.with(PART, LavaDemonPart.EMITTER);
						}
					}

					targetState = targetState.with(MATERIAL, getDisguise(world, target, random));

					world.setBlockState(target, targetState);
				} else {
					// Unable to spread! No origin found!
					world.setBlockState(pos, state.with(CAN_SPREAD, false));
				}
			} else {
				if ((origin != null) && (random.nextInt(6) < origin.getManhattanDistance(pos))) {
					// Unable to spread! Target is not spreadable!
					world.setBlockState(pos, state.with(CAN_SPREAD, false));
				}
			}
		}

		// Calming 2 -> 1
		if (state.get(ANGER) == 2) {
			if (RandUtils.getBool(StylishOccult.SETTING.calm_chance_1, random)) {
				world.setBlockState(pos, state.with(ANGER, 1));
				return;
			}
		}

		// Calming 1 -> 0
		if (state.get(ANGER) == 1){
			if (RandUtils.getBool(StylishOccult.SETTING.calm_chance_2, random)) {
				if (BlockUtils.find(world, pos, ModBlocks.LAVA_DEMON, StylishOccult.SETTING.calm_radius, (BlockState s) -> s.get(ANGER) == 2) == null ) {
					world.setBlockState(pos, state.with(ANGER, 0));
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
		return checkType(type, BlockEntities.LAVA_DEMON, (w, p, s, entity) -> entity.tick(w, p, s));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public BlockState getPartPlacementState(LavaDemonPart part, ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Random random = new Random(pos.asLong());

		return getDefaultState().with(MATERIAL, getDisguise(world, pos, random)).with(ANGER, 2).with(PART, part);
	}

}