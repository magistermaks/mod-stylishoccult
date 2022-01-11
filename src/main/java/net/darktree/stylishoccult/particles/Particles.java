package net.darktree.stylishoccult.particles;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class Particles {

    public static DefaultParticleType ORBITING_SPARK = FabricParticleTypes.simple();
    public static DefaultParticleType SPORE = FabricParticleTypes.simple();

    public static void init(){
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("orbiting_spark"), ORBITING_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("spore"), SPORE);
    }

    public static void clientInit() {
        // Is this even still necessary? To lazy to check
        Utils.requestParticleTexture( new ModIdentifier( "particle/lava_spark" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/spore" ) );

        ParticleFactoryRegistry.getInstance().register(SPORE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ORBITING_SPARK, OrbitingFlameParticle.Factory::new);
    }

}
