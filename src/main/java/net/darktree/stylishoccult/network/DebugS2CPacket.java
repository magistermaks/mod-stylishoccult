package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.gui.screen.DebugRuneScreen;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class DebugS2CPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("debug");

	@Environment(EnvType.CLIENT)
	public void register() {
		ClientSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		BlockPos pos = buffer.readBlockPos();
		NbtCompound nbt = buffer.readNbt();
		context.getTaskQueue().execute(() -> apply(context.getPlayer(), pos, nbt));
	}

	private void apply(PlayerEntity player, BlockPos pos, NbtCompound nbt) {
		if (player != null && player.world != null) {
			DebugRuneScreen.open(pos, nbt);

			if (player.world instanceof ClientWorld clientWorld) {
				Random random = player.getRandom();
				float v = 0.9f + random.nextFloat() * 0.1f;
				float p = 1 + (random.nextFloat() - 0.5f) * 0.25f;

				clientWorld.playSound(pos, Sounds.INSPECT.event, Sounds.INSPECT.category, v, p, false);
			}
		}
	}

	public void send(PlayerEntity entity, BlockPos pos, NbtCompound nbt) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBlockPos(pos);
		data.writeNbt(nbt);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, IDENTIFIER, data);
	}

}
