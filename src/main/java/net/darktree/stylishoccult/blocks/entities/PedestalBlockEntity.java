package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class PedestalBlockEntity extends SimpleBlockEntity {

    private ItemStack itemStack = ItemStack.EMPTY;

    public PedestalBlockEntity() {
        super( BlockEntities.PEDESTAL );
    }

    public int getPower() {
        return itemStack.isEmpty() ? 0 : 15;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.put( "item", itemStack.toTag( new CompoundTag() ) );
        return super.toTag( tag );
    }

    @Override
    public void fromTag(CompoundTag tag) {
        try {
            itemStack = ItemStack.fromTag(tag.getCompound("item"));
        } catch (Exception ignored) {
            itemStack = ItemStack.EMPTY;
        }
        super.fromTag(tag);
    }

    private void updateNeighbours() {
        Block block = getCachedState().getBlock();
        if( world != null ) {
            world.updateNeighborsAlways(pos.down(), block);
            world.updateNeighborsAlways(pos, block);
        }

    }

    public ActionResult interact(PlayerEntity player, Hand hand ) {
        ItemStack stackInHand = player.getStackInHand( hand );
        ActionResult result = ActionResult.PASS;

        if( stackInHand.isEmpty() ) {
            if( !itemStack.isEmpty() ) {
                player.setStackInHand( hand, itemStack.copy() );
                itemStack.setCount(0);
                result = ActionResult.SUCCESS;
            }
        }else{
            if( itemStack.isEmpty() ) {
                itemStack = stackInHand.copy();
                if( !player.isCreative() ) stackInHand.setCount(0);
                result = ActionResult.SUCCESS;
            }
        }

        if( result == ActionResult.SUCCESS ) updateNeighbours();
        return result;
    }

    @Override
    public void render(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(world == null) return;

        matrices.push();

        double offset = 1.0 + ( (Math.sin( (world.getTime() + tickDelta) / 8.0) + 2.0) / 16.0 );
        matrices.translate(0.5, offset, 0.5);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((world.getTime() + tickDelta) * 4));

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                itemStack,
                ModelTransformation.Mode.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers);

        matrices.pop();
    }

    public ItemStack drop() {
        ItemStack stack = itemStack.copy();
        itemStack.setCount(0);
        return stack;
    }
}
