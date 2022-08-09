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
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class ArcS2CPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("arc");

	@Environment(EnvType.CLIENT)
	public void register() {
		ClientSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		double fx = buffer.readDouble();
		double fy = buffer.readDouble();
		double fz = buffer.readDouble();
		double tx = buffer.readDouble();
		double ty = buffer.readDouble();
		double tz = buffer.readDouble();
		context.getTaskQueue().execute(() -> apply(context.getPlayer(), fx, fy, fz, tx, ty, tz));
	}

	private void apply(PlayerEntity player, double fx, double fy, double fz, double tx, double ty, double tz) {
		if( player != null && player.world != null ) {
			WorldRenderer renderer = Objects.requireNonNull(MinecraftClient.getInstance().world).worldRenderer;
			Particle particle = renderer.spawnParticle(Particles.ATTACK, true, true, fx, fy, fz, 0, 0, 0);

			if (particle instanceof AttackParticle attackParticle) {
				attackParticle.setTarget(tx, ty, tz, true);
			}

			renderer.spawnParticle(ParticleTypes.CLOUD, true, true, tx, ty, tz, 0, 0, 0);
			renderer.spawnParticle(ParticleTypes.CLOUD, true, false, tx, ty, tz, 0, 0, 0);
			renderer.spawnParticle(ParticleTypes.CLOUD, false, false, tx, ty, tz, 0, 0, 0);
		}
	}

	private void send(ServerPlayerEntity entity, double fx, double fy, double fz, double tx, double ty, double tz) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeDouble(fx);
		data.writeDouble(fy);
		data.writeDouble(fz);
		data.writeDouble(tx);
		data.writeDouble(ty);
		data.writeDouble(tz);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, IDENTIFIER, data);
	}

	public void send(BlockPos pos, ServerWorld world, double fx, double fy, double fz, double tx, double ty, double tz) {
		for(ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
			send(player, fx, fy, fz, tx, ty, tz);
		}
	}

}
