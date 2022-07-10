package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.particles.AttackParticle;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AttackS2CPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("attack");

	@Environment(EnvType.CLIENT)
	public void register() {
		ClientSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		BlockPos pos = buffer.readBlockPos();
		Direction face = Direction.byId(buffer.readInt());
		context.getTaskQueue().execute(() -> apply(context.getPlayer(), pos, x, y, z, face));
	}

	private void apply(PlayerEntity player, BlockPos pos, double x, double y, double z, Direction face) {
		if( player != null && player.world != null ) {
			Particle particle = MinecraftClient.getInstance().world.worldRenderer.spawnParticle(
					Particles.ATTACK, true, true,
					pos.getX() + 0.5f + face.getOffsetX() * 0.5f, pos.getY() + 0.5f + face.getOffsetY() * 0.5f, pos.getZ() + 0.5f + face.getOffsetZ() * 0.5f,
					0, 0, 0);

			if (particle instanceof AttackParticle attackParticle) {
				attackParticle.setTarget(x, y, z);
			}
		}
	}

	private void send(ServerPlayerEntity entity, BlockPos source, double x, double y, double z, Direction face) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeDouble(x);
		data.writeDouble(y);
		data.writeDouble(z);
		data.writeBlockPos(source);
		data.writeInt(face.getId());
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, IDENTIFIER, data);
	}

	public void send(BlockPos source, LivingEntity target, Direction face, ServerWorld world) {
		double x = target.getX(), y = (target.getEyeY() + target.getY())/2f, z = target.getZ();

		for(ServerPlayerEntity player : PlayerLookup.tracking(world, source)) {
			if( player.getBlockPos().isWithinDistance(target.getBlockPos(), 32.0D) ) {
				send(player, source, x, y, z, face);
			}
		}
	}

}

