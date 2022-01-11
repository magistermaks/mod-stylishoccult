package net.darktree.stylishoccult.network;

import net.darktree.stylishoccult.network.packets.AshS2CPacket;
import net.darktree.stylishoccult.utils.ModIdentifier;

public class Network {

    public static final AshS2CPacket ASH_PACKET = new AshS2CPacket( new ModIdentifier("ash_packet") );

    public static void init() {
        // load class
    }

    public static void clientInit() {
        ASH_PACKET.client();
    }

}
