package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class Utils {

    public static VoxelShape box( float x1, float y1, float z1, float x2, float y2, float z2 ) {
        return VoxelShapes.cuboid( x1/16.0f, y1/16.0f, z1/16.0f, x2/16.0f, y2/16.0f, z2/16.0f );
    }

    public static void requestParticleTexture( Identifier id ) {
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE).register(((texture, registry) -> registry.register( id )));
    }

    public static VoxelShape combine( VoxelShape[] shapes, BooleanBiFunction function ) {
        VoxelShape finalShape = VoxelShapes.empty();
        for( VoxelShape shape : shapes ) {
            finalShape = VoxelShapes.combine( finalShape, shape, function );
        }
        return finalShape;
    }

    public static VoxelShape join( VoxelShape... shapes ) {
        VoxelShape finalShape = VoxelShapes.empty();

        for( VoxelShape shape : shapes ) {
            finalShape = VoxelShapes.combine( finalShape, shape, BooleanBiFunction.OR );
        }

        return finalShape;
    }

    public static MutableText tooltip( String text, Object... args ) {
        return new TranslatableText( "tooltip." + StylishOccult.NAMESPACE + "." + text, args ).formatted( Formatting.GRAY );
    }

    public static MutableText tooltip( String text ) {
        return new TranslatableText( "tooltip." + StylishOccult.NAMESPACE + "." + text ).formatted( Formatting.GRAY );
    }

}
