package net.darktree.stylishoccult.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public abstract class C2SPacket extends Packet {

    public C2SPacket( Identifier identifier ) {
        super(identifier);
        ServerSidePacketRegistry.INSTANCE.register(identifier, this::read);
    }

    @Environment(EnvType.CLIENT)
    public void write( PacketByteBuf data ) {
        ClientSidePacketRegistry.INSTANCE.sendToServer( identifier, data );
    }

}
