package net.darktree.stylishoccult.network.packets;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.network.S2CPacket;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AshS2CPacket extends S2CPacket {

    public AshS2CPacket(Identifier identifier) {
        super(identifier);
    }

    @Override
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
        write( entity, data );
    }

    public void send(BlockPos pos, ServerWorld world) {
        for(ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
            if( player.getBlockPos().isWithinDistance(pos, 32.0D) ) {
                send(player, pos);
            }
        }
    }


}
