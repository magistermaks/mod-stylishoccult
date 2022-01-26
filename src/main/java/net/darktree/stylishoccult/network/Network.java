package net.darktree.stylishoccult.network;

public class Network {

    public static final AshS2CPacket ASH_PACKET = new AshS2CPacket();

    public static void init() {
        // load class
    }

    public static void clientInit() {
        ASH_PACKET.register();
    }

}
