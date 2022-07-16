package net.darktree.stylishoccult.entity;

import net.darktree.stylishoccult.entity.renderer.ClientModEntities;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ModEntities {

    public static final EntityType<SparkEntity> SPARK = FabricEntityTypeBuilder
            .create(SpawnGroup.MONSTER, SparkEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.3f))
            .build();

    public static final EntityType<SporeEntity> SPORE = FabricEntityTypeBuilder
            .create(SpawnGroup.MONSTER, SporeEntity::new)
            .dimensions(EntityDimensions.fixed(0.3f, 0.3f))
            .build();

    public static void init() {
        Registry.register( Registry.ENTITY_TYPE, new ModIdentifier("lava_spark"), SPARK);
        FabricDefaultAttributeRegistry.register(SPARK, SparkEntity.createSparkAttributes());

        Registry.register( Registry.ENTITY_TYPE, new ModIdentifier("spore"), SPORE );
        FabricDefaultAttributeRegistry.register(SPORE, SporeEntity.createSporeAttributes());
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        ClientModEntities.init();
    }

}
