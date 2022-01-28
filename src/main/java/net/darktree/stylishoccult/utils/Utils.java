package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class Utils {

    public static VoxelShape box( float x1, float y1, float z1, float x2, float y2, float z2 ) {
        // make sure that the x1, y1, z1 given to VoxelShapes.cuboid are
        // smaller than x2, y2, z2, required my minecraft >=1.17
        return VoxelShapes.cuboid(
                Math.min(x1, x2) / 16d,
                Math.min(y1, y2) / 16d,
                Math.min(z1, z2) / 16d,
                Math.max(x1, x2) / 16d,
                Math.max(y1, y2) / 16d,
                Math.max(z1, z2) / 16d
        );
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

    public static void decrement( PlayerEntity entity, ItemStack stack ) {
        if( !entity.isCreative() ) {
            stack.decrement(1);
        }
    }

}
