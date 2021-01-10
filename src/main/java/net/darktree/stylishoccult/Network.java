package net.darktree.stylishoccult;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.blocks.entities.AbstractCandleHolderBlockEntity;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Network {

    public static final Identifier CANDLE_UPDATE_PACKET = new ModIdentifier("candle_update");

    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(CANDLE_UPDATE_PACKET, (ctx, data) -> {

            BlockPos pos = data.readBlockPos();
            int id = data.readByte();
            int op = data.readByte();
            boolean offhand = data.readBoolean();

            ctx.getTaskQueue().execute(() -> apply( ctx.getPlayer(), pos, id, op, offhand ));
        });
    }

    private static void apply( PlayerEntity player, BlockPos pos, int id, int op, boolean offhand ) {

        if( player != null && player.world != null && player.world.canSetBlock(pos) ) {

            AbstractCandleHolderBlockEntity entity = BlockUtils.getEntity(
                    AbstractCandleHolderBlockEntity.class,
                    player.world,
                    pos);

            if( entity != null ) {
                entity.applyAction( player, id, op, offhand );
            }

        }

    }

    @Environment(EnvType.CLIENT)
    public static void send( BlockPos pos, int id, int op, Hand hand ) {

        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());

        data.writeBlockPos( pos );
        data.writeByte( (byte) id );
        data.writeByte( (byte) op );
        data.writeBoolean( hand == Hand.OFF_HAND );

        ClientSidePacketRegistry.INSTANCE.sendToServer( CANDLE_UPDATE_PACKET, data );

        // apply action locally
        apply( MinecraftClient.getInstance().player, pos, id, op, hand == Hand.OFF_HAND );

    }

}
