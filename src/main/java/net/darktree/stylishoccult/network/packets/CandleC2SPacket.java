package net.darktree.stylishoccult.network.packets;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.blocks.entities.AbstractCandleHolderBlockEntity;
import net.darktree.stylishoccult.network.C2SPacket;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CandleC2SPacket extends C2SPacket {

    public CandleC2SPacket(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void read(PacketContext context, PacketByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        int id = buffer.readByte();
        int op = buffer.readByte();
        boolean offhand = buffer.readBoolean();

        context.getTaskQueue().execute(() -> apply( context.getPlayer(), pos, id, op, offhand ));
    }

    private void apply(PlayerEntity player, BlockPos pos, int id, int op, boolean offhand ) {
        if( player != null && player.world != null && player.world.canSetBlock(pos) ) {

            AbstractCandleHolderBlockEntity<?> entity = BlockUtils.getEntity(
                    AbstractCandleHolderBlockEntity.class,
                    player.world,
                    pos);

            if( entity != null ) {
                entity.applyAction( player, id, op, offhand );
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void send( BlockPos pos, int id, int op, Hand hand ) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());

        data.writeBlockPos( pos );
        data.writeByte( (byte) id );
        data.writeByte( (byte) op );
        data.writeBoolean( hand == Hand.OFF_HAND );

        write( data );

        // apply action locally
        apply( MinecraftClient.getInstance().player, pos, id, op, hand == Hand.OFF_HAND );
    }

}
