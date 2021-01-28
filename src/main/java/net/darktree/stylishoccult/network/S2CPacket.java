package net.darktree.stylishoccult.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public abstract class S2CPacket extends Packet {

    public S2CPacket( Identifier identifier ) {
        super(identifier);
    }

    @Environment(EnvType.CLIENT)
    public void client() {
        ClientSidePacketRegistry.INSTANCE.register(identifier, this::read);
    }

    public void write( PlayerEntity player, PacketByteBuf data ) {
        ServerSidePacketRegistry.INSTANCE.sendToPlayer( player, identifier, data );
    }

}
