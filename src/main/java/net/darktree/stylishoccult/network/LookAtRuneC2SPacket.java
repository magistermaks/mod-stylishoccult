package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.block.rune.trigger.LookAtRuneBlock;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LookAtRuneC2SPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("look_at_rune");

	public void register() {
		ServerSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		BlockPos pos = buffer.readBlockPos();
		context.getTaskQueue().execute(() -> apply(context.getPlayer(), pos));
	}

	private void apply(PlayerEntity player, BlockPos pos) {
		if (player != null && player.world != null) {
			if (player.world.isChunkLoaded(pos) && player.getBlockPos().isWithinDistance(pos, 256)) {
				if (player.world.getBlockState(pos).getBlock() instanceof LookAtRuneBlock rune) {
					rune.onLookAtServer(player.world, pos, player);
				}
			}
		}
	}

	public void send(BlockPos pos) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBlockPos(pos);
		ClientSidePacketRegistry.INSTANCE.sendToServer(IDENTIFIER, data);
	}

}
