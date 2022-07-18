package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.overlay.PlayerEntityDuck;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class MadnessC2SPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("madness");

	public void register() {
		ServerSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		context.getTaskQueue().execute(() -> apply(context.getPlayer()));
	}

	private void apply(PlayerEntity player) {
		if (player != null) {
			((PlayerEntityDuck) player).stylish_addMadness(0.03f);
		}
	}

	public void send() {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		ClientSidePacketRegistry.INSTANCE.sendToServer(IDENTIFIER, data);
	}

}
