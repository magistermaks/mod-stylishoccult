package net.darktree.stylishoccult.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Network {

    public static final AshS2CPacket ASH = new AshS2CPacket();
    public static final DefuseS2CPacket DEFUSE = new DefuseS2CPacket();
    public static final ToggleC2SPacket TOGGLE = new ToggleC2SPacket();
    public static final AttackS2CPacket ATTACK = new AttackS2CPacket();
    public static final ArcS2CPacket ARC = new ArcS2CPacket();
    public static final DebugS2CPacket DEBUG = new DebugS2CPacket();
    public static final MadnessC2SPacket MADNESS = new MadnessC2SPacket();
    public static final LookAtRuneC2SPacket LOOK = new LookAtRuneC2SPacket();
    public static final AltarSyncS2CPacket ALTAR_SYNC = new AltarSyncS2CPacket();

    public static void init() {
        TOGGLE.register();
        MADNESS.register();
        LOOK.register();
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        ASH.register();
        DEFUSE.register();
        ATTACK.register();
        ARC.register();
        DEBUG.register();
        ALTAR_SYNC.register();
    }

}
