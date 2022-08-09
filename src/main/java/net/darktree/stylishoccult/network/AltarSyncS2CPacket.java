package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class AltarSyncS2CPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("altar_sync");

	@Environment(EnvType.CLIENT)
	public void register() {
		ClientSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		NbtCompound nbt = buffer.readNbt();
		context.getTaskQueue().execute(() -> apply(context.getPlayer(), nbt));
	}

	private void apply(PlayerEntity player, NbtCompound nbt) {
		if (player != null && player.world != null) {
			NbtList rituals = nbt.getList("rituals", NbtElement.COMPOUND_TYPE);
			ResourceLoaders.ALTAR_RITUALS.sync(rituals);
		}
	}

	private void send(PacketSender sender, NbtList list) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		NbtCompound nbt = new NbtCompound();
		nbt.put("rituals", list);
		data.writeNbt(nbt);
		sender.sendPacket(IDENTIFIER, data);
	}

	public void send(PacketSender sender) {
		send(sender, ResourceLoaders.ALTAR_RITUALS.serialize());
	}

}
