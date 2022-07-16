package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.block.rune.CraftRuneBlock;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToggleC2SPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("toggle");

	public void register() {
		ServerSidePacketRegistry.INSTANCE.register(IDENTIFIER, this::read);
	}

	public void read(PacketContext context, PacketByteBuf buffer) {
		BlockPos pos = buffer.readBlockPos();
		int slot = buffer.readInt();
		boolean value = buffer.readBoolean();

		context.getTaskQueue().execute(() -> apply(context.getPlayer(), pos, slot, value));
	}

	private void apply(PlayerEntity player, BlockPos pos, int slot, boolean value) {
		if( player != null && player.world != null ) {
			World world = player.world;

			if(world.isChunkLoaded(pos) && slot >= 0 && slot <= 8) {
				CraftRuneBlock.toggle(world, pos, slot, value);
				world.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 1, 1);
			}
		}
	}

	public void send(BlockPos pos, int slot, boolean value) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBlockPos(pos);
		data.writeInt(slot);
		data.writeBoolean(value);
		ClientSidePacketRegistry.INSTANCE.sendToServer(IDENTIFIER, data);
	}

}
