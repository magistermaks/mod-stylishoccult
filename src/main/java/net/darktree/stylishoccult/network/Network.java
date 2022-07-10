package net.darktree.stylishoccult.network;

public class Network {

    public static final AshS2CPacket ASH_PACKET = new AshS2CPacket();
    public static final DefuseS2CPacket DEFUSE_PACKET = new DefuseS2CPacket();
    public static final ToggleC2SPacket TOGGLE_PACKET = new ToggleC2SPacket();
    public static final AttackS2CPacket ATTACK = new AttackS2CPacket();
    public static final ArcS2CPacket ARC = new ArcS2CPacket();

    public static void init() {
        TOGGLE_PACKET.register();
    }

    public static void clientInit() {
        ASH_PACKET.register();
        DEFUSE_PACKET.register();
        ATTACK.register();
        ARC.register();
    }

}
