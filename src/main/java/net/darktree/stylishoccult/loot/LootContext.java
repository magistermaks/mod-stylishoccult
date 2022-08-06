package net.darktree.stylishoccult.loot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LootContext {

    final private World world;
    final private BlockPos pos;
    final private Entity entity;
    final private ItemStack tool;
    final private BlockState blockState;
    final private BlockEntity blockEntity;
    final private float power;

    private LootContext(World world, BlockPos pos, BlockState blockState, BlockEntity blockEntity, Entity entity, ItemStack tool, float power) {
        this.world = world;
        this.pos = pos;
        this.entity = entity;
        this.tool = tool;
        this.blockState = blockState;
        this.blockEntity = blockEntity;
        this.power = power;
    }

    public float getExplosionPower() {
        return power;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return blockState;
    }

    public Block getBlock() {
        return getState().getBlock();
    }

    public ItemStack getBlockItem() {
        return new ItemStack(getState().getBlock());
    }

    public Entity getEntity() {
        return entity;
    }

    public PlayerEntity getPlayer() {
        return (entity instanceof PlayerEntity) ? (PlayerEntity) entity : null;
    }

    public ItemStack getTool() {
        return tool;
    }

    public boolean toolHasEnchantment(Enchantment enchantment) {
        return toolGetEnchantment(enchantment) != 0;
    }

    public int toolGetEnchantment(Enchantment enchantment) {
        return EnchantmentHelper.getLevel(enchantment, getTool());
    }

    public boolean shouldDrop() {
        PlayerEntity player = getPlayer();
        return player == null || !player.isCreative();
    }

    public <T extends BlockEntity> T getBlockEntity( Class<T> clazz ) {
        if (clazz.isInstance(blockEntity)) {
            return clazz.cast(blockEntity );
        }
        return null;
    }

    public <T extends Block> T getBlock( Class<T> clazz ) {
        Block block = getBlock();
        return clazz.isInstance( block ) ? clazz.cast( block ) : null;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static class Builder {

        private final World world;
        private final BlockPos pos;
        private BlockState blockState;
        private BlockEntity blockEntity;
        private Entity entity;
        private ItemStack tool;
        private float power;

        public Builder(World world, BlockPos pos) {
            if (world == null || pos == null) {
                throw new RuntimeException( "Invalid arguments for LootContext.Builder!" );
            }

            this.world = world;
            this.pos = pos;
            this.blockState = null;
            this.blockEntity = null;
            this.entity = null;
            this.tool = ItemStack.EMPTY;
            this.power = 0.0f;
        }

        public Builder setBlockState(BlockState blockState) {
            this.blockState = blockState;
            return this;
        }

        public Builder setEntity(Entity entity) {
            this.entity = entity;
            return this;
        }

        public Builder setTool(ItemStack tool) {
            this.tool = (tool == null) ? ItemStack.EMPTY : tool;
            return this;
        }

        public Builder setBlockEntity(BlockEntity blockEntity) {
            this.blockEntity = blockEntity;
            return this;
        }

        public Builder setExplosionPower(float power) {
            this.power = power;
            return this;
        }

        public LootContext build() {
            return new LootContext( world, pos, blockState, blockEntity, entity, tool, power );
        }

    }

}
