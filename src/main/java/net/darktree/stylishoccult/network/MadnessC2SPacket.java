package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.advancement.ModCriteria;
import net.darktree.stylishoccult.overlay.PlayerEntityMadnessDuck;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MadnessC2SPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("madness");

	public void register() {
		ServerSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		float madness = buffer.readFloat();
		context.getTaskQueue().execute(() -> apply(context.getPlayer(), madness));
	}

	private void apply(PlayerEntity player, float madness) {
		if (player instanceof ServerPlayerEntity serverPlayer) {
			((PlayerEntityMadnessDuck) player).stylish_setMadness(madness);
			ModCriteria.INSIGHT.trigger(serverPlayer, madness);
		}
	}

	public void send(float madness) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeFloat(madness);
		ClientSidePacketRegistry.INSTANCE.sendToServer(IDENTIFIER, data);
	}

}
