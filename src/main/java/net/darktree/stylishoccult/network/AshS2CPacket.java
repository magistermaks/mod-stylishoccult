package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AshS2CPacket {

    private final Identifier IDENTIFIER = new ModIdentifier("ash_packet");

    @Environment(EnvType.CLIENT)
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
    }

    public void read(PacketContext context, PacketByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        context.getTaskQueue().execute(() -> apply( context.getPlayer(), pos ));
    }

    private void apply(PlayerEntity player, BlockPos pos) {
        if( player != null && player.world != null ) {
            Random random = player.world.random;

            for( int i = 0; i < random.nextInt(4) + 4; i ++ ) {
                player.world.addParticle(
                        ParticleTypes.ASH,
                        pos.getX() + random.nextFloat(),
                        pos.getY() + random.nextFloat(),
                        pos.getZ() + random.nextFloat(),
                        0, 0, 0
                );
            }
        }
    }

    public void send( ServerPlayerEntity entity, BlockPos pos ) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBlockPos( pos );
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, IDENTIFIER, data);
    }

    public void send(BlockPos pos, ServerWorld world) {
        for(ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
            if( player.getBlockPos().isWithinDistance(pos, 32.0D) ) {
                send(player, pos);
            }
        }
    }

}
