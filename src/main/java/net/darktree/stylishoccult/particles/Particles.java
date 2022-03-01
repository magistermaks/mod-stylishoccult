package net.darktree.stylishoccult.particles;

import net.darktree.interference.render.ParticleHelper;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Particles {

    public static DefaultParticleType ORBITING_SPARK = FabricParticleTypes.simple();
    public static DefaultParticleType SPORE = FabricParticleTypes.simple();
    public static DefaultParticleType BLOOD_SPLASH = FabricParticleTypes.simple();
    public static DefaultParticleType BLOOD_DRIPPING = FabricParticleTypes.simple();
    public static DefaultParticleType BLOOD_FALLING = FabricParticleTypes.simple();
    public static DefaultParticleType SWIRLY = ParticleHelper.register(new ModIdentifier("swirly"), true);
    public static DefaultParticleType ATTACK = ParticleHelper.register(new ModIdentifier("attack"), true);
    public static DefaultParticleType UNDER_BLOOD = ParticleHelper.register(new ModIdentifier("under_blood"), false, new Identifier("generic_0"));

    public static void init(){
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("orbiting_spark"), ORBITING_SPARK);
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("spore"), SPORE);
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("blood_splash"), BLOOD_SPLASH);
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("blood_dripping"), BLOOD_DRIPPING);
        Registry.register(Registry.PARTICLE_TYPE, new ModIdentifier("blood_falling"), BLOOD_FALLING);
    }

    public static void clientInit() {
        Utils.requestParticleTexture( new ModIdentifier( "particle/lava_spark" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/spore" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/splash_0" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/splash_1" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/splash_2" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/splash_3" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/spark_0" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/spark_1" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/spark_2" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/spark_3" ) );
        Utils.requestParticleTexture( new ModIdentifier( "particle/spark_4" ) );
        Utils.requestParticleTexture( new ModIdentifier( "misc/madness_outline" ) );

        ParticleFactoryRegistry.getInstance().register(SPORE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ORBITING_SPARK, OrbitingFlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(BLOOD_DRIPPING, BloodParticle.DrippingFactory::new);
        ParticleFactoryRegistry.getInstance().register(BLOOD_FALLING, BloodParticle.FallingFactory::new);
        ParticleFactoryRegistry.getInstance().register(BLOOD_SPLASH, WaterSplashParticle.SplashFactory::new);
        ParticleFactoryRegistry.getInstance().register(UNDER_BLOOD, BloodParticle.SuspendFactory::new);

        ParticleHelper.registerFactory(SWIRLY, SwirlyParticle::new);
        ParticleHelper.registerFactory(ATTACK, AttackParticle::new);
    }

}
