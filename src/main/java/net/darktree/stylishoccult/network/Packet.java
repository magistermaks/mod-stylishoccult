package net.darktree.stylishoccult.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public abstract class Packet {

    protected final Identifier identifier;

    public Packet( Identifier identifier ) {
        this.identifier = identifier;
    }

    @Environment(EnvType.CLIENT)
    public void client() {

    }

    public void read(PacketContext context, PacketByteBuf buffer ) {

    }

}
