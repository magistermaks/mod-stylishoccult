package net.darktree.stylishoccult.event;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.EyeBlock;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Random;

public class LookAtEvent {

    private static final Random random = new Random();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register( LookAtEvent::apply );
    }

    private static void apply( MinecraftServer server ) {

        for( ServerPlayerEntity player : server.getPlayerManager().getPlayerList() ) {

            if( !player.interactionManager.isSurvivalLike() || random.nextInt(4) != 0 ) {
                continue;
            }

            HitResult hit = player.raycast(128.0f, 0, true);
            if( hit.getType() == HitResult.Type.BLOCK ) {

                BlockState state = player.getServerWorld().getBlockState( ((BlockHitResult) hit).getBlockPos() );
                if( state.isOf( ModBlocks.EYE_FLESH ) ) {
                    EyeBlock eye = (EyeBlock) ModBlocks.EYE_FLESH;
                    eye.seenBy( state, player );
                }

            }

        }
    }

}
