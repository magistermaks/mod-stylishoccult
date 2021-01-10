package net.darktree.stylishoccult.particles;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class Particles {

    public static DefaultParticleType LAVA_SPARK = FabricParticleTypes.simple();
    public static DefaultParticleType CANDLE_FLAME = FabricParticleTypes.simple();
    public static DefaultParticleType ORBITING_SPARK = FabricParticleTypes.simple();

    public static void init(){
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("lava_spark"), LAVA_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("candle_flame"), CANDLE_FLAME);
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("orbiting_spark"), ORBITING_SPARK);
    }

    public static void clientInit() {
        Utils.requestParticleTexture( new ModIdentifier( "particle/lava_spark" ) );
        ParticleFactoryRegistry.getInstance().register(LAVA_SPARK, SimpleSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CANDLE_FLAME, SimpleFlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ORBITING_SPARK, OrbitingFlameParticle.Factory::new);
    }

}
