package net.darktree.stylishoccult.parts;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;

public class CandleRenderer {

    static final BlockState CANDLE = ModBlocks.CANDLE.getDefaultState();
    static final BlockState EXTINGUISHED_CANDLE = ModBlocks.EXTINGUISHED_CANDLE.getDefaultState();
    static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static void render( CandleStateInfo stateInfo, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay ) {
        if( stateInfo != null && stateInfo.layers != 0 ) {
            BlockState state;
            float[] point = stateInfo.getOffset();

            if( stateInfo.lit ) {
                state = CANDLE.with(Properties.LAYERS, (int) stateInfo.layers);
            }else{
                state = EXTINGUISHED_CANDLE.with(Properties.LAYERS, (int) stateInfo.layers);
            }

            matrices.push();
            matrices.translate( point[0], point[1], point[2] );

            CLIENT.getBlockRenderManager().renderBlockAsEntity(
                    state,
                    matrices,
                    vertexConsumers,
                    light,
                    overlay);

            matrices.pop();
        }
    }

}
