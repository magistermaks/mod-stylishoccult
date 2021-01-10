package net.darktree.stylishoccult.entities;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ModEntities {

    public static final EntityType<SparkEntity> LAVA_SPARK = FabricEntityTypeBuilder
            .create(SpawnGroup.MONSTER, SparkEntity::new)
            .dimensions(EntityDimensions.fixed(0.125f, 0.125f))
            .build();

    public static void init() {
        Registry.register( Registry.ENTITY_TYPE, new ModIdentifier("lava_spark"), LAVA_SPARK );
        FabricDefaultAttributeRegistry.register( LAVA_SPARK, SparkEntity.createSparkAttributes() );
    }

    public static void clientInit() {
        EntityRendererRegistry.INSTANCE.register( ModEntities.LAVA_SPARK, (dis, ctx) -> new VoidEntityRenderer<SparkEntity>(dis) );
    }

}
