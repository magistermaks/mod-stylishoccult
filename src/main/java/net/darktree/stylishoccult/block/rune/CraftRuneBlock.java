package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.SimpleRay;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CraftRuneBlock extends TransferRuneBlock {

	public static final SlotSwitch[] SLOTS = {
			SlotSwitch.of(0)
					.n(9.75f, 9.75f, 0, 11.75f, 11.75f, 1)
					.w(0, 9.75f, 4.25f, 1, 11.75f, 6.25f)
					.s(4.25f, 9.75f, 15, 6.25f, 11.75f, 16)
					.e(15, 9.75f, 9.75f, 16, 11.75f, 11.75f),

			SlotSwitch.of(1)
					.n(7, 9.75f, 0, 9, 11.75f, 1)
					.w(0, 9.75f, 7, 1, 11.75f, 9)
					.s(7, 9.75f, 15, 9, 11.75f, 16)
					.e(15, 9.75f, 7, 16, 11.75f, 9),

			SlotSwitch.of(2)
					.n(4.25f, 9.75f, 0, 6.25f, 11.75f, 1)
					.w(0, 9.75f, 9.75f, 1, 11.75f, 11.75f)
					.s(9.75f, 9.75f, 15, 11.75f, 11.75f, 16)
					.e(15, 9.75f, 4.25f, 16, 11.75f, 6.25f),

			SlotSwitch.of(3)
					.n(9.75f, 7, 0, 11.75f, 9, 1)
					.w(0, 7, 4.25f, 1, 9, 6.25f)
					.s(4.25f, 7, 15, 6.25f, 9, 16)
					.e(15, 7, 9.75f, 16, 9, 11.75f),

			SlotSwitch.of(4)
					.n(7, 7, 0, 9, 9, 1)
					.w(0, 7, 7, 1, 9, 9)
					.s(7, 7, 15, 9, 9, 16)
					.e(15, 7, 7, 16, 9, 9),

			SlotSwitch.of(5)
					.n(4.25f, 7, 0, 6.25f, 9, 1)
					.w(0, 7, 9.75f, 1, 9, 11.75f)
					.s(9.75f, 7, 15, 11.75f, 9, 16)
					.e(15, 7, 4.25f, 16, 9, 6.25f),

			SlotSwitch.of(6)
					.n(9.75f, 4.25f, 0, 11.75f, 6.25f, 1)
					.w(0, 4.25f, 4.25f, 1, 6.25f, 6.25f)
					.s(4.25f, 4.25f, 15, 6.25f, 6.25f, 16)
					.e(15, 4.25f, 9.75f, 16, 6.25f, 11.75f),

			SlotSwitch.of(7)
					.n(7, 4.25f, 0, 9, 6.25f, 1)
					.w(0, 4.25f, 7, 1, 6.25f, 9)
					.s(7, 4.25f, 15, 9, 6.25f, 16)
					.e(15, 4.25f, 7, 16, 6.25f, 9),

			SlotSwitch.of(8)
					.n(4.25f, 4.25f, 0, 6.25f, 6.25f, 1)
					.w(0, 4.25f, 9.75f, 1, 6.25f, 11.75f)
					.s(9.75f, 4.25f, 15, 11.75f, 6.25f, 16)
					.e(15, 4.25f, 4.25f, 16, 6.25f, 6.25f),
	};

	private static final SlotSide[] SIDES = {
			SlotSide.of(null, 0, 15, 0, 16, 16, 16),
			SlotSide.of(null, 0, 0, 0, 16, 1, 16),
			SlotSide.of(Direction.NORTH, 0, 0, 0, 16, 16, 1),
			SlotSide.of(Direction.SOUTH, 0, 0, 15, 16, 16, 16),
			SlotSide.of(Direction.EAST, 15, 0, 0, 1, 16, 16),
			SlotSide.of(Direction.WEST, 0, 0, 0, 1, 16, 16)
	};

	public CraftRuneBlock(String name) {
		super(name);

		for (SlotSwitch slot : SLOTS) {
			setDefaultState(getDefaultState().with(slot.property, false));
		}
	}

	public static void toggle(World world, BlockPos pos, int slot, boolean value) {
		world.setBlockState(pos, world.getBlockState(pos).with(SLOTS[slot].property, value));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		for (SlotSwitch slot : SLOTS) {
			builder.add(slot.property);
		}

		super.appendProperties(builder);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		SimpleRay ray = new SimpleRay(player.getCameraPosVec(1), player.getRotationVec(1), pos);

		if( !player.getAbilities().allowModifyWorld ) {
			return ActionResult.PASS;
		}

		Direction direction = SlotSide.get(ray);
		if(direction != null) {

			SlotSwitch selected = null;
			float distance = Float.MAX_VALUE;

			for( SlotSwitch slot : SLOTS ) {
				float dist = slot.get(ray, direction);

				if(dist < distance) {
					distance = dist;
					selected = slot;
				}
			}

			if (selected != null) {
				if (world.isClient) {
					Network.TOGGLE.send(pos, selected.id, !state.get(selected.property));
				}

				return ActionResult.SUCCESS;
			}

		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		CraftingInventory inventory = new CraftingInventory(new DummyScreenHandler(), 3, 3);
		BlockState state = world.getBlockState(pos);

		// fill in the input inventory
		for (int i = 0; i < SLOTS.length; i++) {
			if (state.get(SLOTS[i].property)) {
				inventory.setStack(i, script.stack.pull().cast(ItemElement.class).stack);
			}
		}

		Optional<CraftingRecipe> optional = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);

		if (optional.isPresent()) {
			Recipe<CraftingInventory> recipe = optional.get();
			script.stack.push(new ItemElement(recipe.craft(inventory)));
			recipe.getRemainder(inventory).forEach(item -> script.ring.push(new ItemElement(item), world, pos));
		} else {
			for (int i = 0; i < 9; i++) {
				script.ring.push(new ItemElement(inventory.getStack(i)), world, pos);
			}
		}

		Criteria.TRIGGER.trigger(world, pos, this, optional.isPresent());
	}

	private static class SlotSide {

		public final Direction direction;
		public final Box box;

		private SlotSide(Direction direction, Box box) {
			this.direction = direction;
			this.box = box;
		}

		public static SlotSide of(@Nullable Direction direction, int x1, int y1, int z1, int x2, int y2, int z2) {
			return new SlotSide(direction, Utils.box(x1, y1, z1, x2, y2, z2));
		}

		public static Direction get(SimpleRay ray) {
			SlotSide selected = null;
			float distance = Float.MAX_VALUE;

			for(SlotSide side : SIDES) {
				float dist = ray.intersectDistance(side.box, Float.MAX_VALUE);

				if (dist < distance) {
					distance = dist;
					selected = side;
				}
			}

			return selected != null ? selected.direction : null;
		}

	}

	private static class SlotSwitch {

		public final BooleanProperty property;
		public final int id;
		public Box north, east, south, west;

		private SlotSwitch(int id) {
			this.property = BooleanProperty.of("slot_" + id);
			this.id = id;
		}

		public SlotSwitch n(float x1, float y1, float z1, float x2, float y2, float z2) {
			this.north = Utils.box(x1, y1, z1, x2, y2, z2);
			return this;
		}

		public SlotSwitch e(float x1, float y1, float z1, float x2, float y2, float z2) {
			this.east = Utils.box(x1, y1, z1, x2, y2, z2);
			return this;
		}

		public SlotSwitch s(float x1, float y1, float z1, float x2, float y2, float z2) {
			this.south = Utils.box(x1, y1, z1, x2, y2, z2);
			return this;
		}

		public SlotSwitch w(float x1, float y1, float z1, float x2, float y2, float z2) {
			this.west = Utils.box(x1, y1, z1, x2, y2, z2);
			return this;
		}

		public static SlotSwitch of(int id) {
			return new SlotSwitch(id);
		}

		public float get(SimpleRay ray, Direction direction) {
			if(direction == Direction.NORTH) return ray.intersectDistance(this.north, Float.MAX_VALUE);
			if(direction == Direction.EAST) return ray.intersectDistance(this.east, Float.MAX_VALUE);
			if(direction == Direction.SOUTH) return ray.intersectDistance(this.south, Float.MAX_VALUE);
			if(direction == Direction.WEST) return ray.intersectDistance(this.west, Float.MAX_VALUE);
			return Float.MAX_VALUE;
		}

	}

	private static class DummyScreenHandler extends ScreenHandler {

		public DummyScreenHandler() {
			super(null, 0);
		}

		public void onContentChanged(Inventory inventory) {
			// noop
		}

		@Override
		public boolean canUse(PlayerEntity player) {
			return false;
		}

	}

}
