package net.darktree.stylishoccult.entities;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.entities.renderer.ClientModEntities;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

public class ModEntities {

    public static final EntityType<SparkEntity> LAVA_SPARK = FabricEntityTypeBuilder
            .create(SpawnGroup.MONSTER, SparkEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.3f))
            .build();

    public static final EntityType<BrainEntity> BRAIN = FabricEntityTypeBuilder
            .createMob()
            .spawnGroup(SpawnGroup.MONSTER)
            .entityFactory(BrainEntity::new)
            .dimensions(EntityDimensions.changing(2.04f, 2.04f))
            .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BrainEntity::spawnPredicate)
            .build();

    public static void init() {
        Registry.register( Registry.ENTITY_TYPE, new ModIdentifier("lava_spark"), LAVA_SPARK );
        FabricDefaultAttributeRegistry.register( LAVA_SPARK, SparkEntity.createSparkAttributes() );

        Registry.register( Registry.ENTITY_TYPE, new ModIdentifier("brain"), BRAIN );
        FabricDefaultAttributeRegistry.register( BRAIN, BrainEntity.createBrainAttributes() );
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        // hotfix for some class loader problems
        ClientModEntities.init();
    }

}
