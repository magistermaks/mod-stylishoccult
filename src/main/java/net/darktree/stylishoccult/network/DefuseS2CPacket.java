package net.darktree.stylishoccult.network;

import io.netty.buffer.Unpooled;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class DefuseS2CPacket {

	private final Identifier IDENTIFIER = new ModIdentifier("diffuse");

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

			for(Direction direction : Direction.values()) {
				BlockPos target = pos.offset(direction);
				BlockState state = player.world.getBlockState(target);

				if (!state.isOpaque() || !state.isSideSolidFullSquare(player.world, target, direction.getOpposite())) {
					for(int i = random.nextInt(4) + 4; i > 0; i --) {
						player.world.addParticle(
								ParticleTypes.SMOKE,
								pos.getX() + (direction.getOffsetX() == 0 ? random.nextFloat() : 0.5f + direction.getOffsetX() * 0.6f),
								pos.getY() + (direction.getOffsetY() == 0 ? random.nextFloat() : 0.5f + direction.getOffsetY() * 0.6f),
								pos.getZ() + (direction.getOffsetZ() == 0 ? random.nextFloat() : 0.5f + direction.getOffsetZ() * 0.6f),
								0, 0, 0
						);
					}
				}
			}

			player.world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.1f + random.nextFloat() / 50, 1.75f + random.nextFloat() / 10 );
		}
	}

	public void send(ServerPlayerEntity entity, BlockPos pos ) {
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
