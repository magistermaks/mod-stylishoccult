package net.darktree.stylishoccult.network;

import net.darktree.stylishoccult.network.packets.AshS2CPacket;
import net.darktree.stylishoccult.network.packets.CandleC2SPacket;
import net.darktree.stylishoccult.utils.ModIdentifier;

public class Network {

    public static final CandleC2SPacket CANDLE_PACKET = new CandleC2SPacket( new ModIdentifier("candle_packet") );
    public static final AshS2CPacket ASH_PACKET = new AshS2CPacket( new ModIdentifier("ash_packet") );

    public static void init() {
        // load class
    }

    public static void clientInit() {
        clientInit(
                CANDLE_PACKET,
                ASH_PACKET
        );
    }

    private static void clientInit( Packet... args ) {
        for( Packet arg : args ) {
            arg.client();
        }
    }

}
